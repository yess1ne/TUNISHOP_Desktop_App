package services;

import entities.Checkout;
import entities.User;
import entities.Products;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutService implements IService<Checkout> {

    private Connection connection;

    public CheckoutService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Checkout checkout) throws SQLException {
        String sql = "INSERT INTO checkout (checkout_id, first_name, second_name, email, street, city, postal_code, country, id_user, id_produit, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, checkout.getCheckoutId());
            ps.setString(2, checkout.getFirstName());
            ps.setString(3, checkout.getSecondName());
            ps.setString(4, checkout.getEmail());
            ps.setString(5, checkout.getStreet());
            ps.setString(6, checkout.getCity());
            ps.setString(7, checkout.getPostalCode());
            ps.setString(8, checkout.getCountry());

            // ✅ Utilisation d’un objet User
            ps.setInt(9, checkout.getUser().getId());

            // ✅ Utilisation d’un objet Products (nullable)
            if (checkout.getProduit() != null) {
                ps.setInt(10, checkout.getProduit().getId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            ps.setString(11, checkout.getStatus());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifier(Checkout checkout) throws SQLException {
        String sql = "UPDATE checkout SET checkout_id = ?, first_name = ?, second_name = ?, email = ?, street = ?, city = ?, postal_code = ?, country = ?, id_user = ?, id_produit = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, checkout.getCheckoutId());
            ps.setString(2, checkout.getFirstName());
            ps.setString(3, checkout.getSecondName());
            ps.setString(4, checkout.getEmail());
            ps.setString(5, checkout.getStreet());
            ps.setString(6, checkout.getCity());
            ps.setString(7, checkout.getPostalCode());
            ps.setString(8, checkout.getCountry());

            // ✅ Objet user
            ps.setInt(9, checkout.getUser().getId());

            // ✅ Objet produit (nullable)
            if (checkout.getProduit() != null) {
                ps.setInt(10, checkout.getProduit().getId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            ps.setString(11, checkout.getStatus());
            ps.setInt(12, checkout.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM checkout WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Checkout> afficher() throws SQLException {
        List<Checkout> list = new ArrayList<>();
        String sql = "SELECT * FROM checkout";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id_user"));

                Products produit = null;
                if (rs.getObject("id_produit") != null) {
                    produit = new Products();
                    produit.setId(rs.getInt("id_produit"));
                }

                Checkout c = new Checkout(
                        rs.getInt("id"),
                        rs.getLong("checkout_id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("email"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("postal_code"),
                        rs.getString("country"),
                        user,
                        produit,
                        rs.getString("status")
                );
                list.add(c);
            }
        }
        return list;
    }

    public Checkout fetchById(int id) throws SQLException {
        String sql = "SELECT * FROM checkout WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id_user"));

                    Products produit = null;
                    if (rs.getObject("id_produit") != null) {
                        produit = new Products();
                        produit.setId(rs.getInt("id_produit"));
                    }

                    return new Checkout(
                            rs.getInt("id"),
                            rs.getLong("checkout_id"),
                            rs.getString("first_name"),
                            rs.getString("second_name"),
                            rs.getString("email"),
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("postal_code"),
                            rs.getString("country"),
                            user,
                            produit,
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public List<Checkout> getByUserId(int userId) throws SQLException {
        List<Checkout> list = new ArrayList<>();
        String sql = "SELECT * FROM checkout WHERE id_user = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id_user"));

                    Products produit = null;
                    if (rs.getObject("id_produit") != null) {
                        produit = new Products();
                        produit.setId(rs.getInt("id_produit"));
                    }

                    Checkout c = new Checkout(
                            rs.getInt("id"),
                            rs.getLong("checkout_id"),
                            rs.getString("first_name"),
                            rs.getString("second_name"),
                            rs.getString("email"),
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("postal_code"),
                            rs.getString("country"),
                            user,
                            produit,
                            rs.getString("status")
                    );
                    list.add(c);
                }
            }
        }
        return list;
    }
}
