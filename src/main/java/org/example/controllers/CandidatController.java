package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Candidat;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.CandidatService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CandidatController {

    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices();
    @FXML private TableView<Candidat> tableCandidats;
    @FXML private TableColumn<Candidat, Integer> colCIN;
    @FXML private TableColumn<Candidat, String> colNom;
    @FXML private TableColumn<Candidat, String> colPrenom;
    @FXML private TableColumn<Candidat, String> colDateNaissance;
    @FXML private TableColumn<Candidat, String> colEmail;
    @FXML private TableColumn<Candidat, Integer> colTelephone;
    @FXML private TableColumn<Candidat, String> colAdresse;
    @FXML private TableColumn<Candidat, String> colTypePermis;
    @FXML private TableColumn<Candidat, String> colStatus;
    @FXML public TableColumn <Candidat, Integer> colnbrSCatteint;
    @FXML public TableColumn <Candidat, Integer>  colSCONatteint;
    @FXML public TableColumn <Candidat, Integer> colIdVehicule;
    @FXML public TableColumn <Candidat, Integer> colMontantAPayer;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML public TextField cinField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML public ChoiceBox PermichoiceBox;
    @FXML private ListView<String> fileListView;

    private final List<String> filePaths = new ArrayList<>();
    private boolean isEditMode = false;
    private int currentEditingId = -1;
    private final CandidatService candidatService = new CandidatService();
    private final ObservableList<Candidat> candidatsList = FXCollections.observableArrayList();
    @FXML
    private void switchToVehicule() throws IOException {
        App.setRoot("vehiculeMenu");
    }
    @FXML
    private void switchToCalendar() throws IOException{
        App.setRoot("calender");
    }

    @FXML
    private void handleFileSelection(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner des fichiers scannés");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents PDF et Word", ".pdf", ".docx"),
                new FileChooser.ExtensionFilter("Images", ".jpg", ".png", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tous les fichiers", ".")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                filePaths.add(file.getAbsolutePath());
                fileListView.getItems().add(file.getAbsolutePath());
            }
        }
    }
    @FXML
    private void handleRemoveFile() {
        int selectedIndex = fileListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            filePaths.remove(selectedIndex);
            fileListView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    public void switchToSetting(ActionEvent actionEvent)throws IOException{
        App.setRoot("autoecoleinfo");
    }

    @FXML
    private void handleAddCandidat() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String email = emailField.getText().trim();
        String telephoneText = telephoneField.getText().trim();
        String adresse = adresseField.getText().trim();
        VehiculeType typePermis =VehiculeType.valueOf(PermichoiceBox.getValue().toString().toUpperCase());
        String cin = cinField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null || cin.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires (Nom, Prénom, Date de naissance, CIN) !");
            return;
        }

        int telephone = 0;
        if (!telephoneText.isEmpty()) {
            try {
                telephone = Integer.parseInt(telephoneText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le numéro de téléphone doit être un entier !");
                return;
            }
        }

        int cinnum = 0;
        try {
            cinnum = Integer.parseInt(cin);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro de CIN doit être un entier !");
            return;
        }

        String document = filePaths.isEmpty() ? "" : String.join(",", filePaths);

        if (isEditMode) {
            // Récupérer le candidat existant pour conserver certaines valeurs
            Candidat existingCandidat = candidatService.getById(currentEditingId);

            Candidat candidat = new Candidat(
                    cinnum,
                    nom,
                    prenom,
                    dateNaissance,
                    email,
                    telephone,
                    adresse,
                    typePermis,
                    existingCandidat.getStatus(),
                    document.isEmpty() ? existingCandidat.getRepos() : document,
                    existingCandidat.getNbr_seance_code_atteint(),
                    existingCandidat.getNbr_seance_conduit_atteint(),
                    existingCandidat.getIdVehicule(),
                    existingCandidat.getMontantAPayer()
            );

            boolean updated = candidatService.update(currentEditingId, candidat);
            if (updated) {
                showAlert("Succès", "Le candidat a été mis à jour avec succès !");
                resetForm();
                chargerCandidats();
            } else {
                showAlert("Erreur", "Échec lors de la mise à jour du candidat !");
            }
        } else {
            // Ajouter un nouveau candidat
            Candidat candidat = new Candidat(
                    cinnum,
                    nom,
                    prenom,
                    dateNaissance,
                    email,
                    telephone,
                    adresse,
                    typePermis,
                    "Actif",
                    document,
                    0,
                    0,
                    null,
                    0
            );

            boolean created = candidatService.create(candidat);
            if (created) {
                // Définir le chemin du dossier personnel du candidat
                String folderPath = "documents/candidats/" + cin + "_candidat";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Copier chaque fichier scanné dans le dossier personnel du candidat
                for (String filePath : filePaths) {
                    File sourceFile = new File(filePath);
                    File targetFile = new File(folderPath, sourceFile.getName());
                    try {
                        Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (NoSuchFileException e) {
                        System.err.println("File not found: " + e.getMessage());
                        System.err.println("Source Path: " + filePath);
                        System.err.println("Target Path: " + targetFile.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                showAlert("Succès", "Le candidat a été ajouté avec succès !");
                resetForm();
                chargerCandidats();
            } else {
                showAlert("Erreur", "Échec lors de l'ajout du candidat !");
            }
        }
    }

    @FXML
    private void resetForm() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        cinField.clear();
        cinField.setEditable(true);
        PermichoiceBox.getSelectionModel().clearSelection();
        fileListView.getItems().clear();
        filePaths.clear();
        isEditMode = false;
        currentEditingId = -1;
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void initialize() {
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
        colCIN.setCellValueFactory(new PropertyValueFactory<>("CIN"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colTypePermis.setCellValueFactory(new PropertyValueFactory<>("typePermis"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colnbrSCatteint.setCellValueFactory(new PropertyValueFactory<>("nbr_seance_code_atteint"));
        colSCONatteint.setCellValueFactory(new PropertyValueFactory<>("nbr_seance_conduit_atteint"));
        colMontantAPayer.setCellValueFactory(new PropertyValueFactory<>("montantAPayer"));
        colIdVehicule.setCellValueFactory(new PropertyValueFactory<>("idVehicule"));
        chargerCandidats();
        ajouterMenuContextuel();
        colCIN.setCellFactory(col -> {
            TableCell<Candidat, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.toString());
                        setStyle("-fx-text-fill: #0078d7; -fx-font-weight: bold; -fx-cursor: hand;");
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Candidat candidat = cell.getTableView().getItems().get(cell.getIndex());
                    showCandidateProfile(candidat);
                }
            });

            return cell;
        });


    }
    private void showCandidateProfile(Candidat candidat) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("condidatProfile.fxml"));
            Parent root = loader.load();

            CandidateProfileController controller = loader.getController();
            controller.setCandidatData(candidat);

            Stage stage = new Stage();
            stage.setTitle("Profil de " + candidat.getNom() + " " + candidat.getPrenom());
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private void chargerCandidats() {
        List<Candidat> candidats = candidatService.getAll();

        if (candidats.isEmpty()) {
            System.out.println("Aucun candidat récupéré !");
        } else {
            System.out.println("Nombre de candidats  : " + candidats.size());
            candidats.forEach(c -> System.out.println("Candidat : " + c.getNom() + " " + c.getPrenom()));
        }

        candidatsList.setAll(candidats);
        filterCandidats();
    }
    @FXML private TextField searchField;
    @FXML private ChoiceBox<String> statusChoiceBox;
    private final ObservableList<Candidat> filteredCandidatsList = FXCollections.observableArrayList();
    @FXML
    private void filterCandidats() {
        String searchText = searchField.getText().toLowerCase().trim();
        String status = statusChoiceBox.getValue();

        List<Candidat> filtered = candidatsList.stream()
                .filter(candidat -> {
                    // Filtre par statut
                    if (!"Tout".equals(status)) {
                        if ("Actifs".equals(status) && !"Actif".equals(candidat.getStatus())) {
                            return false;
                        } else if ("Inactifs".equals(status) && !"Inactif".equals(candidat.getStatus())) {
                            return false;
                        } else if ("Non payés".equals(status) && candidat.getMontantAPayer() <= 0) {
                            return false;
                        }
                    }

                    // Filtre par CIN ou contenu texte
                    if (!searchText.isEmpty()) {
                        String cinString = String.valueOf(candidat.getCIN());
                        return cinString.contains(searchText) ||
                                candidat.getNom().toLowerCase().contains(searchText) ||
                                candidat.getPrenom().toLowerCase().contains(searchText);
                    }

                    return true;
                })
                .collect(Collectors.toList());

        filteredCandidatsList.setAll(filtered);
        tableCandidats.setItems(filteredCandidatsList);
    }

    private void ajouterMenuContextuel() {
        tableCandidats.setRowFactory(tv -> {
            TableRow<Candidat> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem modifier = new MenuItem("Modifier");
            modifier.setOnAction(event -> modifierCandidat(row.getItem()));

            MenuItem supprimer = new MenuItem("Supprimer");
            supprimer.setOnAction(event -> supprimerCandidat(row.getItem()));

            MenuItem ouvrirDossier = new MenuItem("Ouvrir Dossier");
            ouvrirDossier.setOnAction(event -> ouvrirDossierCandidat(row.getItem()));
            MenuItem rendreInactif = new MenuItem("Rendre inactif");
            rendreInactif.setOnAction(event -> rendreInactifCandidat(row.getItem()));
            contextMenu.getItems().addAll(modifier, supprimer, ouvrirDossier, rendreInactif);

            row.setOnContextMenuRequested(e -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, e.getScreenX(), e.getScreenY());
                }
            });

            return row;
        });
    }
    private void rendreInactifCandidat(Candidat candidat) {
        if (candidat != null) {
            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Rendre inactif le candidat " + candidat.getNom() + " " + candidat.getPrenom() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Modifier le statut du candidat
                candidat.setStatus("Inactif");
                boolean updated = candidatService.update(candidat.getCIN(), candidat);

                if (updated) {
                    showAlert("Succès", "Le candidat a été rendu inactif avec succès !");
                    chargerCandidats(); // Rafraîchir la liste
                } else {
                    showAlert("Erreur", "Échec lors de la mise à jour du statut du candidat !");
                }
            }
        }
    }
    private void modifierCandidat(Candidat candidat) {
        if (candidat != null) {
            isEditMode = true;
            currentEditingId = candidat.getCIN();

            // Remplir les champs avec les données du candidat
            nomField.setText(candidat.getNom());
            prenomField.setText(candidat.getPrenom());
            dateNaissancePicker.setValue(candidat.getDateNaissance());
            cinField.setText(String.valueOf(candidat.getCIN()));
            cinField.setEditable(false); // Ne pas permettre la modification du CIN
            emailField.setText(candidat.getEmail());
            telephoneField.setText(String.valueOf(candidat.getTelephone()));
            adresseField.setText(candidat.getAdresse());
            PermichoiceBox.setValue(candidat.getTypePermis());

            // Charger les fichiers associés s'ils existent
            fileListView.getItems().clear();
            filePaths.clear();

            if (candidat.getRepos() != null && !candidat.getRepos().isEmpty()) {
                String[] paths = candidat.getRepos().split(",");
                for (String path : paths) {
                    if (!path.trim().isEmpty()) {
                        filePaths.add(path);
                        fileListView.getItems().add(path);
                    }
                }
            }
        }
    }


    private void supprimerCandidat(Candidat candidat) {

        //initialize alert and pops up a confirmation message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("supprimer la Candidat :"+candidat.getCIN()+" ?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get()==ButtonType.OK) {
            alert.close();
            candidatService.delete(candidat.getCIN());
            chargerCandidats();
        }else {
            alert.close();
        }
    }

    private void ouvrirDossierCandidat(Candidat candidat) {
        String dossierPath = "documents/candidats/" + candidat.getCIN()+ "_candidat";
        File dossier = new File(dossierPath);
        if (dossier.exists()) {
            try {
                new ProcessBuilder("explorer", dossier.getAbsolutePath()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le dossier n'existe pas.");
        }
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
    private void switchToMap(ActionEvent event) throws IOException{
        App.setRoot("map");
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
    public void switchToPaiement() throws IOException {
        App.setRoot("paiement");
    }

    @FXML
    public void switchToFactures() throws IOException {
        App.setRoot("factures");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    @FXML
    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }
}