package com.management.comptemanagement.rest.response;

import java.util.List;

public class ClientListResponse {
    private List<ClientResponse> clients;
    private int count;
    private String message;

    public ClientListResponse(List<ClientResponse> clients) {
        this.clients = clients;
        this.count = clients.size();
        this.message = "Opération réussie";
    }

    // Getters et Setters
    public List<ClientResponse> getClients() { return clients; }
    public void setClients(List<ClientResponse> clients) { this.clients = clients; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}