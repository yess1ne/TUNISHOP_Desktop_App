package services;

import entities.Products;
import entities.User;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IService<Products> {
    private Connection connection;

    public ProductService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Products product) throws SQLException {
        String sql = "INSERT INTO products (title, price, description, categorie, location, image, quantity, utilisateur_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getCategorie());
            preparedStatement.setString(5, product.getLocation());
            preparedStatement.setString(6, product.getImage());
            preparedStatement.setInt(7, product.getQuantity());

            // 🔁 Utilise l’objet User pour récupérer l’ID
            preparedStatement.setInt(8, product.getUtilisateur().getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void modifier(Products product) throws SQLException {
        String sql = "UPDATE products SET title = ?, price = ?, description = ?, categorie = ?, location = ?, image = ?, quantity = ?, utilisateur_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getCategorie());
            preparedStatement.setString(5, product.getLocation());
            preparedStatement.setString(6, product.getImage());
            preparedStatement.setInt(7, product.getQuantity());

            // 🔁 Utilise l’objet User pour récupérer l’ID
            preparedStatement.setInt(8, product.getUtilisateur().getId());

            preparedStatement.setInt(9, product.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Products> afficher() throws SQLException {
        String sql = "SELECT * FROM products";
        List<Products> productsList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                // 🔁 Crée un User à partir de l’ID
                User utilisateur = new User();
                utilisateur.setId(resultSet.getInt("utilisateur_id"));

                Products product = new Products(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getString("categorie"),
                        resultSet.getString("location"),
                        resultSet.getString("image"),
                        resultSet.getInt("quantity"),
                        utilisateur
                );
                productsList.add(product);
            }
        }

        return productsList;
    }

    public Products fetchProduct(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User utilisateur = new User();
                    utilisateur.setId(resultSet.getInt("utilisateur_id"));

                    return new Products(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getInt("price"),
                            resultSet.getString("description"),
                            resultSet.getString("categorie"),
                            resultSet.getString("location"),
                            resultSet.getString("image"),
                            resultSet.getInt("quantity"),
                            utilisateur
                    );
                }
            }
        }

        return null;
    }
}
