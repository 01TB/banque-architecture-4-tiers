package com.management.comptemanagement.service;

import com.management.comptemanagement.entity.*;
import com.management.comptemanagement.persistance.repository.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClientService {

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private CompteCourantService compteCourantService;

    @EJB
    private CompteDepotService compteDepotService;

    @EJB
    private PretService pretService;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private CompteDepotRepository compteDepotRepository;

    @EJB
    private StatutCompteRepository statutCompteRepository;

    @EJB
    private ConfigurationCompteDepotRepository configurationCompteDepotRepository;

    /**
     * (13) Crée un nouveau client avec ses comptes associés
     */
    public Client creerClient(String nom, String prenom, LocalDate dateNaissance,
                              String adresse, String email, String telephone) {

        // Générer un matricule unique
        String matricule = "CLI" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Créer le client
        Client client = new Client();
        client.setMatricule(matricule);
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setDateNaissance(dateNaissance);
        client.setAdresse(adresse);
        client.setEmail(email);
        client.setTelephone(telephone);
        client.setDateCreation(Instant.now());

        client = clientRepository.save(client);

        // Récupérer le statut "Actif"
        StatutCompte statutActif = statutCompteRepository.findById(1);
        if (statutActif == null) {
            throw new IllegalStateException("Statut 'Actif' non trouvé dans la base de données");
        }

        // Créer le compte courant
        CompteCourant compteCourant = new CompteCourant();
        compteCourant.setNumeroCompte("CC" + client.getId());
        compteCourant.setSolde(BigDecimal.ZERO);
        compteCourant.setDateOuverture(Instant.now());
        compteCourant.setDateFermeture(null);
        compteCourant.setIdStatut(statutActif);
        compteCourant.setIdClient(client);

        compteCourantRepository.save(compteCourant);

        // Créer le compte dépôt
        CompteDepot compteDepot = new CompteDepot();
        compteDepot.setNumeroCompte("CD" + client.getId());
        compteDepot.setDateOuverture(Instant.now());
        compteDepot.setDateFermeture(null);
        compteDepot.setIdStatut(statutActif);
        compteDepot.setIdClient(client);

        compteDepotRepository.save(compteDepot);

        // Créer une configuration par défaut pour le compte dépôt
        ConfigurationCompteDepot config = new ConfigurationCompteDepot();
        config.setTauxInteretAnnuel(1.5); // 1.5% par défaut
        config.setLimiteRetraitMensuel(3); // 3 retraits par mois par défaut
        config.setPourcentageMaxRetrait(30.0); // 30% du solde par défaut
        config.setDateApplication(Instant.now());
        config.setIdCompteDepot(compteDepot);

        configurationCompteDepotRepository.save(config);

        return client;
    }

    /**
     * (4) Calcule le solde avec intérêts du compte dépôt (alias pour calculerSoldeReel)
     */
    public BigDecimal calculerSoldeAvecInteret(int idClient) {
        return compteDepotService.calculerSoldeReel(idClient);
    }

    /**
     * (8) Calcule le solde total simplifié du client
     * solde compte courant + solde avec intérêts compte dépôt - somme des prêts
     */
    public BigDecimal calculerSoldeTotale(int idClient) {
        // Vérifier que le client existe
        Client client = clientRepository.findById(idClient);
        if (client == null) {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + idClient);
        }

        // (1) Solde brut compte courant
        BigDecimal soldeCourant = compteCourantService.calculerSoldeBrut(idClient);

        // (4) Solde avec intérêts compte dépôt
        BigDecimal soldeDepotAvecInterets = calculerSoldeAvecInteret(idClient);

        // (5) Calculer la somme des prêts non remboursés
        BigDecimal sommePrets = calculerSommePrets(idClient);

        // Calcul du solde total
        return soldeCourant.add(soldeDepotAvecInterets).subtract(sommePrets);
    }

    /**
     * (5) Calcule la somme des prêts non remboursés pour un client
     */
    private BigDecimal calculerSommePrets(int idClient) {
        // Utilisation du PretService injecté
        return pretService.calculerSommePrets(idClient);
    }

    // Dans ClientService.java
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client findClientById(int id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(int id, String nom, String prenom, LocalDate dateNaissance,
                               String adresse, String email, String telephone) {
        Client client = clientRepository.findById(id);
        if (client != null) {
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setDateNaissance(dateNaissance);
            client.setAdresse(adresse);
            client.setEmail(email);
            client.setTelephone(telephone);
            return clientRepository.update(client);
        }
        throw new EntityNotFoundException("Client non trouvé avec l'ID: " + id);
    }

    public void deleteClient(int id) {
        Client client = clientRepository.findById(id);
        if (client != null) {
            clientRepository.delete(id);
        } else {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + id);
        }
    }

    public List<Client> searchClients(String nom, String prenom, String email) {
        // Implémentation de la recherche
        return clientRepository.findByCriteria(nom, prenom, email);
    }
}