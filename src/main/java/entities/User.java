package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class User {
    private IntegerProperty id;
    private StringProperty nom;
    private StringProperty prenom;
    private StringProperty email;
    private StringProperty password;
    private StringProperty role;
    private StringProperty verificationToken;

    // Constructors
    public User() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.prenom = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.verificationToken = new SimpleStringProperty();
    }

    public User(int id, String nom, String prenom, String email, String password, String role, String verificationToken) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.verificationToken = new SimpleStringProperty(verificationToken);
    }

    public User(String nom, String prenom, String email, String password, String role) {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.verificationToken = new SimpleStringProperty();
    }

    // Getters and Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty getNomProperty() {
        return nom;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty getPrenomProperty() {
        return prenom;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public StringProperty getRoleProperty() {
        return role;
    }

    public String getVerificationToken() {
        return verificationToken.get();
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken.set(verificationToken);
    }

    public StringProperty getVerificationTokenProperty() {
        return verificationToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom.get() + '\'' +
                ", prenom='" + prenom.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", password='" + password.get() + '\'' +
                ", role='" + role.get() + '\'' +
                ", verificationToken='" + verificationToken.get() + '\'' +
                '}';
    }
}
