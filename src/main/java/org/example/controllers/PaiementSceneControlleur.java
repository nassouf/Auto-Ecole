package org.example.controllers;


import com.itextpdf.io.exceptions.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Candidat;
import org.example.entities.Paiement;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.CandidatService;
import org.example.service.PaiementService;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaiementSceneControlleur {
    @FXML
    public Button afficherRecuButton;
    @FXML
    private ChoiceBox<Candidat> candidatChoiceBox;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField montantRestantTextField;

    @FXML
    private CheckBox faciliteCheckBox;

    @FXML
    private Spinner<Integer> nombreTranchesSpinner;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private ComboBox<String> etatFilterComboBox;

    @FXML
    private TableView<Paiement> paiementTableView;

    @FXML
    private TableColumn<Paiement, Integer> idColumn;

    @FXML
    private TableColumn<Paiement, Double> montantColumn;

    @FXML
    private TableColumn<Paiement, LocalDate> dateColumn;

    @FXML
    private TableColumn<Paiement, String> etatColumn;

    @FXML
    private TableColumn<Paiement, Boolean> faciliteColumn;

    @FXML
    private TableColumn<Paiement, Integer> trancheColumn;



    @FXML
    private Button validerPaiementButton;

    private PaiementService paiementService;
    private CandidatService candidatService;

    @FXML
    public void initialize() {
        paiementService = new PaiementService();
        candidatService = new CandidatService();
        AutoEcoleInfo info = autoEcoleInfoServices.getAutoEcoleInfo(null);
        String logoPath = info.getAutoEcolelogo();
        if (logoPath != null && !logoPath.isEmpty()) {
            try {
                File file = new File(logoPath);
                if (file.exists()) {
                    javafx.scene.image.Image logo = new Image(file.toURI().toString());
                    mainLogo.setImage(logo);
                }
            } catch (Exception e) {
                System.out.println("Error loading logo: " + e.getMessage());
            }
        }
        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        faciliteColumn.setCellValueFactory(new PropertyValueFactory<>("facilite"));
        trancheColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTranche"));
        List<Paiement> p=paiementService.getAll();
        paiementTableView.getItems().setAll(p);

        candidatChoiceBox.setConverter(new StringConverter<Candidat>() {
            @Override
            public String toString(Candidat candidat) {
                return candidat == null ? "" : candidat.getCIN() + " - " + candidat.getNom() + " " + candidat.getPrenom();
            }

            @Override
            public Candidat fromString(String string) {
                return null; // Non nécessaire pour ce cas d'utilisation
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 3);
        nombreTranchesSpinner.setValueFactory(valueFactory);
        etatFilterComboBox.getItems().addAll("Tous", "En attente", "Fait");
        etatFilterComboBox.setValue("Tous");
        dateDebutPicker.setValue(LocalDate.now().minusMonths(1));
        dateFinPicker.setValue(LocalDate.now());
        montantTextField.textProperty().addListener((observable, oldValue, newValue) -> updateMontantRestant());
        candidatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateMontantRestant();
            refreshPaiements();
        });
        faciliteCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            nombreTranchesSpinner.setDisable(!newValue);
        });

        // Configuration des boutons d'action dans la table
        paiementTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateButtonsState(newValue)
        );
        List<Candidat> candidats = candidatService.getAll();
        candidatChoiceBox.getItems().setAll(candidats);
        nombreTranchesSpinner.setDisable(true);

        validerPaiementButton.setDisable(true);
        afficherRecuButton.setDisable(true);

    }



    private void updateMontantRestant() {
        try {
            Candidat candidat = candidatChoiceBox.getValue();
            if (candidat == null) {
                montantRestantTextField.clear();
                return;
            }

            int montantaPayee = paiementService.getMontantRestant(candidat.getCIN());

            if (!montantTextField.getText().isEmpty()) {
                double montantPaye = Double.parseDouble(montantTextField.getText());
                montantaPayee -= montantPaye;
            }
            montantRestantTextField.setText(String.valueOf(montantaPayee));
        } catch (NumberFormatException e) {
            montantRestantTextField.clear();
        }
    }

    @FXML
    private void ajouterPaiement() {
        try {
            Candidat candidat = candidatChoiceBox.getValue();
            if (candidat == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un candidat.");
                return;
            }

            int candidatId = candidat.getCIN();
            double montant = Double.parseDouble(montantTextField.getText());

            if (montant <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être supérieur à zéro.");
                return;
            }

            if (faciliteCheckBox.isSelected()) {
                int nombreTranches = nombreTranchesSpinner.getValue();
                List<Paiement> tranches = paiementService.creerPaiementsParFacilite(candidatId, montant, nombreTranches);

                if (!tranches.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Plan de facilité créé avec " + nombreTranches + " tranches de " +
                                    String.format("%.2f", montant / nombreTranches));
                    refreshPaiements();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la création du plan de facilité.");
                }
            } else {
                String etat = "fait";
                Paiement paiement = new Paiement(0, candidatId, montant, LocalDate.now(), etat, null, false, 0);

                if (paiementService.create(paiement)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Paiement ajouté avec succès !");
                    refreshPaiements();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du paiement.");
                }
            }
            montantTextField.clear();
            updateMontantRestant();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un montant valide.");
        }
    }

    @FXML
    private void validerPaiement() {
        Paiement selectedPaiement = paiementTableView.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null) return;

        if ("fait".equals(selectedPaiement.getEtat())) {
            showAlert(Alert.AlertType.INFORMATION, "Information", "Ce paiement est déjà validé.");
            return;
        }

        selectedPaiement.setEtat("fait");
        selectedPaiement.setDatePaiement(LocalDate.now());
        if (paiementService.update(selectedPaiement.getId(), selectedPaiement)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Paiement validé avec succès !");
            refreshPaiements();
            updateMontantRestant();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la validation du paiement.");
        }
    }


    @FXML
    private void rechercherPaiements() {
        Map<String, Object> criteres = new HashMap<>();

        Candidat candidat = candidatChoiceBox.getValue();
        if (candidat != null) {
            criteres.put("candidatId", candidat.getCIN());
        }

        String etatFilter = etatFilterComboBox.getValue();
        if (!"Tous".equals(etatFilter)) {
            criteres.put("etat", etatFilter.toLowerCase());
        }

        if (dateDebutPicker.getValue() != null) {
            criteres.put("dateDebut", dateDebutPicker.getValue());
        }

        if (dateFinPicker.getValue() != null) {
            criteres.put("dateFin", dateFinPicker.getValue());
        }

        List<Paiement> resultats = paiementService.rechercherPaiements(criteres);
        paiementTableView.getItems().setAll(resultats);
    }

    private void refreshPaiements() {
        Candidat candidat = candidatChoiceBox.getValue();
        if (candidat != null) {
            List<Paiement> paiements = paiementService.getPaiementsByCandidat(candidat.getCIN());
            paiementTableView.getItems().setAll(paiements);
        } else {
            List<Paiement> p=paiementService.getAll();
            paiementTableView.getItems().setAll(p);
        }
    }
    private void updateButtonsState(Paiement selectedPaiement) {
        boolean paiementSelected = selectedPaiement != null;

        afficherRecuButton.setDisable(!paiementSelected);
        boolean canValidate = paiementSelected && "en attente".equals(selectedPaiement.getEtat());
        validerPaiementButton.setDisable(!canValidate);
    }
    private void checkCandidatsSansPaiement() {
        List<Candidat> candidatsSansPaiement = paiementService.getCandidatsSansPaiementRecent();
        if (!candidatsSansPaiement.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder("Les candidats suivants n'ont pas effectué de paiement depuis plus d'un mois :\n\n");

            for (Candidat candidat : candidatsSansPaiement) {
                messageBuilder.append("- ")
                        .append(candidat.getNom())
                        .append(" ")
                        .append(candidat.getPrenom())
                        .append(" (CIN: ")
                        .append(candidat.getCIN())
                        .append(")\n");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Paiements en retard");
            alert.setHeaderText("Attention : Paiements en retard");
            alert.setContentText(messageBuilder.toString());
            alert.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void afficherRecu() {
        Paiement selectedPaiement = paiementTableView.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection", "Veuillez sélectionner un paiement d'abord.");
            return;
        }

        byte[] recuData = paiementService.genererRecuPDF(selectedPaiement.getId());
        if (recuData == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le reçu.");
            return;
        }
        ouvrirPDF(recuData);
    }

    private void ouvrirPDF(byte[] data) {
        try {
            Path tempFile = Files.createTempFile("recu_", ".pdf");
            Files.write(tempFile, data);
            tempFile.toFile().deleteOnExit();

            Desktop.getDesktop().open(tempFile.toFile());
        } catch (IOException | java.io.IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'affichage", "Impossible d'afficher le reçu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToMoniteur(ActionEvent event) throws java.io.IOException {
        App.setRoot("moniteur");
    }
    @FXML
    private void switchToIngenenieur(ActionEvent event) throws java.io.IOException {
        App.setRoot("ingenieur");
    }
    @FXML
    private void switchToMap(ActionEvent event) throws java.io.IOException {
        App.setRoot("map");
    }
    @FXML
    private void switchToCondidat(ActionEvent event) throws java.io.IOException {

        App.setRoot("condidat");
    }

    @FXML
    private VBox comptabiliteSubMenu;

    @FXML
    private Button comptabiliteButton;

    @FXML
    public void toggleComptabiliteMenu() {
        comptabiliteSubMenu.setVisible(!comptabiliteSubMenu.isVisible());
        comptabiliteSubMenu.setManaged(!comptabiliteSubMenu.isManaged());
    }

    @FXML
    public void switchToPaiement() throws java.io.IOException {
        App.setRoot("paiement");
    }

    @FXML
    public void switchToFactures() throws java.io.IOException {
        App.setRoot("factures");
    }

    public void switchToVehicule(ActionEvent actionEvent) throws java.io.IOException {
        App.setRoot("vehiculeMenu");
    }

    public void switchToCalendar(ActionEvent actionEvent) throws java.io.IOException {
        App.setRoot("calender");
    }
    @FXML
    private void switchToHome() throws java.io.IOException {
        App.setRoot("Home");
    }
    @FXML
    public void switchToSetting(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("autoecoleinfo");
    }

    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }
    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices(); ;

}