package com.management.comptemanagement.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // Laisser vide pour utiliser l'auto-détection des classes JAX-RS
}
