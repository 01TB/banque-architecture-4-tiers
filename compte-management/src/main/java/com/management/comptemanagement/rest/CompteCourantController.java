package com.management.comptemanagement.rest;

import com.management.comptemanagement.rest.request.MouvementRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.SoldeResponse;
import com.management.comptemanagement.rest.response.SuccessResponse;
import com.management.comptemanagement.service.CompteCourantService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.Instant;

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
}