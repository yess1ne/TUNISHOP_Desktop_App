package services;

import entities.Remboursement;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RemboursementService {

    Connection cnx = MyDataBase.getInstance().getMyConnection();

    // Méthode pour ajouter un remboursement à la base de données
    public void ajouterRemboursement(Remboursement remboursement) {
        String query = "INSERT INTO remboursement (id , montant, mode_remboursement, date) VALUES (? , ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setDouble(1, remboursement.getId());
            stmt.setDouble(2, remboursement.getMontant());
            stmt.setString(3, remboursement.getModeRemboursement());
            stmt.setDate(4, Date.valueOf(remboursement.getDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout remboursement: " + e.getMessage());
        }
    }


    // Méthode pour modifier un remboursement
    public void modifierRemboursement(int id, double montant, String modeRemboursement) {
        String query = "UPDATE remboursement SET montant = ?, mode_remboursement = ? WHERE id_remboursement = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setDouble(1, montant);
            stmt.setString(2, modeRemboursement);
            stmt.setInt(3, id);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Le remboursement a été mis à jour avec succès !");
            } else {
                System.out.println("Aucun remboursement trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de mise à jour : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un remboursement
    public void supprimerRemboursement(int id) {
        String query = "DELETE FROM remboursement WHERE id_remboursement = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Remboursement supprimé !");
            } else {
                System.out.println("Aucun remboursement trouvé pour suppression.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur suppression : " + e.getMessage());
        }
    }

    // Méthode pour récupérer tous les remboursements
    public List<Remboursement> getAll() {
        List<Remboursement> remboursements = new ArrayList<>();
        String query = "SELECT * FROM remboursement";
        try (PreparedStatement stmt = cnx.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Remboursement remboursement = new Remboursement(
                        rs.getInt("id"),
                        rs.getDouble("montant"),
                        rs.getString("mode_remboursement"),
                        rs.getDate("date").toLocalDate()
                );
                remboursement.setIdRemboursement(rs.getInt("id_remboursement"));
                remboursements.add(remboursement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return remboursements;
    }

    // Méthode pour récupérer un remboursement par ID
    public Remboursement getRemboursementById(int id) {
        Remboursement remboursement = null;
        String query = "SELECT * FROM remboursement WHERE id_remboursement = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                remboursement = new Remboursement(
                        rs.getInt("id"),
                        rs.getDouble("montant"),
                        rs.getString("mode_remboursement"),
                        rs.getDate("date").toLocalDate()
                );
                remboursement.setIdRemboursement(rs.getInt("id_remboursement"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getById : " + e.getMessage());
        }
        return remboursement;
    }
}
