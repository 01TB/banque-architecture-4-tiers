package com.management.comptemanagement.rest;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.entity.Pret;
import com.management.comptemanagement.rest.request.ClientRequest;
import com.management.comptemanagement.rest.request.PretRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.PretResponse;
import com.management.comptemanagement.rest.response.SoldeResponse;
import com.management.comptemanagement.service.PretService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/prets")
@Produces(MediaType.APPLICATION_JSON)
public class PretController {

    @EJB
    private PretService pretService;

    @GET
    @Path("/client/{idClient}/somme")
    public Response calculerSommePrets(@PathParam("idClient") int idClient) {
        try {
            BigDecimal somme = pretService.calculerSommePrets(idClient);
            return Response.ok(new SoldeResponse(somme)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createPret(PretRequest request) {
        try {
            Pret pret = pretService.creerPret(
                    request.getIdClient(),
                    request.getMontantPret(),
                    request.getTauxInteretAnnuel(),
                    request.getPeriodiciteRemboursement(),
                    request.getDateCreation()
            );
            return Response.status(Response.Status.CREATED)
                    .entity(new PretResponse(pret))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }
}