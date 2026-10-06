package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Sceance;
import org.example.entities.SceanceConduite;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.SceanceCodeServices;
import org.example.service.SceanceConduiteServices;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;
    @FXML
    private ImageView mainLogo;
    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();

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

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<Sceance>> sceanceMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<Sceance> sceanceList = sceanceMap.get(currentDate);
                        if(sceanceList != null){
                            createCalendarActivity(sceanceList, rectangleHeight, rectangleWidth, stackPane);
                        }
                        //**********
                        LocalDate currentLocalDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);
                        // If it's Sunday, set the text color to red.
                        if(currentLocalDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            rectangle.setFill(Paint.valueOf("#FF7F7F"));
                        }
                        // Attach a click event to the entire cell.
                        final int finalCurrentDate = currentDate; // must be final for the lambda
                        stackPane.setOnMouseClicked(event -> {
                            // Set the selected date from the cell even if no activities (VBox) exists.
                            SingleDayCodeController.setSelectedDate(
                                    ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), finalCurrentDate, 0, 0, 0, 0, dateFocus.getZone())
                                            .toLocalDate()
                            );
                            try {
                                if (!SingleDayCodeController.getSelectedDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                                    App.setRoot("sceanceDayMenuCode");
                                }
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        });

                        //************
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setFill(Color.LIGHTGREEN);
                    }
                    //*********
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<Sceance> sceances, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < sceances.size(); k++) {
            int finalK = k;
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                break;
            }

            Text text;
            if (sceances.get(k) instanceof SceanceConduite) {
                text = new Text("sceance:CT " + sceances.get(k).getSceanceId() + ", " + sceances.get(k).getSceanceDate().toLocalTime());
            }else{
                text = new Text("sceance:CE " + sceances.get(k).getSceanceId() + ", " + sceances.get(k).getSceanceDate().toLocalTime());
            }
            calendarActivityBox.getChildren().add(text);
            calendarActivityBox.setOnMouseClicked(event -> {
                System.out.println(sceances.get(finalK).getSceanceDate());
                SingleDayConduiteController.setSelectedDate(sceances.get(finalK).getSceanceDate().toLocalDate());
                try {
                    if (!sceances.get(finalK).getSceanceDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        //*******dfkjhvnefgnotnbjgr
                        SingleDayCodeController.setSelectedDate(sceances.get(finalK).getSceanceDate().toLocalDate());
                        App.setRoot("sceanceDayMenuCode");
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<Sceance>> createCalendarMap(List<Sceance> sceances) {
        //List<? extends Sceance> sceances
        Map<Integer, List<Sceance>> sceanceMap = new HashMap<>();

        for (Sceance sc: sceances) {
            int sceanceDate = sc.getSceanceDate().getDayOfMonth();
            if(!sceanceMap.containsKey(sceanceDate)){
                sceanceMap.put(sceanceDate, List.of(sc));
            } else {
                List<Sceance> OldListByDate = sceanceMap.get(sceanceDate);

                List<Sceance> newList = new ArrayList<>(OldListByDate);
                newList.add(sc);
                sceanceMap.put(sceanceDate, newList);
            }
        }
        return sceanceMap;
    }

    private Map<Integer, List<Sceance>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<Sceance> sceances = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        SceanceCodeServices sceanceCodeServices = new SceanceCodeServices();
        SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
        List<Sceance> sceanceList= Stream.concat(sceanceConduiteServices.getAll().stream(),sceanceCodeServices.getAll().stream()).collect(Collectors.toList());

        return createCalendarMap(
                sceanceList.stream()
                        .filter(sc -> sc.getSceanceDate().getYear() == dateFocus.getYear() && sc.getSceanceDate().getMonth() == dateFocus.getMonth())
                        .collect(Collectors.toList()));

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
    private void switchToCondidat(ActionEvent event) throws IOException {

        App.setRoot("condidat");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    @FXML
    public void switchToSetting(ActionEvent actionEvent)throws IOException{
        App.setRoot("autoecoleinfo");
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
    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }


}
