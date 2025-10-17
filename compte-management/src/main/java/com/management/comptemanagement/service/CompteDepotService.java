package com.management.comptemanagement.service;

import com.management.comptemanagement.entity.*;
import com.management.comptemanagement.persistance.repository.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Stateless
public class CompteDepotService {

    private static final int STATUT_COMPTE_ACTIF = 1;

    private static final int TYPE_MOUVEMENT_DEPOT = 1;
    private static final int TYPE_MOUVEMENT_RETRAIT = 2;

    @EJB
    private CompteDepotRepository compteDepotRepository;

    @EJB
    private MouvementCompteDepotRepository mouvementCompteDepotRepository;

    @EJB
    private TypeMouvementCompteDepotRepository typeMouvementCompteDepotRepository;

    @EJB
    private ConfigurationCompteDepotRepository configurationCompteDepotRepository;

    @EJB
    private ClientRepository clientRepository;

    /**
     * (2) Calcule le solde brut du compte dépôt à l'instant T
     * @param idClient ID du client
     * @return Solde brut du compte dépôt
     */
    public BigDecimal calculerSoldeBrut(int idClient) {
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Calculer le solde brut en sommant tous les mouvements (hors intérêts)
        List<MouvementCompteDepot> mouvements = mouvementCompteDepotRepository.findByIdCompteDepot(compte.getId());

        BigDecimal soldeBrut = BigDecimal.ZERO;
        for (MouvementCompteDepot mouvement : mouvements) {
            if (mouvement.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_DEPOT) { // Dépôt
                soldeBrut = soldeBrut.add(mouvement.getMontant());
            } else if (mouvement.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_RETRAIT) { // Retrait
                soldeBrut = soldeBrut.subtract(mouvement.getMontant());
            }
            // On ignore les intérêts (type 3) pour le solde brut
        }

        return soldeBrut;
    }

