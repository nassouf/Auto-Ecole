package org.example.service.verification;

import javafx.scene.control.Label;
import org.example.entities.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TimeCheckServices {
    public static boolean isMoniteurExamenValable(List<ExamenConduit> examenConduits, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (ExamenConduit examenConduit : examenConduits.stream()
                .filter(sc -> sc.getIdMoniteur().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = examenConduit.getDateExamen();
            LocalDateTime existingEnd = examenConduit.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Cet employé n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }
    public static boolean isMoniteurSeanceValable(List<SceanceConduite> sceancesConduite, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (SceanceConduite sceance : sceancesConduite.stream()
                .filter(sc -> sc.getMoniteurId().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = sceance.getSceanceDate();
            LocalDateTime existingEnd = sceance.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Cet employé n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }
    public static boolean isIngenieurExamenValable(List<ExamenCode> examenCodes, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (ExamenCode examenCode : examenCodes.stream()
                .filter(sc -> sc.getIdIngenieur().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = examenCode.getDateExamen();
            LocalDateTime existingEnd = examenCode.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Cet ingénieur n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }

    public static boolean isIngenieurSeanceValable(List<SceanceCode> sceancesCode, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (SceanceCode sceance : sceancesCode.stream()
                .filter(sc -> sc.getIngenieurId().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = sceance.getSceanceDate();
            LocalDateTime existingEnd = sceance.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Cet ingénieur n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }
    public static boolean isCandidatExamenValable(List<? extends Examen> examens, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (Examen examen : examens.stream()
                .filter(sc -> sc.getIdCandidat().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = examen.getDateExamen();
            LocalDateTime existingEnd = examen.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Ce candidat n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }

    public static boolean isCandidatSeanceValable(List<? extends Sceance> sceances, Integer cin, LocalDateTime date, Duration duration, Label errorLabel) {
        for (Sceance sceance : sceances.stream()
                .filter(sc -> sc.getCondidatId().equals(cin))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = sceance.getSceanceDate();
            LocalDateTime existingEnd = sceance.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Ce candidat n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }

    public static boolean isVehiculeSeanceAvailable(List<SceanceConduite> sceanceConduites, Integer matricule, LocalDateTime date, Duration duration, Label errorLabel) {
        for (SceanceConduite sceance : sceanceConduites.stream()
                .filter(sc -> sc.getMatriculeVehicule().equals(matricule))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = sceance.getSceanceDate();
            LocalDateTime existingEnd = sceance.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Ce véhicule n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }
    public static boolean isVehiculeExamenAvailable(List<ExamenConduit> examenConduits, Integer matricule, LocalDateTime date, Duration duration, Label errorLabel) {
        for (ExamenConduit examenConduit : examenConduits.stream()
                .filter(sc -> sc.getMatriculeVehicule().equals(matricule))
                .collect(Collectors.toList())) {
            LocalDateTime existingStart = examenConduit.getDateExamen();
            LocalDateTime existingEnd = examenConduit.getEndTime();

            // Check if the new séance overlaps with an existing one.
            if (!(date.plus(duration).isBefore(existingStart) || date.isAfter(existingEnd))) {
                errorLabel.setText("Ce véhicule n'est pas disponible à ce temps");
                errorLabel.setVisible(true);
                return false; // Overlap detected
            }
        }
        return true;
    }
    public static boolean isWorkingTime(LocalDateTime dateTime, Duration duration) {
        LocalTime start = dateTime.toLocalTime();
        LocalTime end = start.plus(duration);
        return (!start.isBefore(LocalTime.of(8, 0))) &&
                (!end.isAfter(LocalTime.of(15, 0))) &&
                !dateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
}
