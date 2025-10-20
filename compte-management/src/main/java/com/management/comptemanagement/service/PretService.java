package com.management.comptemanagement.service;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.entity.HistoriqueStatutPret;
import com.management.comptemanagement.entity.Pret;
import com.management.comptemanagement.entity.StatutPret;
import com.management.comptemanagement.persistance.repository.ClientRepository;
import com.management.comptemanagement.persistance.repository.HistoriqueStatutPretRepository;
import com.management.comptemanagement.persistance.repository.PretRepository;
import com.management.comptemanagement.persistance.repository.StatutPretRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.math.BigDecimal;
import java.time.Instant;

@Stateless
public class PretService {

    private static final int TYPE_MOUVEMENT_DEBIT = 2;

    @EJB
    private PretRepository pretRepository;

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private StatutPretRepository statutPretRepository;

    @EJB
    private HistoriqueStatutPretRepository historiqueStatutPretRepository;

    private static int STATUT_PRET_ACTIF = 1;

    /**
     * (5) Calcule la somme des prêts à l'instant T pour un client
     * @param idClient ID du client
     * @return Somme des montants des prêts non fermés
     */
    public BigDecimal calculerSommePrets(int idClient) {
        return pretRepository.findSommePretsByClientId(idClient);
    }

    public Pret creerPret(
            Integer idClient,
             BigDecimal montantPret,
             BigDecimal tauxInteretAnnuel,
             Integer periodiciteRemboursement,
             Instant dateCreation){

        // Récupérer le client
        Client client = clientRepository.findById(idClient);
        if (client == null) {
            throw new IllegalStateException("Client non trouvé dans la base de données");
        }

        // Création de l'entité prêt
        Pret pret = new Pret();
        pret.setIdClient(client);
        pret.setMontantPret(montantPret);
        pret.setTauxInteretAnnuel(tauxInteretAnnuel);
        pret.setPeriodiciteRemboursement(periodiciteRemboursement);
        pret.setDateCreation(dateCreation);
        pret.setDateFermeture(null);

        pret = pretRepository.save(pret);

        // Récupérer le statut prêt
        StatutPret statutActif = statutPretRepository.findById(STATUT_PRET_ACTIF);
        if (statutActif == null) {
            throw new IllegalStateException("Statut 'Actif' non trouvé dans la base de données");
        }

        // Créer le mouvement historique statut pret
        HistoriqueStatutPret historiqueStatutPret = new HistoriqueStatutPret();
        historiqueStatutPret.setIdPret(pret);
        historiqueStatutPret.setIdStatut(statutActif);
        historiqueStatutPret.setDateModification(Instant.now());

        historiqueStatutPret = historiqueStatutPretRepository.save(historiqueStatutPret);

        return pret;
    }
}