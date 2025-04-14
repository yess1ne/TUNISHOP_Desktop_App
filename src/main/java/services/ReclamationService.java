package services;

import entities.Reclamation;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    Connection cnx = MyDataBase.getInstance().getMyConnection();

    public void ajouterReclamation(Reclamation rec) {
        String query = "INSERT INTO reclamation (id_commande_id, id_produit_id, raison, date, id_user_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, rec.getIdCommande());
            pst.setInt(2, rec.getIdProduit());
            pst.setString(3, rec.getRaison());
            pst.setDate(4, Date.valueOf(rec.getDate()));
            pst.setInt(5, rec.getIdUserId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout réclamation: " + e.getMessage());
        }
    }

    public void modifierRaison(int id, String newRaison) {
        String sql = "UPDATE reclamation SET raison = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, newRaison);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("La réclamation a été mise à jour avec succès !");
            } else {
                System.out.println("Aucune réclamation n'a été trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de mise à jour : " + e.getMessage());
        }
    }

    public void supprimerReclamation(int id) {
        String query = "DELETE FROM reclamation WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réclamation supprimée !");
            } else {
                System.out.println("Aucune réclamation trouvée pour suppression.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur suppression : " + e.getMessage());
        }
    }

    public List<Reclamation> getAll() {
        List<Reclamation> list = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id_commande_id"),
                        rs.getInt("id_produit_id"),
                        rs.getString("raison"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("id_user_id")
                );
                r.setId(rs.getInt("id"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return list;
    }

    // ✅ Nouvelle méthode pour récupérer une réclamation par ID
    public Reclamation getReclamationById(int id) {
        String query = "SELECT * FROM reclamation WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id_commande_id"),
                        rs.getInt("id_produit_id"),
                        rs.getString("raison"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("id_user_id")
                );
                r.setId(rs.getInt("id"));
                return r;
            }
        } catch (SQLException e) {
            System.out.println("Erreur getById : " + e.getMessage());
        }
        return null;
    }
}
