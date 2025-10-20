package com.management.comptemanagement.rest;

import com.management.comptemanagement.rest.response.SessionDataResponse;
import com.management.comptemanagement.service.UtilisateurService;
import com.management.comptemanagement.rest.request.LoginRequest;
import com.management.comptemanagement.rest.response.ErrorResponse;
import com.management.comptemanagement.rest.response.LoginResponse;
import com.management.comptemanagement.rest.response.SuccessResponse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurController {

    @EJB
    private UtilisateurService utilisateurService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // Vérifier que les champs obligatoires sont présents
            if (request.getMatricule() == null || request.getMatricule().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Le matricule est obligatoire"))
                        .build();
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Le mot de passe est obligatoire"))
                        .build();
            }

            // Tenter la connexion
            boolean loginSuccess = utilisateurService.login(request.getMatricule(), request.getPassword());

            if (loginSuccess) {
                return Response.ok(new LoginResponse(
                        "Connexion réussie",
                        utilisateurService.getUtilisateurConnecte()
                )).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Matricule ou mot de passe incorrect"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la connexion: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout() {
        try {
            if (!utilisateurService.isSessionActive()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Aucune session active"))
                        .build();
            }

            utilisateurService.logout();
            return Response.ok(new SuccessResponse("Déconnexion réussie")).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la déconnexion: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/session")
    public Response getSessionInfo() {
        try {
            if (!utilisateurService.isSessionActive()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Aucune session active"))
                        .build();
            }

            return Response.ok(new LoginResponse(
                    "Session active",
                    utilisateurService.getUtilisateurConnecte()
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la récupération de la session: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/permissions")
    public Response getPermissions() {
        try {
            if (!utilisateurService.isSessionActive()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Aucune session active"))
                        .build();
            }

            // Retourner les données de session (directions et actions roles)
            SessionDataResponse sessionData = new SessionDataResponse(
                    utilisateurService.getUtilisateurConnecte(),
                    utilisateurService.getToutesDirections(),
                    utilisateurService.getToutesActionsRoles()
            );

            return Response.ok(sessionData).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erreur lors de la récupération des permissions: " + e.getMessage()))
                    .build();
        }
    }
}