package entities;

import java.time.LocalDate;

public class Remboursement {

    private int idRemboursement;
    private int id;
    private double montant;
    private String modeRemboursement;
    private LocalDate date;

    public Remboursement(int id , double montant, String modeRemboursement, LocalDate date) {
        this.id =id;
        this.montant = montant;
        this.modeRemboursement = modeRemboursement;
        this.date = date;
    }

    public Remboursement(int idRemboursement, int id , double montant, String modeRemboursement, LocalDate date) {
        this.idRemboursement = idRemboursement;
        this.id = id;
        this.montant = montant;
        this.modeRemboursement = modeRemboursement;
        this.date = date;
    }

    public int getIdRemboursement() {
        return idRemboursement;
    }

    public void setIdRemboursement(int idRemboursement) {
        this.idRemboursement = idRemboursement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getModeRemboursement() {
        return modeRemboursement;
    }

    public void setModeRemboursement(String modeRemboursement) {
        this.modeRemboursement = modeRemboursement;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
