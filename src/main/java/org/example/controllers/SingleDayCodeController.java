package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.entities.*;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.*;
import org.example.service.verification.TimeCheckServices;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleDayCodeController {
    private static LocalDate selectedDate;
    @FXML
    private TableView<SceanceCode> table;
    @FXML
    private TableColumn<SceanceCode, Integer> cin;
    @FXML
    private TableColumn<SceanceCode, VehiculeType> type;
    @FXML
    private TableColumn<SceanceCode, LocalDateTime> date;
    @FXML
    private TableColumn<SceanceCode, Duration> duree;
    @FXML
    private TableColumn<SceanceCode, Integer> idIngenieur;
    @FXML
    private TableColumn<SceanceCode, Float> price;
    @FXML
    private Label currentDay;
    @FXML
    private ChoiceBox<VehiculeType> typeChoice;
    @FXML
    private ChoiceBox<Candidat> condidatList;
    @FXML
    private ChoiceBox<Employee> ingenieurList ;
    @FXML
    private Label errorType;
    @FXML
    private Label errorTime;
    @FXML
    private Label errorDuration;
    @FXML
    private Label errorCandidat;
    @FXML
    private Label errorIngenieur;
    @FXML
    private TextField hrTemp;
    @FXML
    private TextField minTemp;
    @FXML
    private TextField hrDuree;
    @FXML
    private TextField minDuree;
    @FXML
    private Label errorPast;
    @FXML
    private Button button;
    @FXML
    private Button btmodifier;
    @FXML
    private Button btannuler;



    @FXML
    private void initialize() {
        button.setVisible(true);
        btannuler.setVisible(false);
        btmodifier.setVisible(false);
        condidatList.setDisable(true);
        ingenieurList.setDisable(true);

        hideErrors();
        if (selectedDate.isBefore(LocalDate.now())) {
            disableInputs();
            errorPast.setText("vous ne pouvez pas\najouter un événement\ndans le passé!!!");
            errorPast.setVisible(true);
        }
        typeChoice.getItems().addAll(VehiculeType.VOITURE,VehiculeType.CAMION,VehiculeType.MOTO);
        typeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                condidatList.setDisable(false);
                ingenieurList.setDisable(false);
                updateLists(newVal);
            }
        });

        CandidatService candidatService=new CandidatService();
        condidatList.getItems().addAll(candidatService.getAll());
        IngenieurService ingenieurService=new IngenieurService();
        ingenieurList.getItems().addAll(ingenieurService.getAll());
        currentDay.setText(selectedDate.getYear()+" "+selectedDate.getMonth()+" "+selectedDate.getDayOfMonth());
        System.out.println(selectedDate);
        cin.setCellValueFactory(new PropertyValueFactory<SceanceCode, Integer>("CondidatId"));
        type.setCellValueFactory(new PropertyValueFactory<SceanceCode, VehiculeType>("vehiculeType"));
        date.setCellValueFactory(new PropertyValueFactory<SceanceCode, LocalDateTime>("sceanceDate"));
        duree.setCellValueFactory(new PropertyValueFactory<SceanceCode, Duration>("duration"));
        idIngenieur.setCellValueFactory(new PropertyValueFactory<SceanceCode, Integer>("ingenieurId"));
        price.setCellValueFactory(new PropertyValueFactory<SceanceCode, Float>("price"));
        SceanceCodeServices service = new SceanceCodeServices();
        ObservableList<SceanceCode> sceanceCodes= FXCollections.observableArrayList(service.getAll());
        table.setItems(sceanceCodes.stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );

        table.refresh();
    }
    private List<SceanceCode> seancesCodesToday(){
        SceanceCodeServices sceanceCodeServices=new SceanceCodeServices();
        return sceanceCodeServices.getAll().stream()
                .filter(sc->sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }
    private List<ExamenCode> examenCodesToday(){
        ExamenCodeServices examenCodeServices=new ExamenCodeServices();
        return examenCodeServices.getAll().stream()
                .filter(ex->ex.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }
    private void updateLists(VehiculeType selectedType) {
        CandidatService candidatService = new CandidatService();
        IngenieurService ingenieurService = new IngenieurService();

        // Filter and update condidatList
        condidatList.getItems().setAll(
                candidatService.getAll().stream()
                        .filter(c -> c.getTypePermis() == selectedType)
                        .collect(Collectors.toList())
        );

        // Filter and update ingenieurList
        ingenieurList.getItems().setAll(
                ingenieurService.getAll().stream()
                        .filter(i -> i.getVehiculeType() == selectedType)
                        .collect(Collectors.toList())
        );
    }

    @FXML
    private void ajoutSeanceCode(ActionEvent event) {
        hideErrors();
        if (testInputs()){
            SceanceCodeServices sceanceCodeServices=new SceanceCodeServices();
            SceanceCode sceanceCode=new SceanceCode();
            sceanceCode.setCondidatId(condidatList.getValue().getCIN());
            sceanceCode.setVehiculeType(typeChoice.getValue());
            sceanceCode.setIngenieurId(ingenieurList.getValue().getIdMoniteur());
            TarifDAO tarifDAO=new TarifDAO();
            int prix = 0;
            if (typeChoice.getValue()==VehiculeType.CAMION){
                prix=tarifDAO.getTarifs().getCodeSessionPricecamion();
            }else if(typeChoice.getValue()==VehiculeType.MOTO){
                prix=tarifDAO.getTarifs().getCodeSessionPricemoto();
            }else if(typeChoice.getValue()==VehiculeType.VOITURE){
                prix=tarifDAO.getTarifs().getCodeSessionPricevoiture();
            }
            sceanceCode.setPrice((float) prix);
            sceanceCode.setSceanceDate(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
            sceanceCode.setDuration(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
            if (sceanceCodeServices.create(sceanceCode)){
                resetInputs();
                hideErrors();
                ObservableList<SceanceCode> sceanceCodes = table.getItems();
                sceanceCodes.add(sceanceCode);
                table.setItems(sceanceCodes);
                table.refresh();
            }else {
                System.out.println("Erreur ajoutSeanceCode");
            }
        }

    }
    private void resetInputs(){
        hrTemp.setText("");
        minTemp.setText("");
        hrDuree.setText("");
        minDuree.setText("");
        ingenieurList.getItems().clear();
        condidatList.getItems().clear();
    }
    private void disableInputs(){
        hrTemp.setDisable(true);
        minTemp.setDisable(true);
        hrDuree.setDisable(true);
        minDuree.setDisable(true);
        condidatList.setDisable(true);
        ingenieurList.setDisable(true);
        typeChoice.setDisable(true);
        button.setDisable(true);
    }
    //************** the methods for the "right click" menu *******************

    //1)delete
    @FXML
    private void scCodeDeleteOption(ActionEvent event) {
        SceanceCode sceanceCode=table.getSelectionModel().getSelectedItem();
        if(sceanceCode!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer la seance :"+sceanceCode.getSceanceId()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                SceanceCodeServices sceanceCodeServices=new SceanceCodeServices();
                sceanceCodeServices.delete(sceanceCode.getSceanceId());
                table.getItems().remove(sceanceCode);
                table.setItems(table.getItems());
                table.refresh();
            }else {
                alert.close();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Rien est selectionné");
        }
    }
    //2)edit
    @FXML
    private void seanceEditOption(ActionEvent event) {
        button.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        SceanceCode selectedScCode=table.getSelectionModel().getSelectedItem();
        CandidatService candidatService=new CandidatService();
        IngenieurService ingenieurService=new IngenieurService();
        if (selectedScCode != null) {
            typeChoice.setValue(selectedScCode.getVehiculeType());
            hrTemp.setText(String.valueOf(selectedScCode.getSceanceDate().getHour()));
            minTemp.setText(String.valueOf(selectedScCode.getSceanceDate().getMinute()));
            hrDuree.setText(String.valueOf(selectedScCode.getDuration().toMinutes()/60));
            minDuree.setText(String.valueOf(selectedScCode.getDuration().toMinutes()%60));
            condidatList.setValue(candidatService.getById(selectedScCode.getCondidatId()));
            ingenieurList.setValue(ingenieurService.getById(selectedScCode.getIngenieurId()));
        } else {
            System.out.println("Veuillez sélectionner un ingenieur !");
        }
        button.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }
    //************************
    @FXML
    private void annulerEdit(ActionEvent event) {
        button.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        resetInputs();
    }
    @FXML
    public void modifierSeance(ActionEvent event) {
        SceanceCode selectedSeance = table.getSelectionModel().getSelectedItem();
        System.out.println(selectedSeance);
        if(testInputsUpdate(selectedSeance)) {
            try {
                if (selectedSeance == null) {
                    System.out.println("Veuillez d'abord sélectionner seance code !");
                    return;
                }
                TarifDAO tarifDAO=new TarifDAO();
                int prix = 0;
                if (typeChoice.getValue()==VehiculeType.CAMION){
                    prix=tarifDAO.getTarifs().getCodeSessionPricecamion();
                }else if(typeChoice.getValue()==VehiculeType.MOTO){
                    prix=tarifDAO.getTarifs().getCodeSessionPricemoto();
                }else if(typeChoice.getValue()==VehiculeType.VOITURE){
                    prix=tarifDAO.getTarifs().getCodeSessionPricevoiture();
                }
                SceanceCode sceanceCode = new SceanceCode(
                        selectedSeance.getSceanceId(),
                        typeChoice.getValue(),
                        condidatList.getValue().getCIN(),
                        LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())),
                        Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())),
                        (float)prix,
                        ingenieurList.getValue().getIdMoniteur()
                );

                // Mettre à jour dans la base de données
                SceanceCodeServices sceanceCodeServices = new SceanceCodeServices();
                if (sceanceCodeServices.update(selectedSeance.getSceanceId(), sceanceCode)) {
                    loadSeanceTable(); // Rafraîchir la table
                } else {
                    System.out.println("Échec de la modification.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            annulerEdit(event);
            loadSeanceTable();
        }
    }

    private void loadSeanceTable() {
        SceanceCodeServices sceanceCodeServices=new SceanceCodeServices();
        List<SceanceCode> sceanceCodes = sceanceCodeServices.getAll().stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<SceanceCode> data = FXCollections.observableArrayList(sceanceCodes);
        table.setItems(data);
        table.refresh();
    }

    private boolean testInputs() {
        boolean result = true;

        // Check if type is selected.
        if (typeChoice.getSelectionModel().getSelectedItem() == null) {
            result = false;
            errorType.setText("Il faut spécifier le type");
            errorType.setVisible(true);
        }

        // Validate time input: hours and minutes.
        String hrTempStr = hrTemp.getText().trim();
        String minTempStr = minTemp.getText().trim();
        if (!hrTempStr.matches("\\d+") || !minTempStr.matches("\\d+") || Integer.parseInt(hrTempStr)<0 || Integer.parseInt(minTempStr)<0 || Integer.parseInt(hrTempStr)>24 || Integer.parseInt(minTempStr)>=60) {
            result = false;
            errorTime.setText("Temps invalide");
            errorTime.setVisible(true);
        }

        // Validate duration input: hours and minutes.
        String hrDureeStr = hrDuree.getText().trim();
        String minDureeStr = minDuree.getText().trim();
        if (!hrDureeStr.matches("\\d+") || !minDureeStr.matches("\\d+")) {
            result = false;
            errorDuration.setText("Duree invalide");
            errorDuration.setVisible(true);
        }

        // If time and duration are valid, parse them and perform checks.
        try {
            int hour = Integer.parseInt(hrTempStr);
            int minute = Integer.parseInt(minTempStr);
            int durationHours = Integer.parseInt(hrDureeStr);
            int durationMinutes = Integer.parseInt(minDureeStr);

            LocalDateTime sessionStart = LocalDateTime.of(
                    selectedDate.getYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDayOfMonth(),
                    hour,
                    minute
            );
            Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

            // Check if the session is within working time.
            if (!TimeCheckServices.isWorkingTime(sessionStart, sessionDuration)) {
                result = false;
                errorTime.setText("La séance doit être entre 8:00 et 15:00, hors dimanche");
                errorTime.setVisible(true);
            }
        } catch (NumberFormatException e) {
            result = false;
            // Error labels have been set already.
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Ingenieur availability.
        try {
            if (ingenieurList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                if (!TimeCheckServices.isIngenieurSeanceValable(
                        seancesCodesToday(),
                        ingenieurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorIngenieur
                )||!TimeCheckServices.isIngenieurExamenValable(
                        examenCodesToday(),
                        ingenieurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorIngenieur
                )
                ) {
                    result = false;
                }
            } else {
                result = false;
                errorIngenieur.setText("Il faut choisir un ingénieur");
                errorIngenieur.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Candidat availability.
        try {
            if (condidatList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);
                List<? extends Sceance> combinedSceances = Stream.concat(
                        seancesConduitToday().stream(),
                        seancesCodesToday().stream()
                ).collect(Collectors.toList());
                List<? extends Examen> combinedExamens=Stream.concat(
                        examenConduitsToday().stream(),
                        examenCodesToday().stream()
                ).collect(Collectors.toList());

                if (!TimeCheckServices.isCandidatSeanceValable(
                        combinedSceances,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCandidat
                )||!TimeCheckServices.isCandidatExamenValable(
                        combinedExamens,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCandidat
                ) )
                {
                    result = false;
                }
            } else {
                result = false;
                errorCandidat.setText("Il faut choisir un candidat");
                errorCandidat.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        return result;
    }

    private List<SceanceConduite> seancesConduitToday() {
        SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
        return sceanceConduiteServices.getAll().stream()
                .filter(sc->sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }
    private List<ExamenConduit> examenConduitsToday() {
        ExamenConduiteServices examenConduiteServices=new ExamenConduiteServices();
        return examenConduiteServices.getAll().stream()
                .filter(ex->ex.getDateExamen().toLocalDate().equals(selectedDate))
                .collect(Collectors.toList());
    }


    private void hideErrors(){
        errorType.setVisible(false);
        errorTime.setVisible(false);
        errorDuration.setVisible(false);
        errorCandidat.setVisible(false);
        errorIngenieur.setVisible(false);
        errorPast.setVisible(false);
    }
    public static void setSelectedDate(LocalDate selectedDate) {
        SingleDayCodeController.selectedDate = selectedDate;
    }
    public static LocalDate getSelectedDate(){
        return selectedDate;
    }
    @FXML
    private void switchToCalendar(ActionEvent event) {
        try {
            App.setRoot("calender");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToScConduite(ActionEvent event){
        SingleDayConduiteController.setSelectedDate(selectedDate);
        try {
            App.setRoot("sceanceDayMenuConduit");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    public boolean testInputsUpdate(SceanceCode sc) {
        boolean result = true;

        // Check if type is selected.
        if (typeChoice.getSelectionModel().getSelectedItem() == null) {
            result = false;
            errorType.setText("Il faut spécifier le type");
            errorType.setVisible(true);
        }

        // Validate time input: hours and minutes.
        String hrTempStr = hrTemp.getText().trim();
        String minTempStr = minTemp.getText().trim();
        if (!hrTempStr.matches("\\d+") || !minTempStr.matches("\\d+") || Integer.parseInt(hrTempStr)<0 || Integer.parseInt(minTempStr)<0 || Integer.parseInt(hrTempStr)>24 || Integer.parseInt(minTempStr)>=60) {
            result = false;
            errorTime.setText("Temps invalide");
            errorTime.setVisible(true);
        }

        // Validate duration input: hours and minutes.
        String hrDureeStr = hrDuree.getText().trim();
        String minDureeStr = minDuree.getText().trim();
        if (!hrDureeStr.matches("\\d+") || !minDureeStr.matches("\\d+")) {
            result = false;
            errorDuration.setText("Duree invalide");
            errorDuration.setVisible(true);
        }

        // If time and duration are valid, parse them and perform checks.
        try {
            int hour = Integer.parseInt(hrTempStr);
            int minute = Integer.parseInt(minTempStr);
            int durationHours = Integer.parseInt(hrDureeStr);
            int durationMinutes = Integer.parseInt(minDureeStr);

            LocalDateTime sessionStart = LocalDateTime.of(
                    selectedDate.getYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDayOfMonth(),
                    hour,
                    minute
            );
            Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

            // Check if the session is within working time.
            if (!TimeCheckServices.isWorkingTime(sessionStart, sessionDuration)) {
                result = false;
                errorTime.setText("La séance doit être entre 8:00 et 15:00, hors dimanche");
                errorTime.setVisible(true);
            }
        } catch (NumberFormatException e) {
            result = false;
            // Error labels have been set already.
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Ingenieur availability.
        try {
            if (ingenieurList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                // Get all seances except the one being updated
                List<SceanceCode> filteredSceances = seancesCodesToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getSceanceId(), sc.getSceanceId()))
                        .collect(Collectors.toList());
                if (!TimeCheckServices.isIngenieurSeanceValable(
                        filteredSceances,
                        ingenieurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorIngenieur
                )||!TimeCheckServices.isIngenieurExamenValable(
                        examenCodesToday(),
                        ingenieurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorIngenieur
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorIngenieur.setText("Il faut choisir un ingénieur");
                errorIngenieur.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Candidat availability.
        try {
            if (condidatList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                SceanceCodeServices sceanceCodeServices = new SceanceCodeServices();
                SceanceConduiteServices sceanceConduiteServices = new SceanceConduiteServices();

                // Filter out the SceanceCode being updated from code sessions
                List<SceanceCode> filteredCodeSceances = seancesCodesToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getSceanceId(), sc.getSceanceId()))
                        .collect(Collectors.toList());

                // Combine with conduite sessions
                List<? extends Sceance> combinedSceances = Stream.concat(
                        seancesConduitToday().stream(),
                        filteredCodeSceances.stream()
                ).collect(Collectors.toList());
                List<? extends Examen> combinedExamen=Stream.concat(
                        examenConduitsToday().stream(),
                        examenCodesToday().stream()
                ).collect(Collectors.toList());

                if (!TimeCheckServices.isCandidatSeanceValable(
                        combinedSceances,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCandidat
                )||!TimeCheckServices.isCandidatExamenValable(
                        combinedExamen,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCandidat
                )){
                    result = false;
                }
            } else {
                result = false;
                errorCandidat.setText("Il faut choisir un candidat");
                errorCandidat.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        return result;
    }
    @FXML
    private void switchToExamenCode(ActionEvent event){
        ExamenCodeController.setSelectedDate(selectedDate);
        try {
            App.setRoot("examenDayMenuCode");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToExamenConduit(ActionEvent event){
        ExamenConduitController.setSelectedDate(selectedDate);
        try {
            App.setRoot("examenDayMenuConduit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

