package com.management.comptemanagement.rest;

import com.management.comptemanagement.rest.response.ErrorResponse;
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
}