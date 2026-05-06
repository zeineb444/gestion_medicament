package org.example;

import models.SuiviMedical;
import service.SuiviMedicalService;

import java.sql.Date;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        SuiviMedicalService sms = new SuiviMedicalService();

        try {

            // AJOUT
            SuiviMedical s = new SuiviMedical(
                    3, // id du médicament existant
                    "Ahmed Ben Ali",
                    Date.valueOf("2025-05-10"),
                    "Le patient va mieux",
                    "AMELIORATION"
            );

            sms.ajouter(s);

            System.out.println("✅ Suivi médical ajouté");

            // AFFICHAGE
            System.out.println("----- Liste des suivis -----");

            for (SuiviMedical suivi : sms.recuperer()) {

                System.out.println(suivi);

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }
}