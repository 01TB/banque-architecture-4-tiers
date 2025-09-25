System Heterogene : 
Dotnet / Java
4 : Dotnet 2, Java 2 
Java => Java EJB
Java => Dotnet Web service

Server 1 Java  : Gestion Compte Courant => Solde
Server 2 Dotnet/Pret  : Gestion Depot => interet 2% / ANS, retrait: 1 fois par mois, pas + 50 %
Server 3 Dotnet  : Gestion Pret => interet 24% / ANS
Server 4 Java : Centralisateur

	- Dans la gestion du banque, la gestion du compte dépôt d'un client est il correcte dans ce contexte : un interet 2% / ANS et le client n'a droit qu'à un seul retrait par mois et ne peut pas retirer + 50 % dans le compte dépôt?


Tables : 
- Utilisateur
	- numero_utilisateur
	- nom
	- mot_de_passe

- TypeMouvementCompteCourant : 
	- id
	- libele (Retrait / Depot / Retrait long terme / Depot Long Terme / Rembourssement Long Terme/ Pret  Long Terme/ Interer depot long terme/ Interer pret long terme)
	
- TypeMouvementDepotLongTerme : 
	- id
	- libele (Retrait / Depot / Interet )

- TypeMouvementPretLongTerme : 
	- id
	- libele (Pret / Rembourssement / Interet)


- MouvementCompteCourant : 
	- id
	- id_utilisateur
	- id_type_mouvement
	- date_mouvement
	- motif
	- valeur (>= 0)
	
- MouvementDepotLongTerme :
	- id
	- id_utilisateur
	- id_type_mouvement_depot_long_terme
	- date_mouvement
	- motif
	- valeur (>= 0)
	
- MouvementPretLongTerme :
	- id
	- id_utilisateur
	- id_type_mouvement_depot_long_terme
	- date_mouvement
	- motif
	- valeur (>= 0)

- Configuration : 
	- id
	- libele
	- valeur (>= 0)
	- date_modification
	- indication
	- 