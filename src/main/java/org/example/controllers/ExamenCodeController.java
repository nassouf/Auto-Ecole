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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExamenCodeController {
    private static LocalDate selectedDate;
    @FXML
    private RadioButton isSuccess;
    @FXML
    private TableView<ExamenCode> table;
    @FXML
    private TableColumn<ExamenCode, Boolean> status;
    @FXML
    private TableColumn<ExamenCode, Integer> cin;
    @FXML
    private TableColumn<ExamenCode, VehiculeType> type;
    @FXML
    private TableColumn<ExamenCode, LocalDateTime> date;
    @FXML
    private TableColumn<ExamenCode, Duration> duree;
    @FXML
    private TableColumn<ExamenCode, Integer> idIngenieur;
    @FXML
    private TableColumn<ExamenCode, Float> price;
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
        isSuccess.setVisible(false);
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
        cin.setCellValueFactory(new PropertyValueFactory<ExamenCode, Integer>("idCandidat"));
        type.setCellValueFactory(new PropertyValueFactory<ExamenCode, VehiculeType>("vehiculeType"));
        date.setCellValueFactory(new PropertyValueFactory<ExamenCode, LocalDateTime>("dateExamen"));
        duree.setCellValueFactory(new PropertyValueFactory<ExamenCode, Duration>("durationExamen"));
        idIngenieur.setCellValueFactory(new PropertyValueFactory<ExamenCode, Integer>("idIngenieur"));
        price.setCellValueFactory(new PropertyValueFactory<ExamenCode, Float>("prix"));
        status.setCellValueFactory(new PropertyValueFactory<ExamenCode,Boolean>("status"));
        ExamenCodeServices examenCodeServices=new ExamenCodeServices();
        ObservableList<ExamenCode> examenCodes= FXCollections.observableArrayList(examenCodeServices.getAll());
        table.setItems(examenCodes.stream()
                .filter(sc -> sc.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );

        table.refresh();
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
    @FXML
    private void ajoutExamenCode(ActionEvent event) {
        hideErrors();
        //
        if (testInputs()){
            ExamenCodeServices examenCodeServices=new ExamenCodeServices();
            ExamenCode examenCode=new ExamenCode();
            examenCode.setIdCandidat(condidatList.getValue().getCIN());
            examenCode.setVehiculeType(typeChoice.getValue());
            examenCode.setIdIngenieur(ingenieurList.getValue().getIdMoniteur());
            TarifDAO tarifDAO=new TarifDAO();
            int prix = 0;
            if (typeChoice.getValue()==VehiculeType.CAMION){
                prix=tarifDAO.getTarifs().getCodeSessionPricecamion();
            }else if(typeChoice.getValue()==VehiculeType.MOTO){
                prix=tarifDAO.getTarifs().getCodeSessionPricemoto();
            }else if(typeChoice.getValue()==VehiculeType.VOITURE){
                prix=tarifDAO.getTarifs().getCodeSessionPricevoiture();
            }
            examenCode.setPrix((float) prix);
            examenCode.setDateExamen(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
            examenCode.setDurationExamen(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
            examenCode.setStatus(false);

            //***********!!!!!!!!!!
            SceanceCodeServices sceanceCodeServices=new SceanceCodeServices();
            if(sceanceCodeServices.getLastSeanceDateTime(examenCode.getIdCandidat(),3)!=null) {
                if(sceanceCodeServices.getLastSeanceDateTime(examenCode.getIdCandidat(),3).toLocalDate().isBefore(selectedDate)) {
                    if (examenCodeServices.create(examenCode)) {
                        resetInputs();
                        hideErrors();
                        ObservableList<ExamenCode> examenCodes = table.getItems();
                        examenCodes.add(examenCode);
                        table.setItems(examenCodes);
                        table.refresh();
                    } else {
                        System.out.println("Erreur ajoutExamenCode");
                    }
                }else {
                    errorCandidat.setText("le date d'examen pour ce candidat doit\netre au minimum le "+sceanceCodeServices.getLastSeanceDateTime(examenCode.getIdCandidat(),3).toLocalDate());
                    errorCandidat.setVisible(true);
                }
            }else {
                errorCandidat.setText("Cet Candidat n'a pas atteind le\nnbr minimum de seance codes");
                errorCandidat.setVisible(true);
            }
        }

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
    private void hideErrors(){
        errorType.setVisible(false);
        errorTime.setVisible(false);
        errorDuration.setVisible(false);
        errorCandidat.setVisible(false);
        errorIngenieur.setVisible(false);
        errorPast.setVisible(false);
    }
    private void resetInputs(){
        hrTemp.setText("");
        minTemp.setText("");
        hrDuree.setText("");
        minDuree.setText("");
        ingenieurList.setValue(null);
        condidatList.setValue(null);
        typeChoice.setValue(null);
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
    private void examenCodeDeleteOption(ActionEvent event) {
        ExamenCode examenCode=table.getSelectionModel().getSelectedItem();
        if(examenCode!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer examen :"+examenCode.getId()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                ExamenCodeServices examenCodeServices=new ExamenCodeServices();
                examenCodeServices.delete(examenCode.getId());
                table.getItems().remove(examenCode);
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
    private void examenEditOption(ActionEvent event) {
        button.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        isSuccess.setVisible(true);
        ExamenCode examenCode=table.getSelectionModel().getSelectedItem();
        CandidatService candidatService=new CandidatService();
        IngenieurService ingenieurService=new IngenieurService();
        if (examenCode != null) {
            typeChoice.setValue(examenCode.getVehiculeType());
            hrTemp.setText(String.valueOf(examenCode.getDateExamen().getHour()));
            minTemp.setText(String.valueOf(examenCode.getDateExamen().getMinute()));
            hrDuree.setText(String.valueOf(examenCode.getDurationExamen().toMinutes()/60));
            minDuree.setText(String.valueOf(examenCode.getDurationExamen().toMinutes()%60));
            condidatList.setValue(candidatService.getById(examenCode.getIdCandidat()));
            ingenieurList.setValue(ingenieurService.getById(examenCode.getIdIngenieur()));
        } else {
            System.out.println("Veuillez sélectionner un ingenieur !");
        }
        button.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }
    //************************
    private void loadExamenTable() {
        ExamenCodeServices examenCodeServices=new ExamenCodeServices();
        List<ExamenCode> examenCodes = examenCodeServices.getAll().stream()
                .filter(sc -> sc.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<ExamenCode> data = FXCollections.observableArrayList(examenCodes);
        table.setItems(data);
        table.refresh();
    }
    @FXML
    public void modifierExamen(ActionEvent event) {
        ExamenCode examenCode = table.getSelectionModel().getSelectedItem();
        System.out.println(examenCode);
        if(testInputsUpdate(examenCode)) {
            try {
                if (examenCode == null) {
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
                ExamenCode exam=new ExamenCode();
                exam.setId(examenCode.getId());
                exam.setDateExamen(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
                exam.setVehiculeType(typeChoice.getValue());
                exam.setIdIngenieur(ingenieurList.getValue().getIdMoniteur());
                exam.setIdCandidat(condidatList.getValue().getCIN());
                exam.setDurationExamen(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
                exam.setPrix((float)prix);
                exam.setStatus(isSuccess.isSelected());
                // Mettre à jour dans la base de données
                ExamenCodeServices examenCodeServices=new ExamenCodeServices();
                if (examenCodeServices.update(examenCode.getId(), exam)) {
                    loadExamenTable(); // Rafraîchir la table
                } else {
                    System.out.println("Échec de la modification.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            annulerEdit(event);
            loadExamenTable();
        }
    }
    public boolean testInputsUpdate(ExamenCode sc) {
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
                List<ExamenCode> filteredExamen = examenCodesToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getId(), sc.getId()))
                        .collect(Collectors.toList());
                if (!TimeCheckServices.isIngenieurSeanceValable(
                        seancesCodesToday(),
                        ingenieurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorIngenieur
                )||!TimeCheckServices.isIngenieurExamenValable(
                        filteredExamen,
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
                List<ExamenCode> filteredCodeExamens = examenCodesToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getId(), sc.getId()))
                        .collect(Collectors.toList());

                // Combine with conduite sessions
                List<? extends Sceance> combinedSceances = Stream.concat(
                        seancesConduitToday().stream(),
                        seancesCodesToday().stream()
                ).collect(Collectors.toList());
                List<? extends Examen> combinedExamen=Stream.concat(
                        filteredCodeExamens.stream(),
                        examenConduitsToday().stream()
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
                )) {
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
    private void annulerEdit(ActionEvent event) {
        button.setVisible(true);
        isSuccess.setVisible(false);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        resetInputs();
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
    @FXML
    private void switchToCalendar(ActionEvent event) {
        try {
            App.setRoot("calender");
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
    @FXML
    private void switchToScCode(ActionEvent event){
        SingleDayCodeController.setSelectedDate(selectedDate);
        try {
            App.setRoot("sceanceDayMenuCode");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void setSelectedDate(LocalDate selectedDate) {
        ExamenCodeController.selectedDate = selectedDate;
    }
}
