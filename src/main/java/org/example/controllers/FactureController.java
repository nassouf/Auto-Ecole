package org.example.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.DashboardDAO;
import org.example.service.FactureDAO;
import org.example.entities.Facture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FactureController {
    @FXML
    public ImageView mainlogo;
    @FXML
    private TableView<Facture> tableFactures;
    @FXML
    private TableColumn<Facture, Integer> colIdFacture;
    @FXML
    private TableColumn<Facture, String> colNumero;
    @FXML
    private TableColumn<Facture, LocalDate> colDateFacture;
    @FXML
    private TableColumn<Facture, String> colCategorie;
    @FXML
    private TableColumn<Facture, String> colDescription;
    @FXML
    private TableColumn<Facture, Float> colMontant;
    @FXML
    private TableColumn<Facture, String> colStatut;
    @FXML
    private TableColumn<Facture, LocalDate> colDatePaiement;
    @FXML
    private TableColumn<Facture, Void> colActions;

    @FXML
    private TextField numeroFactureField;
    @FXML
    private DatePicker dateFacturePicker;
    @FXML
    private ChoiceBox<String> categorieField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField montantField;
    @FXML
    private ChoiceBox<String> statutField;
    @FXML
    private DatePicker datePaiementPicker;
    @FXML
    private ListView<String> fileListView;
    @FXML
    private StackPane dropZone;

    @FXML
    private ChoiceBox<String> categorieChoiceBox;
    @FXML
    private ChoiceBox<String> statutChoiceBox;
    @FXML
    private TextField searchField;

    @FXML
    private Label totalPayeLabel;
    @FXML
    private Label totalNonPayeLabel;

    @FXML
    private Button comptabiliteButton;
    @FXML
    private VBox comptabiliteSubMenu;

    private FactureDAO factureDAO;
    private ObservableList<Facture> facturesList;
    private File selectedFile;
    private Facture selectedFacture;


    @FXML
    private void initialize() {
        // Initialize DAO
        factureDAO = new FactureDAO();

        // Initialize Choice Boxes with default values
        categorieChoiceBox.setValue("Toutes catégories");
        statutChoiceBox.setValue("Tous");
        categorieField.setValue("Électricité");
        statutField.setValue("Non payée");

        // Setup table columns
        setupTableColumns();
        loadFactures();

        // Update totals
        updateTotals();
        createUploadDirectory();
        AutoEcoleInfo info = autoEcoleInfoServices.getAutoEcoleInfo(null);
        String logoPath = info.getAutoEcolelogo();
        if (logoPath != null && !logoPath.isEmpty()) {
            try {
                File file = new File(logoPath);
                if (file.exists()) {
                    Image logo = new Image(file.toURI().toString());
                    mainLogo.setImage(logo);
                }
            } catch (Exception e) {
                System.out.println("Error loading logo: " + e.getMessage());
            }
        }
    }

    private void createUploadDirectory() {
        try {
            Path path = Paths.get("src/main/java/org/example/repository/factures");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du répertoire d'upload: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        colIdFacture.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroFacture"));
        colDateFacture.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDatePaiement.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));

        // Format date columns
        colDateFacture.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        colDatePaiement.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        colMontant.setCellFactory(column -> new TableCell<>() {
            private final NumberFormat currencyFormat = NumberFormat.getNumberInstance();

            {
                currencyFormat.setMinimumFractionDigits(2);
                currencyFormat.setMaximumFractionDigits(2);
            }

            @Override
            protected void updateItem(Float amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(amount) + " DT");
                }
            }
        });
        ajouterMenu();

    }
    public void ajouterMenu(){
        tableFactures.setRowFactory(tv -> {
            TableRow<Facture> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem viewItem = new MenuItem("Voir");
            viewItem.setOnAction(event -> {
                Facture facture = row.getItem();
                viewFacture(facture);
            });

            MenuItem editItem = new MenuItem("Modifier");
            editItem.setOnAction(event -> {
                Facture facture = row.getItem();
                editFacture(facture);
            });

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Facture facture = row.getItem();
                deleteFacture(facture);
            });

            contextMenu.getItems().addAll(viewItem, editItem, deleteItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }
    private void loadFactures() {
        List<Facture> factures = factureDAO.getAll();
        facturesList = FXCollections.observableArrayList(factures);
        tableFactures.setItems(facturesList);
    }

    @FXML
    private void filterFactures() {
        String numeroFacture = searchField.getText().trim();
        String categorie = "Toutes catégories".equals(categorieChoiceBox.getValue()) ? "" : categorieChoiceBox.getValue();
        String statut = "Tous".equals(statutChoiceBox.getValue()) ? "" : statutChoiceBox.getValue();

        List<Facture> filteredFactures = factureDAO.rechercherFactures(numeroFacture, categorie, statut);
        facturesList.clear();
        facturesList.addAll(filteredFactures);
    }

    private void updateTotals() {
        Float totalPaye = factureDAO.getTotalFacturesByStatut("Payée");
        Float totalNonPaye = factureDAO.getTotalFacturesByStatut("Non payée");

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        totalPayeLabel.setText(currencyFormat.format(totalPaye) + " DT");
        totalNonPayeLabel.setText(currencyFormat.format(totalNonPaye) + " DT");
    }

    @FXML
    private void handleFileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF ou image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document et images", "*.pdf", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(dropZone.getScene().getWindow());
        if (selectedFile != null) {
            fileListView.getItems().clear();
            fileListView.getItems().add(selectedFile.getName());
        }
    }

    @FXML
    private void handleRemoveFile() {
        int selectedIndex = fileListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            fileListView.getItems().remove(selectedIndex);
            selectedFile = null;
        }
    }

    private String saveFile(File file, int id) {
        try {
            String newFileName = id + "_" + file.getName();
            Path targetPath = Paths.get("src/main/java/org/example/repository/factures" + newFileName);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du fichier: " + e.getMessage());
            return null;
        }
    }

    @FXML
    private void handleAddFacture() {
        if (!validateForm()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs obligatoires correctement.");
            return;
        }

        try {
            Facture facture = new Facture();
            facture.setNumeroFacture(numeroFactureField.getText());
            facture.setDateFacture(dateFacturePicker.getValue());
            facture.setDescription(descriptionField.getText());
            facture.setMontant(Float.parseFloat(montantField.getText().replace(",", ".")));
            facture.setCategorie(categorieField.getValue());
            facture.setStatut(statutField.getValue());
            facture.setDatePaiement("Payée".equals(statutField.getValue()) ? datePaiementPicker.getValue() : null);

            // Save file if selected
            if (selectedFile != null) {
                String filePath = saveFile(selectedFile,facture.getIdFacture());
                facture.setCheminScan(filePath);
            }

            if (selectedFacture == null) {
                // Create new invoice
                boolean success = factureDAO.create(facture);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La facture a été ajoutée avec succès.");
                    resetForm();
                    loadFactures();
                    updateTotals();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de la facture.");
                }
            } else {
                // Update existing invoice
                boolean success = factureDAO.update(selectedFacture.getIdFacture(), facture);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La facture a été mise à jour avec succès.");
                    resetForm();
                    loadFactures();
                    updateTotals();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour de la facture.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Le montant doit être un nombre valide.");
        }
    }

    private boolean validateForm() {
        if (numeroFactureField.getText().trim().isEmpty()) return false;
        if (dateFacturePicker.getValue() == null) return false;
        if (montantField.getText().trim().isEmpty()) return false;
        if (categorieField.getValue() == null) return false;
        if (statutField.getValue() == null) return false;
        if ("Payée".equals(statutField.getValue()) && datePaiementPicker.getValue() == null) return false;

        try {
            Float.parseFloat(montantField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


    @FXML
    private void resetForm() {
        numeroFactureField.clear();
        dateFacturePicker.setValue(LocalDate.now());
        categorieField.setValue("Électricité");
        descriptionField.clear();
        montantField.clear();
        statutField.setValue("Non payée");
        datePaiementPicker.setValue(null);
        fileListView.getItems().clear();
        selectedFile = null;
        selectedFacture = null;
    }


    private void editFacture(Facture facture) {
        selectedFacture = facture;

        numeroFactureField.setText(facture.getNumeroFacture());
        dateFacturePicker.setValue(facture.getDateFacture());
        descriptionField.setText(facture.getDescription());
        montantField.setText(String.valueOf(facture.getMontant()));
        categorieField.setValue(facture.getCategorie());
        statutField.setValue(facture.getStatut());
        datePaiementPicker.setValue(facture.getDatePaiement());

        // Show file in list if exists
        fileListView.getItems().clear();
        if (facture.getCheminScan() != null && !facture.getCheminScan().isEmpty()) {
            File file = new File(facture.getCheminScan());
            if (file.exists()) {
                fileListView.getItems().add(file.getName());
                selectedFile = file;
            }
        }
    }

    private void deleteFacture(Facture facture) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Êtes-vous sûr ?");
        confirmAlert.setContentText("Voulez-vous vraiment supprimer cette facture ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = factureDAO.delete(facture.getIdFacture());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La facture a été supprimée avec succès.");
                loadFactures();
                updateTotals();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de la facture.");
            }
        }
    }

    private void viewFacture(Facture facture) {
        if (facture.getCheminScan() == null || facture.getCheminScan().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun document scanné disponible pour cette facture.");
            return;
        }

        try {
            File file = new File(facture.getCheminScan());
            if (!file.exists()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le fichier scanné n'existe pas ou a été déplacé.");
                return;
            }

            // Open file with default system application
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le fichier: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchToCandidat(ActionEvent event) throws IOException {
        App.setRoot("condidat");
    }

    @FXML
    private void switchToMoniteur(ActionEvent event) throws IOException {
        App.setRoot("moniteur");
    }

    @FXML
    private void switchToIngenenieur(ActionEvent event) throws IOException{
        App.setRoot("ingenieur");
    }

    @FXML
    private void switchToVehicule() throws IOException {
        App.setRoot("vehiculeMenu");
    }

    @FXML
    private void switchToCalendar() throws IOException{
        App.setRoot("calender");
    }





    @FXML
    public void switchToPaiement(ActionEvent event) throws IOException{
        App.setRoot("paiement");
    }

    @FXML
    public void switchToSettings(ActionEvent actionEvent) throws IOException{
        App.setRoot("autoecoleinfo");
    }

    public void switchToDashbord(ActionEvent actionEvent)throws IOException {
        App.setRoot("tableaudebord");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    public void switchToSetting(ActionEvent actionEvent)throws IOException {
        App.setRoot("autoecoleinfo");
    }
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
    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }
    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices();;
}