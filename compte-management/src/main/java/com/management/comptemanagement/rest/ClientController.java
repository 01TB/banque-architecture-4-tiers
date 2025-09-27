package com.management.comptemanagement.rest;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.rest.request.ClientRequest;
import com.management.comptemanagement.rest.response.*;
import com.management.comptemanagement.service.ClientService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientController {

    @EJB
    private ClientService clientService;

    // === CRUD BASIQUE ===

    @GET
    public Response getAllClients() {
        try {
            List<Client> clients = clientService.findAllClients();
            List<ClientResponse> responses = clients.stream()
                    .map(ClientResponse::new)
                    .collect(Collectors.toList());
            return Response.ok(new ClientListResponse(responses)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la récupération des clients: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getClientById(@PathParam("id") int id) {
        try {
            Client client = clientService.findClientById(id);
            return Response.ok(new ClientResponse(client)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createClient(ClientRequest request) {
        try {
            Client client = clientService.creerClient(
                    request.getNom(),
                    request.getPrenom(),
                    request.getDateNaissance(),
                    request.getAdresse(),
                    request.getEmail(),
                    request.getTelephone()
            );
            return Response.status(Response.Status.CREATED)
                    .entity(new ClientResponse(client))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateClient(@PathParam("id") int id, ClientRequest request) {
        try {
            Client client = clientService.updateClient(
                    id,
                    request.getNom(),
                    request.getPrenom(),
                    request.getDateNaissance(),
                    request.getAdresse(),
                    request.getEmail(),
                    request.getTelephone()
            );
            return Response.ok(new ClientResponse(client)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteClient(@PathParam("id") int id) {
        try {
            clientService.deleteClient(id);
            return Response.ok(new SuccessResponse("Client supprimé avec succès")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // === FONCTIONNALITÉS MÉTIER ===

    @GET
    @Path("/{idClient}/solde-avec-interet")
    public Response calculerSoldeAvecInteret(@PathParam("idClient") int idClient) {
        try {
            BigDecimal solde = clientService.calculerSoldeAvecInteret(idClient);
            return Response.ok(new SoldeResponse(solde)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{idClient}/solde-totale")
    public Response calculerSoldeTotale(@PathParam("idClient") int idClient) {
        try {
            BigDecimal solde = clientService.calculerSoldeTotale(idClient);
            return Response.ok(new SoldeResponse(solde)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // === FONCTIONNALITÉS DE RECHERCHE ===

    @GET
    @Path("/search")
    public Response searchClients(
            @QueryParam("nom") String nom,
            @QueryParam("prenom") String prenom,
            @QueryParam("email") String email) {
        try {
            List<Client> clients = clientService.searchClients(nom, prenom, email);
            List<ClientResponse> responses = clients.stream()
                    .map(ClientResponse::new)
                    .collect(Collectors.toList());
            return Response.ok(new ClientListResponse(responses)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la recherche: " + e.getMessage()))
                    .build();
        }
    }
}