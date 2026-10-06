package org.example.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.*;
import org.example.service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CandidateProfileController {
    @FXML
    public Button backButton;
    // Informations de base
    @FXML private ImageView candidatPhoto;
    @FXML private Button changePhotoButton;
    @FXML private Label nomCompletLabel;
    @FXML private Label statusLabel;
    @FXML private Label cinLabel;
    @FXML private Label dateNaissanceLabel;
    @FXML private Label typePermisLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label adresseLabel;
    @FXML private Label vehiculeLabel;
    @FXML private Label montantLabel;

    // Séances de code
    @FXML private Label seancesCodeCountLabel;
    @FXML private ProgressBar codeProgressBar;
    @FXML private TableView<SceanceCode> seancesCodeTableView;
    @FXML private TableColumn<SceanceCode, Integer> codeIdColumn;
    @FXML private TableColumn<SceanceCode, String> codeDateColumn;
    @FXML private TableColumn<SceanceCode, String> codeIngenieurColumn;
    @FXML private TableColumn<SceanceCode, Integer> codeIdColumn1; // Pour la durée

    // Séances de conduite
    @FXML private Label seancesConduiteCountLabel;
    @FXML private ProgressBar conduiteProgressBar;
    @FXML private TableView<SceanceConduite> seancesConduiteTableView;
    @FXML private TableColumn<SceanceConduite, Integer> conduiteIdColumn;
    @FXML private TableColumn<SceanceConduite, String> conduiteDateColumn;
    @FXML private TableColumn<SceanceConduite, String> conduiteMoniteurColumn;
    @FXML private TableColumn<SceanceConduite, String> codeIdColumn2; // Pour matriculeVehicule

    // Graphiques
    @FXML private PieChart progressionPieChart;
    @FXML private BarChart<String, Number> seancesBarChart;

    // Paiements
    @FXML private TableView<Paiement> paiementsTableView;
    @FXML private TableColumn<Paiement, Double> montantColumn;
    @FXML private TableColumn<Paiement, String> dateColumn;
    @FXML private TableColumn<Paiement, String> etatColumn;
    @FXML private TableColumn<Paiement, Boolean> faciliteColumn;
    @FXML private TableColumn<Paiement, Integer> trancheColumn;
    @FXML private Label dejaPayeLabel;
    @FXML private Label Totalepayé;

    // Examens Code
    @FXML private TableView<ExamenCode> examensCodeTableView;
    @FXML private TableColumn<ExamenCode, String> codeTypeColumn;
    @FXML private TableColumn<ExamenCode, String> codeDate1Column;
    @FXML private TableColumn<ExamenCode, Double> codePrixColumn;
    @FXML private TableColumn<ExamenCode, Integer> codeIngenieur1Column;
    @FXML private TableColumn<ExamenCode, String> codeResultatColumn;

    // Examens Conduite
    @FXML private TableView<ExamenConduit> examensConduiteTableView;
    @FXML private TableColumn<ExamenConduit, String> conduiteTypeColumn;
    @FXML private TableColumn<ExamenConduit, String> conduiteDate1Column;
    @FXML private TableColumn<ExamenConduit, String> conduiteLieuColumn;
    @FXML private TableColumn<ExamenConduit, Double> conduitePrixColumn;
    @FXML private TableColumn<ExamenConduit, Integer> conduiteMoniteur1Column;
    @FXML private TableColumn<ExamenConduit, String> conduiteResultatColumn;

    // Documents
    @FXML private ListView<String> documentsListView;
    @FXML private Button addDocButton;
    @FXML private Button viewDocButton;
    @FXML private Button deleteDocButton;

    private Candidat candidat;
    private PaiementService paiementService = new PaiementService();
    private SceanceCodeServices seanceCodeService = new SceanceCodeServices();
    private SceanceConduiteServices seanceConduiteService = new SceanceConduiteServices();
    private ExamenService examenService = new ExamenService();

    @FXML
    public void initialize() {
        // Only setup UI elements that don't need data
        candidatPhoto.setOnMouseEntered(e -> changePhotoButton.setVisible(true));
        candidatPhoto.setOnMouseExited(e -> changePhotoButton.setVisible(false));
        changePhotoButton.setOnMouseEntered(e -> changePhotoButton.setVisible(true));
        changePhotoButton.setOnMouseExited(e -> changePhotoButton.setVisible(false));


        setupExamensTableColumns();
        setupPaiementTableColumns();
        setupSeancesTableColumns();
        setupDocumentButtons();
    }
    private void setupExamensTableColumns() {
        codeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
        codeDate1Column.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        codePrixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        codeIngenieur1Column.setCellValueFactory(new PropertyValueFactory<>("idIngenieur"));
        codeResultatColumn.setCellValueFactory(cellData -> {
            Boolean status = cellData.getValue().getStatus();
            return new ReadOnlyObjectWrapper<>(status != null && status ? "Réussi" : "En attente");
        });

        conduiteTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
        conduiteDate1Column.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        conduiteLieuColumn.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        conduitePrixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        conduiteMoniteur1Column.setCellValueFactory(new PropertyValueFactory<>("idMoniteur"));
        conduiteResultatColumn.setCellValueFactory(cellData -> {
            Boolean status = cellData.getValue().getStatus();
            return new ReadOnlyObjectWrapper<>(status != null && status ? "Réussi" : "En attente");
        });
    }

    private void setupPaiementTableColumns() {
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        faciliteColumn.setCellValueFactory(new PropertyValueFactory<>("facilite"));
        trancheColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTranche"));
    }

    private void setupSeancesTableColumns() {
        codeIdColumn.setCellValueFactory(new PropertyValueFactory<>("sceanceId"));
        codeDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = cellData.getValue().getSceanceDate().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        codeIngenieurColumn.setCellValueFactory(cellData -> {
            int engineerId = cellData.getValue().getIngenieurId();
            return new SimpleStringProperty("Ingénieur #" + engineerId);
        });

        conduiteIdColumn.setCellValueFactory(new PropertyValueFactory<>("sceanceId"));
        conduiteDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = cellData.getValue().getSceanceDate().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        conduiteMoniteurColumn.setCellValueFactory(cellData -> {
            int monitorId = cellData.getValue().getMoniteurId();
            return new SimpleStringProperty("Moniteur #" + monitorId);
        });
        codeIdColumn2.setCellValueFactory(cellData -> {
            int matricule = cellData.getValue().getMatriculeVehicule();
            return new SimpleStringProperty(String.valueOf(matricule));
        });
    }

    public void setCandidatData(Candidat candidat) {
        if (candidat == null) {
            throw new IllegalArgumentException("Candidat cannot be null");
        }

        this.candidat = candidat;

        // Update basic information
        nomCompletLabel.setText(candidat.getNom() + " " + candidat.getPrenom());
        cinLabel.setText(String.valueOf(candidat.getCIN()));
        dateNaissanceLabel.setText(candidat.getDateNaissance().toString());
        typePermisLabel.setText(candidat.getTypePermis().toString());
        telephoneLabel.setText(String.valueOf(candidat.getTelephone()));
        emailLabel.setText(candidat.getEmail());
        adresseLabel.setText(candidat.getAdresse());
        vehiculeLabel.setText(candidat.getIdVehicule() != null ? candidat.getIdVehicule().toString() : "Non assigné");
        montantLabel.setText(candidat.getMontantAPayer() + " DT");

        // Update status with style
        statusLabel.setText(candidat.getStatus());
        if ("Actif".equals(candidat.getStatus())) {
            statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 10; -fx-background-radius: 5; -fx-background-color: #27ae60; -fx-text-fill: white;");
        } else {
            statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 10; -fx-background-radius: 5; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        }

        // Load all data that depends on the candidat
        loadCandidatPhoto();
        loadSeancesData();
        loadPaiementData();
        loadExamensData();
        loadDocumentsData();
    }

    private void loadExamensData() {
        if (candidat == null) return;

        ExamenCodeServices examenCodeServices = new ExamenCodeServices();
        ExamenConduiteServices examenConduiteServices = new ExamenConduiteServices();

        List<ExamenCode> examensCode = examenCodeServices.getExamensByCandidat(candidat.getCIN());
        List<ExamenConduit> examensConduite = examenConduiteServices.getExamensByCandidat(candidat.getCIN());

        examensCode = examensCode != null ? examensCode : new ArrayList<>();
        examensConduite = examensConduite != null ? examensConduite : new ArrayList<>();

        examensCodeTableView.setItems(FXCollections.observableArrayList(examensCode));
        examensConduiteTableView.setItems(FXCollections.observableArrayList(examensConduite));
    }


    private void setupDocumentButtons() {
        // Configuration des boutons pour les documents
        addDocButton.setOnAction(e -> handleAddDocument());
        viewDocButton.setOnAction(e -> handleViewDocument());
        deleteDocButton.setOnAction(e -> handleDeleteDocument());

        // Désactiver les boutons view et delete par défaut (jusqu'à ce qu'un document soit sélectionné)
        viewDocButton.setDisable(true);
        deleteDocButton.setDisable(true);

        // Activer les boutons lorsqu'un document est sélectionné
        documentsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean documentSelected = newVal != null;
            viewDocButton.setDisable(!documentSelected);
            deleteDocButton.setDisable(!documentSelected);
        });
    }

    @FXML
    private void handleAddDocument() {
        if (candidat == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"),
                new FileChooser.ExtensionFilter("Documents PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Documents Word", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Créer le dossier du candidat s'il n'existe pas
                String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
                File folder = new File(candidatFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Copier le fichier dans le dossier du candidat
                File destinationFile = new File(candidatFolder, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Rafraîchir la liste des documents
                loadDocumentsData();

                // Message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le document a été ajouté avec succès.");
                alert.showAndWait();

            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de l'ajout du document: " + ex.getMessage());
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleViewDocument() {
        String selectedDocName = documentsListView.getSelectionModel().getSelectedItem();
        if (selectedDocName == null || candidat == null) return;

        try {
            String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
            File documentFile = new File(candidatFolder, selectedDocName);

            if (documentFile.exists()) {
                // Utiliser la classe Desktop pour ouvrir le document avec l'application par défaut
                java.awt.Desktop.getDesktop().open(documentFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le document est introuvable.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir le document: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteDocument() {
        String selectedDocName = documentsListView.getSelectionModel().getSelectedItem();
        if (selectedDocName == null || candidat == null) return;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce document?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
                File documentFile = new File(candidatFolder, selectedDocName);

                if (documentFile.exists() && documentFile.delete()) {
                    // Rafraîchir la liste des documents
                    loadDocumentsData();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Le document a été supprimé avec succès.");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Impossible de supprimer le document.");
                    errorAlert.showAndWait();
                }
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Erreur lors de la suppression du document: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void loadExamensTable() {

            codeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
            codeDate1Column.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
            codePrixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            codeIngenieur1Column.setCellValueFactory(new PropertyValueFactory<>("idIngenieur"));
            codeResultatColumn.setCellValueFactory(cellData -> {
                Boolean status = cellData.getValue().getStatus();
                return new ReadOnlyObjectWrapper<>(status ? "Réussi" : "En attente");
            });

            // Initialize ExamenConduit table columns
            conduiteTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
            conduiteDate1Column.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
            conduiteLieuColumn.setCellValueFactory(new PropertyValueFactory<>("locationName"));
            conduitePrixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            conduiteMoniteur1Column.setCellValueFactory(new PropertyValueFactory<>("idMoniteur"));
            conduiteResultatColumn.setCellValueFactory(cellData -> {
                Boolean status = cellData.getValue().getStatus();
                return new ReadOnlyObjectWrapper<>(status ? "Réussi" : "En attente");
            });

            // Load the exam data
            loadExamensData();

    }

    public void loadPaiement(){
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        faciliteColumn.setCellValueFactory(new PropertyValueFactory<>("facilite"));
        trancheColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTranche"));
    }



    private void loadCandidatPhoto() {
        // Chemin du dossier du candidat
        String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
        File folder = new File(candidatFolder);

        if (folder.exists() && folder.isDirectory()) {
            // Chercher un fichier image dans le dossier (jpg, png, jpeg)
            File[] imageFiles = folder.listFiles((dir, name) -> {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".jpg") ||
                        lowercaseName.endsWith(".png") ||
                        lowercaseName.endsWith(".jpeg");
            });

            // Si une image est trouvée, l'utiliser
            if (imageFiles != null && imageFiles.length > 0) {
                try {
                    String imagePath = imageFiles[0].toURI().toString();
                    Image image = new Image(imagePath);
                    candidatPhoto.setImage(image);
                    return;
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }
        }

        // Si aucune image n'est trouvée ou en cas d'erreur, utiliser l'image par défaut
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/org/example/defaultImage.png"));
            candidatPhoto.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image par défaut: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Copier l'image dans le dossier du candidat
            String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
            File folder = new File(candidatFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Nom du fichier de destination (photo_profil.extension)
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
            File destinationFile = new File(candidatFolder, "photo_profil" + extension);

            try {
                // Copier le fichier
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour l'affichage
                Image newImage = new Image(destinationFile.toURI().toString());
                candidatPhoto.setImage(newImage);

                // Message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("La photo a été modifiée avec succès.");
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la modification de la photo: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void loadSeancesData() {
        try {
            // Récupérer les séances du candidat depuis les services
            List<SceanceCode> seancesCode = seanceCodeService.getSceancesByCandidat(candidat.getCIN());
            List<SceanceConduite> seancesConduite = seanceConduiteService.getSceancesByCandidat(candidat.getCIN());

            // Mettre à jour les tableaux
            seancesCodeTableView.setItems(FXCollections.observableArrayList(seancesCode));
            seancesConduiteTableView.setItems(FXCollections.observableArrayList(seancesConduite));

            // Séances de code
            int codeSessionsCompleted = candidat.getNbr_seance_code_atteint();
            int totalCodeSessions = 10; // Nombre total de séances de code
            seancesCodeCountLabel.setText(codeSessionsCompleted + "/" + totalCodeSessions);
            codeProgressBar.setProgress((double) codeSessionsCompleted / totalCodeSessions);

            // Séances de conduite
            int conduiteSessionsCompleted = candidat.getNbr_seance_conduit_atteint();
            int totalConduiteSessions = 25; // Nombre total de séances de conduite
            seancesConduiteCountLabel.setText(conduiteSessionsCompleted + "/" + totalConduiteSessions);
            conduiteProgressBar.setProgress((double) conduiteSessionsCompleted / totalConduiteSessions);

            // Mise à jour du graphique en camembert
            updateProgressionPieChart(codeSessionsCompleted, totalCodeSessions, conduiteSessionsCompleted, totalConduiteSessions);

            // Mise à jour du graphique à barres avec les données mensuelles
            updateSeancesBarChart(seancesCode, seancesConduite);

        } catch (Exception e) {
            System.out.println("Error loading sessions data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateProgressionPieChart(int codeCompleted, int totalCode, int conduiteCompleted, int totalConduite) {
        // Calcul du pourcentage global
        double codePercentage = (double) codeCompleted / totalCode * 100;
        double conduitePercentage = (double) conduiteCompleted / totalConduite * 100;

        // Créer les données pour le graphique
        PieChart.Data codeData = new PieChart.Data("Code (" + String.format("%.1f", codePercentage) + "%)", codeCompleted);
        PieChart.Data conduiteData = new PieChart.Data("Conduite (" + String.format("%.1f", conduitePercentage) + "%)", conduiteCompleted);
        PieChart.Data remainingData = new PieChart.Data("Restant", (totalCode - codeCompleted) + (totalConduite - conduiteCompleted));

        // Mettre à jour le graphique
        progressionPieChart.getData().clear();
        progressionPieChart.getData().addAll(codeData, conduiteData, remainingData);
    }

    private void updateSeancesBarChart(List<SceanceCode> seancesCode, List<SceanceConduite> seancesConduite) {
        // Préparation des données pour le graphique à barres (séances par mois)
        Map<Month, Integer> codeByMonth = new HashMap<>();
        Map<Month, Integer> conduiteByMonth = new HashMap<>();

        // Compter les séances de code par mois
        for (SceanceCode seance : seancesCode) {
            LocalDate date = LocalDate.from(seance.getSceanceDate());
            Month month = date.getMonth();
            codeByMonth.put(month, codeByMonth.getOrDefault(month, 0) + 1);
        }

        // Compter les séances de conduite par mois
        for (SceanceConduite seance : seancesConduite) {
            LocalDate date =LocalDate.from(seance.getSceanceDate());
            Month month = date.getMonth();
            conduiteByMonth.put(month, conduiteByMonth.getOrDefault(month, 0) + 1);
        }

        // Préparation des séries pour le graphique
        XYChart.Series<String, Number> codeSeries = new XYChart.Series<>();
        codeSeries.setName("Séances de Code");

        XYChart.Series<String, Number> conduiteSeries = new XYChart.Series<>();
        conduiteSeries.setName("Séances de Conduite");

        // Ajouter les données pour chaque mois (les 6 derniers mois)
        LocalDate today = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            Month month = today.minusMonths(i).getMonth();
            String monthName = month.toString();

            // Ajouter les données pour les séances de code
            codeSeries.getData().add(new XYChart.Data<>(monthName, codeByMonth.getOrDefault(month, 0)));

            // Ajouter les données pour les séances de conduite
            conduiteSeries.getData().add(new XYChart.Data<>(monthName, conduiteByMonth.getOrDefault(month, 0)));
        }

        // Mettre à jour le graphique
        seancesBarChart.getData().clear();
        seancesBarChart.getData().addAll(codeSeries, conduiteSeries);
    }



    @FXML private Label documentNameLabel;
    @FXML private Label documentTypeLabel;
    @FXML private Label documentSizeLabel;
    @FXML private ImageView documentPreview;
    private void loadDocumentsData() {
        // Vider la liste actuelle
        if (documentsListView != null) {
            documentsListView.getItems().clear();

            // Charger tous les documents du répertoire du candidat
            String candidatFolder = "documents/candidats/" + candidat.getCIN() + "_candidat";
            File folder = new File(candidatFolder);

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> {
                    // On exclut le fichier de photo de profil
                    return !name.startsWith("photo_profil");
                });

                if (files != null) {
                    for (File file : files) {
                        documentsListView.getItems().add(file.getName());
                    }
                }
            }

            // Message si aucun document
            if (documentsListView.getItems().isEmpty()) {
                documentsListView.setPlaceholder(new Label("Aucun document disponible"));
            }
        }
    }

    private void loadSeancesCodeTable() {
        codeIdColumn.setCellValueFactory(new PropertyValueFactory<>("sceanceId"));
        codeDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = cellData.getValue().getSceanceDate().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        codeIngenieurColumn.setCellValueFactory(cellData -> {
            // Assuming you have a method to get the engineer's name by ID
            int engineerId = cellData.getValue().getIngenieurId();
            // You could replace this with a call to an actual service
            return new SimpleStringProperty("Ingénieur #" + engineerId);
        });
    }

    private void loadSeancesConduiteTable() {
        conduiteIdColumn.setCellValueFactory(new PropertyValueFactory<>("sceanceId"));
        conduiteDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = cellData.getValue().getSceanceDate().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        conduiteMoniteurColumn.setCellValueFactory(cellData -> {
            // Assuming you have a method to get the monitor's name by ID
            int monitorId = cellData.getValue().getMoniteurId();
            // You could replace this with a call to an actual service
            return new SimpleStringProperty("Moniteur #" + monitorId);
        });
        codeIdColumn2.setCellValueFactory(cellData -> {
            int matricule = cellData.getValue().getMatriculeVehicule();
            return new SimpleStringProperty(String.valueOf(matricule));
        });
    }
    private void loadPaiementData() {
        List<Paiement> paiements = paiementService.getPaiementsByCandidat(candidat.getCIN());

        paiementsTableView.setItems(FXCollections.observableArrayList(paiements));
        double totalPaye = paiements.stream()
                .filter(p -> "fait".equals(p.getEtat()))
                .mapToDouble(Paiement::getMontant)
                .sum();

        dejaPayeLabel.setText(totalPaye + " DT");
        double montantTotal = candidat.getMontantAPayer()+totalPaye;
        Totalepayé.setText(montantTotal + " DT");
    }
    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}