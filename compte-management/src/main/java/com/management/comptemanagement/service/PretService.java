package com.management.comptemanagement.service;

import com.management.comptemanagement.persistance.repository.PretRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class PretService {

    private static final int TYPE_MOUVEMENT_DEBIT = 2;

    @EJB
    private PretRepository pretRepository;

    /**
     * (5) Calcule la somme des prêts à l'instant T pour un client
     * @param idClient ID du client
     * @return Somme des montants des prêts non fermés
     */
    public BigDecimal calculerSommePrets(int idClient) {
        return pretRepository.findSommePretsByClientId(idClient);
    }
}