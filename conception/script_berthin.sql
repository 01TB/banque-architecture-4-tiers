CREATE TABLE client(
   id INTEGER,
   matricule VARCHAR(50)  NOT NULL,
   nom VARCHAR(255)  NOT NULL,
   prenom VARCHAR(255)  NOT NULL,
   date_naissance DATE NOT NULL,
   adresse VARCHAR(255)  NOT NULL,
   email VARCHAR(100)  NOT NULL,
   telephone VARCHAR(50)  NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(matricule)
);

CREATE TABLE statut_compte(
   id INTEGER,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE compte_depot(
   id INTEGER,
   numero_compte VARCHAR(50)  NOT NULL,
   solde NUMERIC(15,2)   NOT NULL,
   date_ouverture TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_statut INTEGER NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_client),
   UNIQUE(numero_compte),
   FOREIGN KEY(id_statut) REFERENCES statut_compte(id),
   FOREIGN KEY(id_client) REFERENCES client(id)
);

CREATE TABLE pret(
   id INTEGER,
   montant_pret NUMERIC(15,2)   NOT NULL,
   taux_interet_annuel NUMERIC(15,2)   NOT NULL,
   periodicite_remboursement INTEGER NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_client) REFERENCES client(id)
);

CREATE TABLE statut_pret(
   id INTEGER,
   libelle VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE configuration_compte_depot(
   id INTEGER,
   taux_interet_annuel DOUBLE PRECISION NOT NULL,
   limite_retrait_mensuel INTEGER NOT NULL,
   pourcentage_max_retrait DOUBLE PRECISION NOT NULL,
   date_application TIMESTAMP NOT NULL,
   id_1 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES compte_depot(id)
);

CREATE TABLE type_mouvement_compte_depot(
   id INTEGER,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE remboursement_pret(
   id INTEGER,
   montant_rembourse NUMERIC(15,2)   NOT NULL,
   interet_paye NUMERIC(15,2)   NOT NULL,
   date_paiement NUMERIC(15,2)   NOT NULL,
   id_1 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES pret(id)
);

CREATE TABLE type_mouvement_compte_courant(
   id INTEGER,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE historique_statut_pret(
   id INTEGER,
   date_modification VARCHAR(50) ,
   id_1 INTEGER NOT NULL,
   id_2 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES statut_pret(id),
   FOREIGN KEY(id_2) REFERENCES pret(id)
);

CREATE TABLE compte_courant(
   id INTEGER,
   numero_compte VARCHAR(50)  NOT NULL,
   solde NUMERIC(15,2)   NOT NULL,
   date_ouverture TIMESTAMP NOT NULL,
   date_fermeture TIMESTAMP,
   id_1 INTEGER NOT NULL,
   id_2 INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_2),
   UNIQUE(numero_compte),
   FOREIGN KEY(id_1) REFERENCES statut_compte(id),
   FOREIGN KEY(id_2) REFERENCES client(id)
);

CREATE TABLE mouvement_compte_courant(
   id INTEGER,
   montant NUMERIC(15,2)   NOT NULL,
   description TEXT NOT NULL,
   date_mouvement VARCHAR(50)  NOT NULL,
   id_1 INTEGER NOT NULL,
   id_2 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES type_mouvement_compte_courant(id),
   FOREIGN KEY(id_2) REFERENCES compte_courant(id)
);

CREATE TABLE mouvement_compte_depot(
   id INTEGER,
   montant NUMERIC(15,2)   NOT NULL,
   description TEXT NOT NULL,
   date_mouvement VARCHAR(50)  NOT NULL,
   id_1 INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_1) REFERENCES type_mouvement_compte_depot(id)
);

CREATE TABLE est_historis�_par_2(
   id INTEGER,
   id_compte INTEGER,
   PRIMARY KEY(id, id_compte),
   FOREIGN KEY(id) REFERENCES compte_depot(id),
   FOREIGN KEY(id_compte) REFERENCES mouvement_compte_depot(id)
);
