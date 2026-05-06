package models;

import java.sql.Date;

public class SuiviMedical {

    private int id;
    private int medicamentId;
    private String nomPatient;
    private Date dateSuivi;
    private String observation;
    private String etatPatient;

    public SuiviMedical() {
    }

    public SuiviMedical(int id, int medicamentId, String nomPatient,
                        Date dateSuivi, String observation,
                        String etatPatient) {

        this.id = id;
        this.medicamentId = medicamentId;
        this.nomPatient = nomPatient;
        this.dateSuivi = dateSuivi;
        this.observation = observation;
        this.etatPatient = etatPatient;
    }

    public SuiviMedical(int medicamentId, String nomPatient,
                        Date dateSuivi, String observation,
                        String etatPatient) {

        this.medicamentId = medicamentId;
        this.nomPatient = nomPatient;
        this.dateSuivi = dateSuivi;
        this.observation = observation;
        this.etatPatient = etatPatient;
    }

    public int getId() {
        return id;
    }

    public int getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(int medicamentId) {
        this.medicamentId = medicamentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public Date getDateSuivi() {
        return dateSuivi;
    }

    public void setDateSuivi(Date dateSuivi) {
        this.dateSuivi = dateSuivi;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getEtatPatient() {
        return etatPatient;
    }

    public void setEtatPatient(String etatPatient) {
        this.etatPatient = etatPatient;
    }

    @Override
    public String toString() {
        return "SuiviMedical{" +
                "id=" + id +
                ", medicamentId=" + medicamentId +
                ", nomPatient='" + nomPatient + '\'' +
                ", dateSuivi=" + dateSuivi +
                ", etatPatient='" + etatPatient + '\'' +
                '}';
    }
}
