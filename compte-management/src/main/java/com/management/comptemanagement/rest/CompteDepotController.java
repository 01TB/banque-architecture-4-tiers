package com.management.comptemanagement.rest;

import com.management.comptemanagement.rest.request.MouvementRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.SoldeResponse;
import com.management.comptemanagement.rest.response.SuccessResponse;
import com.management.comptemanagement.service.CompteDepotService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.Instant;

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
}