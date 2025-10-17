package com.management.comptemanagement.rest;

import com.management.comptemanagement.entity.CompteDepot;
import com.management.comptemanagement.entity.MouvementCompteDepot;
import com.management.comptemanagement.rest.request.MouvementRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.HistoriqueMouvementsDepotResponse;
import com.management.comptemanagement.rest.response.SoldeResponse;
import com.management.comptemanagement.rest.response.SuccessResponse;
import com.management.comptemanagement.service.CompteDepotService;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Path("/comptes-depot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteDepotController {

    @EJB
    private CompteDepotService compteDepotService;

    @GET
    @Path("/client/{idClient}/solde-brut")
    public Response calculerSoldeBrut(@PathParam("idClient") int idClient) {
        try {
            BigDecimal solde = compteDepotService.calculerSoldeBrut(idClient);
            return Response.ok(new SoldeResponse(solde)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/client/{idClient}/interets")
    public Response calculerInteretsGagnes(@PathParam("idClient") int idClient) {
        try {
            BigDecimal interets = compteDepotService.calculerInteretsGagnes(idClient);
            return Response.ok(new SoldeResponse(interets)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/client/{idClient}/solde-reel")
    public Response calculerSoldeReel(@PathParam("idClient") int idClient) {
        try {
            BigDecimal solde = compteDepotService.calculerSoldeReel(idClient);
            return Response.ok(new SoldeResponse(solde)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/client/{idClient}/crediter")
    public Response crediterCompteDepot(
            @PathParam("idClient") int idClient,
            MouvementRequest request) {
        try {
            compteDepotService.crediterCompteDepot(
                    idClient,
                    request.getMontant(),
                    request.getDateMouvement() != null ? request.getDateMouvement() : Instant.now(),
                    request.getDescription()
            );
            return Response.ok(new SuccessResponse("Dépôt effectué avec succès")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/client/{idClient}/debiter")
    public Response debiterCompteDepot(
            @PathParam("idClient") int idClient,
            MouvementRequest request) {
        try {
            compteDepotService.debiterCompteDepot(
                    idClient,
                    request.getMontant(),
                    request.getDateMouvement() != null ? request.getDateMouvement() : Instant.now(),
                    request.getDescription()
            );
            return Response.ok(new SuccessResponse("Retrait effectué avec succès")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/client/{idClient}/historique-mouvements")
    public Response getHistoriqueMouvements(@PathParam("idClient") int idClient) {
        try {
            List<MouvementCompteDepot> mouvements = compteDepotService.historiqueMouvementCompteDepot(idClient);

            // Convertir les entités en DTO
            HistoriqueMouvementsDepotResponse response = convertirEnResponse(idClient, mouvements);

            return Response.ok(response).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la récupération de l'historique"))
                    .build();
        }
    }

    private HistoriqueMouvementsDepotResponse convertirEnResponse(int idClient, List<MouvementCompteDepot> mouvements) {
        if (mouvements == null || mouvements.isEmpty()) {
            // Si pas de mouvements, retourner une réponse avec les informations de base
            CompteDepot compte = compteDepotService.getCompteDepotByClientId(idClient);
            if (compte != null) {
                BigDecimal soldeBrut = BigDecimal.ZERO;
                BigDecimal interets = BigDecimal.ZERO;
                BigDecimal soldeReel = BigDecimal.ZERO;

                try {
                    soldeBrut = compteDepotService.calculerSoldeBrut(idClient);
                    interets = compteDepotService.calculerInteretsGagnes(idClient);
                    soldeReel = compteDepotService.calculerSoldeReel(idClient);
                } catch (Exception e) {
                    // Utiliser les valeurs par défaut en cas d'erreur de calcul
                }

                return new HistoriqueMouvementsDepotResponse(
                        idClient,
                        compte.getNumeroCompte(),
                        soldeBrut,
                        soldeReel,
                        interets,
                        List.of()
                );
            }
            return new HistoriqueMouvementsDepotResponse(idClient, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, List.of());
        }

        // Récupérer les informations du compte depuis le premier mouvement
        CompteDepot compte = mouvements.get(0).getIdCompteDepot();

        // Calculer les soldes
        BigDecimal soldeBrut = compteDepotService.calculerSoldeBrut(idClient);
        BigDecimal interets = compteDepotService.calculerInteretsGagnes(idClient);
        BigDecimal soldeReel = compteDepotService.calculerSoldeReel(idClient);

        // Convertir les mouvements en DTO
        List<HistoriqueMouvementsDepotResponse.MouvementDetail> mouvementDetails = mouvements.stream()
                .map(m -> new HistoriqueMouvementsDepotResponse.MouvementDetail(
                        m.getId(),
                        m.getMontant(),
                        m.getDescription(),
                        m.getDateMouvement(),
                        m.getIdTypeMouvement().getLibelle()
                ))
                .collect(Collectors.toList());

        return new HistoriqueMouvementsDepotResponse(
                idClient,
                compte.getNumeroCompte(),
                soldeBrut,
                soldeReel,
                interets,
                mouvementDetails
        );
    }
}