package com.management.comptemanagement.rest;

import com.management.comptemanagement.entity.MouvementCompteCourant;
import com.management.comptemanagement.rest.request.MouvementRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.MouvementResponse;
import com.management.comptemanagement.rest.response.SoldeResponse;
import com.management.comptemanagement.rest.response.SuccessResponse;
import com.management.comptemanagement.service.CompteCourantService;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Path("/comptes-courants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteCourantController {

    @EJB
    private CompteCourantService compteCourantService;

    @GET
    @Path("/client/{idClient}/solde-brut")
    public Response calculerSoldeBrut(@PathParam("idClient") int idClient) {
        try {
            BigDecimal solde = compteCourantService.calculerSoldeBrut(idClient);
            return Response.ok(new SoldeResponse(solde)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/client/{idClient}/crediter")
    public Response crediterCompteCourant(
            @PathParam("idClient") int idClient,
            MouvementRequest request) {
        try {
            compteCourantService.crediterCompteCourant(
                    idClient,
                    request.getMontant(),
                    request.getDateMouvement() != null ? request.getDateMouvement() : Instant.now(),
                    request.getDescription()
            );
            return Response.ok(new SuccessResponse("Crédit effectué avec succès")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/client/{idClient}/debiter")
    public Response debiterCompteCourant(
            @PathParam("idClient") int idClient,
            MouvementRequest request) {
        try {
            compteCourantService.debiterCompteCourant(
                    idClient,
                    request.getMontant(),
                    request.getDateMouvement() != null ? request.getDateMouvement() : Instant.now(),
                    request.getDescription()
            );
            return Response.ok(new SuccessResponse("Débit effectué avec succès")).build();
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
            List<MouvementCompteCourant> mouvements = compteCourantService.historiqueMouvementCompteCourant(idClient);
            List<MouvementResponse> response = mouvements.stream()
                    .map(m -> new MouvementResponse(
                            m.getId(),
                            m.getMontant(),
                            m.getDescription(),
                            m.getDateMouvement(),
                            m.getIdTypeMouvement().getLibelle()))
                    .collect(Collectors.toList());
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
}