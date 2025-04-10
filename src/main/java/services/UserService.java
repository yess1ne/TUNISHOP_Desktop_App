package services;

import entities.User;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import midlleware.TokenManager;
import org.mindrot.jbcrypt.BCrypt;

public class UserService implements services.IService<User> {

    private Connection connection;

    public UserService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());

            // Hash the password before saving it
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            preparedStatement.setString(4, hashedPassword);

            preparedStatement.setString(5, user.getRole());
            preparedStatement.executeUpdate();
        }
    }

    public String authentifier(User user) throws SQLException {
        String sql = "SELECT id, email, role, password FROM utilisateur WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");  // Changed from 'idUser' to 'id'
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("role");
                    String storedPasswordHash = resultSet.getString("password");

                    // Debugging logs
                    System.out.println("Stored password hash: " + storedPasswordHash);
                    System.out.println("Provided password: " + user.getPassword());

                    // Compare the provided password with the stored hash
                    if (BCrypt.checkpw(user.getPassword(), storedPasswordHash)) {
                        String token = TokenManager.generateJwtToken(id, email, role);  // Changed from 'idUser' to 'id'
                        TokenManager.saveToken(token);
                        return token;
                    } else {
                        System.out.println("Password mismatch");
                    }
                } else {
                    System.out.println("User not found");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while authenticating user", e);
        }
        return null;
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, role = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    @Override
    public List<User> afficher() throws SQLException {
        String sql = "SELECT * FROM utilisateur";
        List<User> users = new ArrayList<>();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));  // Changed from 'idUser' to 'id'
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));  // Fetching the role here
                users.add(user);
            }
        }
        return users;
    }



    public User fetchUser(int id) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));  // Changed from 'idUser' to 'id'
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));  // Fetching the role here
                    return user;
                }
            }
        }
        return null;
    }

    public void updatePassword(String newPassword, String email) throws SQLException {
        String sql = "UPDATE utilisateur SET password = ? WHERE email = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Hash the password before saving it
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            st.setString(1, hashedPassword);
            st.setString(2, email);
            st.executeUpdate();
        }
    }



}