    /**
     * (3) Calcule les intérêts gagnés au sein du compte dépôt jusqu'à l'instant T avec prorata temporis
     * @param idClient ID du client
     * @return Montant des intérêts gagnés
     */
    public BigDecimal calculerInteretsGagnes(int idClient) {
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Récupérer la dernière configuration
        ConfigurationCompteDepot derniereConfig = configurationCompteDepotRepository.findLatestByCompteDepotId(compte.getId());
        if (derniereConfig == null) {
            throw new IllegalStateException("Aucune configuration trouvée pour le compte dépôt ID: " + compte.getId());
        }

        // Récupérer tous les mouvements triés par date (hors intérêts)
        List<MouvementCompteDepot> mouvements = mouvementCompteDepotRepository.findMouvementsHorsInterets(compte.getId());

        // Ajouter un mouvement fictif pour la date de début (ouverture du compte)
        List<MouvementCompteDepot> tousLesMouvements = new ArrayList<>();
        MouvementCompteDepot mouvementOuverture = new MouvementCompteDepot();
        mouvementOuverture.setDateMouvement(compte.getDateOuverture());
        mouvementOuverture.setMontant(BigDecimal.ZERO);
        tousLesMouvements.add(mouvementOuverture);
        tousLesMouvements.addAll(mouvements);

        // Trier par date
        tousLesMouvements.sort(Comparator.comparing(MouvementCompteDepot::getDateMouvement));

        // Calculer les intérêts avec prorata temporis
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal soldeCourant = BigDecimal.ZERO;

        for (int i = 0; i < tousLesMouvements.size() - 1; i++) {
            MouvementCompteDepot mouvementActuel = tousLesMouvements.get(i);
            MouvementCompteDepot mouvementSuivant = tousLesMouvements.get(i + 1);

            // Mettre à jour le solde courant
            if (mouvementActuel.getIdTypeMouvement() != null) {
                if (mouvementActuel.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_DEPOT) { // Dépôt
                    soldeCourant = soldeCourant.add(mouvementActuel.getMontant());
                } else if (mouvementActuel.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_RETRAIT) { // Retrait
                    soldeCourant = soldeCourant.subtract(mouvementActuel.getMontant());
                }
            }

            // Calculer la durée entre les mouvements en jours
            long jours = ChronoUnit.DAYS.between(
                    mouvementActuel.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate(),
                    mouvementSuivant.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate()
            );

            if (jours > 0 && soldeCourant.compareTo(BigDecimal.ZERO) > 0) {
                // Calculer les intérêts pour cette période
                BigDecimal interetsPeriodes = calculerInteretsPourPeriode(
                        soldeCourant, derniereConfig.getTauxInteretAnnuel(), jours);
                interetsTotaux = interetsTotaux.add(interetsPeriodes);
            }
        }

        // Calculer les intérêts pour la dernière période (dernier mouvement à maintenant)
        MouvementCompteDepot dernierMouvement = tousLesMouvements.get(tousLesMouvements.size() - 1);

        // Mettre à jour le solde courant pour le dernier mouvement
        if (dernierMouvement.getIdTypeMouvement() != null) {
            if (dernierMouvement.getIdTypeMouvement().getId() == 1) { // Dépôt
                soldeCourant = soldeCourant.add(dernierMouvement.getMontant());
            } else if (dernierMouvement.getIdTypeMouvement().getId() == 2) { // Retrait
                soldeCourant = soldeCourant.subtract(dernierMouvement.getMontant());
            }
        }

        long joursFin = ChronoUnit.DAYS.between(
                dernierMouvement.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate(),
                Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        );

        if (joursFin > 0 && soldeCourant.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interetsFin = calculerInteretsPourPeriode(
                    soldeCourant, derniereConfig.getTauxInteretAnnuel(), joursFin);
            interetsTotaux = interetsTotaux.add(interetsFin);
        }

        return interetsTotaux.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcule les intérêts pour une période donnée
     */
    private BigDecimal calculerInteretsPourPeriode(BigDecimal solde, double tauxAnnuel, long jours) {
        return solde.multiply(BigDecimal.valueOf(tauxAnnuel))
                .multiply(BigDecimal.valueOf(jours))
                .divide(BigDecimal.valueOf(36500), 10, RoundingMode.HALF_UP); // ÷ 365 et ÷ 100 pour le pourcentage
    }

    /**
     * (10) Calcule le solde réel du compte dépôt (solde brut + intérêts) à l'instant T
     * @param idClient ID du client
     * @return Solde réel du compte dépôt
     */
    public BigDecimal calculerSoldeReel(int idClient) {
        BigDecimal soldeBrut = calculerSoldeBrut(idClient);
        BigDecimal interets = calculerInteretsGagnes(idClient);

        return soldeBrut.add(interets);
    }

    /**
     * (11) Crédite le compte dépôt d'un client
     * @param idClient ID du client
     * @param montant Montant à créditer
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    public void crediterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description) {
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        TypeMouvementCompteDepot typeMouvement = typeMouvementCompteDepotRepository.findById(TYPE_MOUVEMENT_DEPOT); // Dépôt

        // Créer le mouvement
        MouvementCompteDepot mouvement = new MouvementCompteDepot();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteDepot(compte);

        mouvementCompteDepotRepository.save(mouvement);
    }

    /**
     * (12) Débite le compte dépôt d'un client
     * @param idClient ID du client
     * @param montant Montant à débiter
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    public void debiterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description) {
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        // Récupérer la dernière configuration
        ConfigurationCompteDepot config = configurationCompteDepotRepository.findLatestByCompteDepotId(compte.getId());
        if (config == null) {
            throw new IllegalStateException("Aucune configuration trouvée pour le compte dépôt");
        }

        // Vérifier le nombre maximal de retraits ce mois-ci
        int retraitsCeMois = mouvementCompteDepotRepository.countRetraitsThisMonth(compte.getId());
        if (retraitsCeMois >= config.getLimiteRetraitMensuel()) {
            throw new IllegalStateException("Limite de retraits mensuels atteinte: " + config.getLimiteRetraitMensuel());
        }

        // Vérifier le pourcentage maximum de retrait par rapport au solde réel
        BigDecimal soldeReel = calculerSoldeReel(idClient);
        BigDecimal pourcentageRetrait = montant.multiply(BigDecimal.valueOf(100))
                .divide(soldeReel, 2, RoundingMode.HALF_UP);

        if (pourcentageRetrait.compareTo(BigDecimal.valueOf(config.getPourcentageMaxRetrait())) > 0) {
            throw new IllegalStateException(String.format(
                    "Le retrait dépasse le pourcentage maximum autorisé: %.2f%% > %.2f%%",
                    pourcentageRetrait.doubleValue(), config.getPourcentageMaxRetrait()
            ));
        }

        // Vérifier que le solde brut est suffisant
        BigDecimal soldeBrut = calculerSoldeBrut(idClient);
        if (soldeBrut.compareTo(montant) < 0) {
            throw new IllegalStateException("Solde brut insuffisant pour effectuer le retrait");
        }

        TypeMouvementCompteDepot typeMouvement = typeMouvementCompteDepotRepository.findById(TYPE_MOUVEMENT_RETRAIT); // Retrait

        // Créer le mouvement
        MouvementCompteDepot mouvement = new MouvementCompteDepot();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteDepot(compte);

        mouvementCompteDepotRepository.save(mouvement);
    }

    /**
     * (15) Historique des mouvements du compte dépôt d'un client
     * @param idClient ID du client
     */
    public List<MouvementCompteDepot> historiqueMouvementCompteDepot(int idClient) {

        // Vérifier que le compte dépôt existe pour le client
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Obtenir les mouvements du compte dépôt
        return mouvementCompteDepotRepository.findByIdCompteDepot(compte.getId());
    }

    /**
     * Récupère le compte dépôt d'un client
     * @param idClient ID du client
     * @return Compte dépôt du client
     */
    public CompteDepot getCompteDepotByClientId(int idClient) {
        CompteDepot compte = compteDepotRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }
        return compte;
    }
}