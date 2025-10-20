package com.management.comptemanagement.service;

import com.management.comptemanagement.entity.*;
import com.management.comptemanagement.persistance.repository.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Stateless
public class CompteCourantService {

    private static final int STATUT_COMPTE_ACTIF = 1;

    private static final int TYPE_MOUVEMENT_CREDIT = 1;
    private static final int TYPE_MOUVEMENT_DEBIT = 2;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private MouvementCompteCourantRepository mouvementCompteCourantRepository;

    @EJB
    private TypeMouvementCompteCourantRepository typeMouvementCompteCourantRepository;

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private UtilisateurService utilisateurService;

    /**
     * (1) Calcule le solde brut du compte courant à l'instant T
     * @param idClient ID du client
     * @return Solde brut du compte courant
     */
    public BigDecimal calculerSoldeBrut(int idClient) {
        CompteCourant compte = compteCourantRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }
        return compte.getSolde();
    }

    /**
     * (6) Crédite le compte courant d'un client
     * @param idClient ID du client
     * @param montant Montant à créditer
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    public void crediterCompteCourant(int idClient, BigDecimal montant, Instant dateMouvement,
                                      String description) {
        CompteCourant compte = compteCourantRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
            throw new IllegalArgumentException("Role non satisfait de utilisateur");
        }

        TypeMouvementCompteCourant typeMouvement = typeMouvementCompteCourantRepository.findById(TYPE_MOUVEMENT_CREDIT); // Crédit

        // Mettre à jour le solde
        BigDecimal nouveauSolde = compte.getSolde().add(montant);
        compte.setSolde(nouveauSolde);
        compteCourantRepository.update(compte);

        // Créer le mouvement
        MouvementCompteCourant mouvement = new MouvementCompteCourant();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteCourant(compte);

        mouvementCompteCourantRepository.save(mouvement);
    }

    /**
     * (9) Débite le compte courant d'un client
     * @param idClient ID du client
     * @param montant Montant à débiter
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    public void debiterCompteCourant(int idClient, BigDecimal montant, Instant dateMouvement,
                                     String description) {
        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        CompteCourant compte = compteCourantRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Vérifier le solde suffisant
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new IllegalStateException("Solde insuffisant pour effectuer le débit");
        }

        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
            throw new IllegalArgumentException("Role non satisfait de utilisateur");
        }

        TypeMouvementCompteCourant typeMouvement = typeMouvementCompteCourantRepository.findById(TYPE_MOUVEMENT_DEBIT); // Débit

        // Mettre à jour le solde
        BigDecimal nouveauSolde = compte.getSolde().subtract(montant);
        compte.setSolde(nouveauSolde);
        compteCourantRepository.update(compte);

        // Créer le mouvement
        MouvementCompteCourant mouvement = new MouvementCompteCourant();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteCourant(compte);

        mouvementCompteCourantRepository.save(mouvement);
    }

    /**
     * (14) Historique des mouvements du compte courant d'un client
     * @param idClient ID du client
     */
    public List<MouvementCompteCourant> historiqueMouvementCompteCourant(int idClient) {

        // Vérifier que le compte courant existe pour le client
        CompteCourant compte = compteCourantRepository.findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Obtenir les mouvements du compte courant
        return mouvementCompteCourantRepository.findByIdCompteCourant(compte.getId());
    }
}