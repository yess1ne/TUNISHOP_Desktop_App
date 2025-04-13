package entities;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Blog {
    private StringProperty titre;
    private StringProperty description;
    private IntegerProperty nbLikes;
    private ObjectProperty<LocalDate> dateCreation;
    private StringProperty image;
    private IntegerProperty utilisateur_id_id;

    public Blog(String titre, String description, int nbLikes, LocalDate dateCreation, String image, int utilisateurId) {
        this.titre = new SimpleStringProperty(titre);
        this.description = new SimpleStringProperty(description);
        this.nbLikes = new SimpleIntegerProperty(nbLikes);
        this.dateCreation = new SimpleObjectProperty<>(dateCreation);
        this.image = new SimpleStringProperty(image);
        this.utilisateur_id_id = new SimpleIntegerProperty(utilisateurId);
    }

    public String getTitre() {
        return titre.get();
    }

    public String getDescription() {
        return description.get();
    }

    public int getNbLikes() {
        return nbLikes.get();
    }

    public LocalDate getDateCreation() {
        return dateCreation.get();
    }

    public String getImage() {
        return image.get();
    }

    public int getUtilisateur_id_id() {
        return utilisateur_id_id.get();
    }
}
