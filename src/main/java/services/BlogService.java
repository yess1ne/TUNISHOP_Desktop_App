package services;

import entities.Blog;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogService {
    private Connection connection;

    public BlogService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    public void ajouter(Blog blog) throws SQLException {
        String sql = "INSERT INTO blog (titre, description, date_creation, image, nb_likes, utilisateur_id_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, blog.getTitre());
            ps.setString(2, blog.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(blog.getDateCreation().atStartOfDay()));
            ps.setString(4, blog.getImage());
            ps.setInt(5, blog.getNbLikes());
            ps.setInt(6, blog.getUtilisateur_id_id());

            ps.executeUpdate();
        }
    }
    public List<Blog> getAllBlogs() throws SQLException {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM blog";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Créer un objet Blog avec les données de la ligne du résultat
                Blog blog = new Blog(
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getInt("nb_likes"),
                        rs.getTimestamp("date_creation").toLocalDateTime().toLocalDate(),
                        rs.getString("image"),
                        rs.getInt("utilisateur_id_id")
                );
                blogs.add(blog);
            }
        }

        return blogs;
    }
    public void deleteBlogByTitre(String titre) throws SQLException {
        String sql = "DELETE FROM blog WHERE titre = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titre);
            ps.executeUpdate();
        }
    }

    public void updateBlogByTitre(String titre, Blog updatedBlog) throws SQLException {
        String sql = "UPDATE blog SET description = ?, date_creation = ?, image = ?, nb_likes = ?, utilisateur_id_id = ? WHERE titre = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, updatedBlog.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(updatedBlog.getDateCreation().atStartOfDay()));
            ps.setString(3, updatedBlog.getImage());
            ps.setInt(4, updatedBlog.getNbLikes());
            ps.setInt(5, updatedBlog.getUtilisateur_id_id());
            ps.setString(6, titre); // ancien titre comme condition

            ps.executeUpdate();
        }
    }

}
