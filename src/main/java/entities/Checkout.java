package entities;

public class Checkout {
    private int id;
    private long checkoutId;
    private String firstName;
    private String secondName;
    private String email;
    private String street;
    private String city;
    private String postalCode;
    private String country;

    private User user;            // ✅ Objet User au lieu de idUser
    private Products produit;     // ✅ Objet Products au lieu de idProduit

    private String status;

    // ✅ Constructeur avec ID (récupération depuis DB)
    public Checkout(int id, long checkoutId, String firstName, String secondName, String email, String street,
                    String city, String postalCode, String country, User user, Products produit, String status) {
        this.id = id;
        this.checkoutId = checkoutId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
        this.produit = produit;
        this.status = status;
    }

    // ✅ Constructeur sans ID (ajout d’un nouveau checkout)
    public Checkout(long checkoutId, String firstName, String secondName, String email, String street,
                    String city, String postalCode, String country, User user, Products produit, String status) {
        this.checkoutId = checkoutId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
        this.produit = produit;
        this.status = status;
    }

    // ✅ Getters & Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public long getCheckoutId() { return checkoutId; }

    public void setCheckoutId(long checkoutId) { this.checkoutId = checkoutId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }

    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Products getProduit() { return produit; }

    public void setProduit(Products produit) { this.produit = produit; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", checkoutId=" + checkoutId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", user=" + (user != null ? user.getNom() : "null") +
                ", produit=" + (produit != null ? produit.getTitle() : "null") +
                ", status='" + status + '\'' +
                '}';
    }
}
