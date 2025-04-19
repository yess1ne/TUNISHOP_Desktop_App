package entities;

public class Products {
    private int id;
    private String title;
    private int price;
    private String description;
    private String categorie;
    private String location;
    private String image;
    private int quantity;

    private User utilisateur; // 🔁 Remplace utilisateurId par un objet User
     public Products() {
         
     }
    // Constructor avec User
    public Products(int id, String title, int price, String description, String categorie, String location, String image, int quantity, User utilisateur) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.categorie = categorie;
        this.location = location;
        this.image = image;
        this.quantity = quantity;
        this.utilisateur = utilisateur;
    }

    // Constructor sans id (utilisé lors de l’ajout)
    public Products(String title, int price, String description, String categorie, String location, String image, int quantity, User utilisateur) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categorie = categorie;
        this.location = location;
        this.image = image;
        this.quantity = quantity;
        this.utilisateur = utilisateur;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public User getUtilisateur() { return utilisateur; }
    public void setUtilisateur(User utilisateur) { this.utilisateur = utilisateur; }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", utilisateur=" + (utilisateur != null ? utilisateur.getNom() + " " + utilisateur.getPrenom() : "null") +
                '}';
    }
}
