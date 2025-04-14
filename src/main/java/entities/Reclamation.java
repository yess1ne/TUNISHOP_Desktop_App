package entities;

import java.time.LocalDate;

public class Reclamation {

    private int id;
    private int id_commande;
    private int id_produit;
    private String raison;
    private LocalDate date;
    private int id_user_id;  // Ajout de l'attribut id_user_id

    // Constructeur mis à jour pour inclure id_user_id
    public Reclamation(int id_commande, int id_produit, String raison, LocalDate date, int id_user_id) {
        this.id_commande = id_commande;
        this.id_produit = id_produit;
        this.raison = raison;
        this.date = date;
        this.id_user_id = id_user_id;  // Initialisation de l'attribut id_user_id
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCommande() {
        return id_commande;
    }

    public void setIdCommande(int id_commande) {
        this.id_commande = id_commande;
    }

    public int getIdProduit() {
        return id_produit;
    }

    public void setIdProduit(int id_produit) {
        this.id_produit = id_produit;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getIdUserId() {
        return id_user_id;  // Getter pour id_user_id
    }

    public void setIdUserId(int id_user_id) {
        this.id_user_id = id_user_id;  // Setter pour id_user_id
    }
}
