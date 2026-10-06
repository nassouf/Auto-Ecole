package org.example.service.verification;

import javafx.scene.control.Label;
import org.example.entities.myEnums.VehiculeType;

import java.time.LocalDate;
import java.util.Arrays;

public class InputCheckServices {

    public static boolean isValidname(String username, Label errorLabel) {
        if (username == null || username.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return username.matches("[A-Za-z]+");
    }

    public static boolean isValidKilometre(String kilometre, Label errorLabel) {
        if (kilometre == null || kilometre.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        try {
            if (Float.parseFloat(kilometre) < 0) {
                if (errorLabel != null) {
                    errorLabel.setText("Value must not be negative");
                    errorLabel.setVisible(true);
                }
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            if (errorLabel != null) {
                errorLabel.setText("Contains invalid characters");
                errorLabel.setVisible(true);
            }
            return false;
        }
    }

    public static boolean isValidPassword(String password, Label errorLabel) {
        if (password == null || password.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (password.length() < 8) {
            if (errorLabel != null) {
                errorLabel.setText("Password must be at least 8 characters");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            if (errorLabel != null) {
                errorLabel.setText("Password must contain at least one uppercase letter");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (!password.matches(".*[^A-Za-z0-9].*")) {
            if (errorLabel != null) {
                errorLabel.setText("Password must contain at least one special character");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email, Label errorLabel) {
        if (email == null || email.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        // Here we require a Gmail address.
        if (!email.endsWith("@gmail.com")) {
            if (errorLabel != null) {
                errorLabel.setText("Invalid email address");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }

    public static boolean isValidPhoneNumber(String phoneNumber, Label errorLabel) {
        System.out.println(phoneNumber);
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        // Now requires exactly 11 digits.
        if (!phoneNumber.trim().matches("[0-9]{8}")) {
            if (errorLabel != null) {
                errorLabel.setText("Phone number must contain 8 digits");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }

    public static boolean isValidDate(LocalDate date, Label errorLabel) {
        if (date == null || date.equals(LocalDate.now())) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (date.isBefore(LocalDate.now())) {
            if (errorLabel != null) {
                errorLabel.setText("The date must be after the current date");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }

    public static boolean isValidPrice(String price, Label errorLabel) {
        if (price == null || price.trim().isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        try {
            if (Float.parseFloat(price.trim()) < 0) {
                if (errorLabel != null) {
                    errorLabel.setText("Price must be a positive number");
                    errorLabel.setVisible(true);
                }
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            if (errorLabel != null) {
                errorLabel.setText("Contains invalid characters");
                errorLabel.setVisible(true);
            }
            return false;
        }
    }

    public static boolean isValidCIN(String cin, Label errorLabel) {
        if (cin == null || cin.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (cin.length() != 8) {
            if (errorLabel != null) {
                errorLabel.setText("CIN must contain 8 digits");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (!cin.matches("\\d+")) {
            if (errorLabel != null) {
                errorLabel.setText("Invalid CIN");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }

    public static boolean isValidMatricule(String matricule, Label errorLabel) {
        if (matricule == null || matricule.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Field is empty");
                errorLabel.setVisible(true);
            }
            return false;
        }
        if (!matricule.replace("TUN", "").matches("\\d+")) {
            if (errorLabel != null) {
                errorLabel.setText("Invalid matricule");
                errorLabel.setVisible(true);
            }
            return false;
        }
        return true;
    }
}
