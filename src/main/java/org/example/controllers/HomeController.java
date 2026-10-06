package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import org.example.App;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.scene.control.Separator;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Candidat;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.PaiementService;
import org.example.service.CandidatService;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

    @FXML private Label telephone;
    @FXML private GridPane moduleGrid;
    @FXML private StackPane notificationArea;

    private final AutoEcoleInfoServices infoService = new AutoEcoleInfoServices();
    private final PaiementService paiementService = new PaiementService();
    private final CandidatService candidatService = new CandidatService();


    @FXML private Button notificationButton;
    @FXML private Label notificationCount;
    private Popup notificationPopup;
    private List<Candidat> candidatsEnRetard;

    @FXML
    public void initialize() {
        AutoEcoleInfo auto = infoService.getAutoEcoleInfo(null);
        telephone.setText(auto.getAutoEcolePhone().toString());
        setupModuleHoverEffects();
        checkForLatePayments();
        setupNotificationButton();
    }

    private void setupNotificationButton() {
        // Créer le popup pour les notifications
        notificationPopup = new Popup();
        notificationPopup.setAutoHide(true); // Se ferme quand on clique ailleurs

        notificationButton.setOnAction(e -> showNotificationPopup());
    }

    /**
     * Vérifie les retards de paiement et met à jour l'indicateur de notification
     */
    private void checkForLatePayments() {
        candidatsEnRetard = paiementService.getCandidatsSansPaiementRecent();

        if (candidatsEnRetard.isEmpty()) {
            // Pas de notification
            notificationCount.setVisible(false);
        } else {
            // Afficher le nombre de notifications
            notificationCount.setText(String.valueOf(candidatsEnRetard.size()));
            notificationCount.setVisible(true);
        }
    }

    /**
     * Affiche le popup des notifications
     */
    private void showNotificationPopup() {
        // Vider le contenu actuel du popup
        notificationPopup.getContent().clear();

        // Créer le contenu du popup
        VBox notificationsBox = new VBox(5);
        notificationsBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        notificationsBox.setPrefWidth(300);

        if (candidatsEnRetard.isEmpty()) {
            // Aucun retard de paiement
            Label noNotificationLabel = new Label("Aucune notification de retard de paiement");
            noNotificationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4a90e2;");
            notificationsBox.getChildren().add(noNotificationLabel);
        } else {
            // Titre des notifications
            Label titleLabel = new Label("Retards de paiement (" + candidatsEnRetard.size() + ")");
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
            notificationsBox.getChildren().add(titleLabel);

            // Séparateur
            Separator separator = new Separator();
            separator.setStyle("-fx-padding: 5 0;");
            notificationsBox.getChildren().add(separator);

            // Ajout des notifications
            int displayCount = Math.min(candidatsEnRetard.size(), 5);
            for (int i = 0; i < displayCount; i++) {
                Candidat candidat = candidatsEnRetard.get(i);
                HBox candidatNotification = createCandidatNotification(candidat);
                notificationsBox.getChildren().add(candidatNotification);
            }

            // Bouton "Voir tout" si nécessaire
            if (candidatsEnRetard.size() > 5) {
                Button voirToutButton = new Button("Voir tous les retards (" + candidatsEnRetard.size() + ")");
                voirToutButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5; -fx-margin: 5;");
                voirToutButton.setPrefWidth(Double.MAX_VALUE);
                voirToutButton.setOnAction(e -> {
                    try {
                        notificationPopup.hide();
                        App.setRoot("paiement");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                notificationsBox.getChildren().add(voirToutButton);
            }
        }

        // Ajouter le contenu au popup
        notificationPopup.getContent().add(notificationsBox);

        // Positionner et afficher le popup
        Window window = notificationButton.getScene().getWindow();
        double x = window.getX() + notificationButton.localToScene(0, 0).getX() + notificationButton.getScene().getX();
        double y = window.getY() + notificationButton.localToScene(0, 0).getY() + notificationButton.getScene().getY() + notificationButton.getHeight();

        notificationPopup.show(window, x - notificationsBox.getPrefWidth() + notificationButton.getWidth(), y);
    }
    private void displayLatePayments() {
        List<Candidat> candidatsEnRetard = paiementService.getCandidatsSansPaiementRecent();

        notificationArea.getChildren().clear();

        if (candidatsEnRetard.isEmpty()) {
            // Aucun retard de paiement
            Label noNotificationLabel = new Label("Aucune notification de retard de paiement");
            noNotificationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4a90e2;");
            notificationArea.getChildren().add(noNotificationLabel);
        } else {
            // Créer une VBox pour organiser les notifications
            VBox notificationsBox = new VBox(5);
            notificationsBox.setStyle("-fx-padding: 5;");


            Label titleLabel = new Label("Retards de paiement (" + candidatsEnRetard.size() + ")");
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
            notificationsBox.getChildren().add(titleLabel);


            int displayCount = Math.min(candidatsEnRetard.size(), 5);

            for (int i = 0; i < displayCount; i++) {
                Candidat candidat = candidatsEnRetard.get(i);
                HBox candidatNotification = createCandidatNotification(candidat);
                notificationsBox.getChildren().add(candidatNotification);
            }

            // Ajouter un bouton "Voir tout" si plus de 5 candidats en retard
            if (candidatsEnRetard.size() > 5) {
                Button voirToutButton = new Button("Voir tous les retards (" + candidatsEnRetard.size() + ")");
                voirToutButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-cursor: hand;");
                voirToutButton.setOnAction(e -> {

                    try {
                        App.setRoot("paiement");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                notificationsBox.getChildren().add(voirToutButton);
            }

            // Ajouter le VBox à la zone de notification
            notificationArea.getChildren().add(notificationsBox);
        }
    }

    private HBox createCandidatNotification(Candidat candidat) {
        HBox notificationBox = new HBox(10);
        notificationBox.setStyle("-fx-padding: 5; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: #f8f8f8;");

        // Informations du candidat
        VBox infoBox = new VBox(2);
        Label nameLabel = new Label(candidat.getNom() + " " + candidat.getPrenom());
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label montantLabel = new Label("Montant restant: " + candidat.getMontantAPayer() + " DT");
        montantLabel.setStyle("-fx-text-fill: #e74c3c;");

        infoBox.getChildren().addAll(nameLabel, montantLabel);



        notificationBox.getChildren().addAll(infoBox);

        return notificationBox;
    }




    private void setupModuleHoverEffects() {

        if (moduleGrid != null) {
            for (javafx.scene.Node node : moduleGrid.getChildren()) {
                if (node instanceof VBox) {
                    VBox moduleBox = (VBox) node;

                    moduleBox.setOnMouseEntered(e -> {
                        // Animation à l'entrée de la souris
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), moduleBox);
                        scaleTransition.setToX(1.03);
                        scaleTransition.setToY(1.03);
                        scaleTransition.play();

                        moduleBox.setStyle(moduleBox.getStyle() + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);");
                    });

                    moduleBox.setOnMouseExited(e -> {
                        // Animation à la sortie de la souris
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), moduleBox);
                        scaleTransition.setToX(1.0);
                        scaleTransition.setToY(1.0);
                        scaleTransition.play();

                        moduleBox.setStyle(moduleBox.getStyle().replace("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3)", "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2)"));
                    });
                }
            }
        }
    }

    // Gestionnaires d'événements pour les modules
    @FXML
    private void handleCandidatClick()throws java.io.IOException {
        App.setRoot("condidat");
    }

    @FXML
    private void handleMoniteurClick() throws java.io.IOException {
        App.setRoot("moniteur");
    }

    @FXML
    private void handleExamenClick() throws java.io.IOException {
        App.setRoot("examen");
    }

    @FXML
    private void handleSeanceClick()throws java.io.IOException {
        App.setRoot("calender");
    }

    @FXML
    private void handlePaiementClick()throws java.io.IOException {
        App.setRoot("paiement");
    }

    @FXML
    private void handleVehiculeClick() throws java.io.IOException {
        App.setRoot("vehiculeMenu");
    }

    @FXML
    private void handleSatatiqueClick(MouseEvent event)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }

    @FXML
    private void handleFinancementClick(MouseEvent event)throws java.io.IOException {
        App.setRoot("factures");
    }

    @FXML
    private void handleSettingClick(ActionEvent event) throws java.io.IOException {
        App.setRoot("autoecoleinfo");
    }

    @FXML
    private void handleDexonnexion(ActionEvent event)throws java.io.IOException {

        // Animation de transition avant la déconnexion
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), ((Button) event.getSource()).getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.8);
        fadeOut.setOnFinished(e -> {
            try {
                App.setRoot("startup");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        fadeOut.play();
    }

    // Méthode améliorée pour l'affichage des alertes
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static class SessionData {
        private static Map<String, Map<String, Object>> dataStore = new HashMap<>();

        public static void storeData(String key, Map<String, Object> data) {
            dataStore.put(key, data);
        }

        public static Map<String, Object> getData(String key) {
            return dataStore.get(key);
        }

        public static void clearData(String key) {
            dataStore.remove(key);
        }
    }
}