package com.conceptualGraph;

import com.conceptualGraph.model.Reader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

//import java.io.IOException;



public class HelloFX extends Application {

    private double[] params = {0,0,0,0,0,0,0};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Conceptual graph");

        Button checkBookBtn = new Button();
        checkBookBtn.setText("Проверить книгу"); // Proverit' knigu
        checkBookBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно проверки книги"); // Otkrivaem okno proverki knigi

                openCheckBookWindow(event);
            }
        });
        checkBookBtn.setPrefSize(150.0, 180.0);
        checkBookBtn.setAlignment(Pos.BOTTOM_CENTER);

        Button compareBooksBtn = new Button();
        compareBooksBtn.setText("Сравнить книги"); // Sravnit' knigi
        compareBooksBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно сравнения книг"); // Otkrivaem okno sravneniya knig
                // to do...
            }
        });
        compareBooksBtn.setPrefSize(150.0, 180.0);
        compareBooksBtn.setAlignment(Pos.BOTTOM_CENTER);

        Button archiveBtn = new Button();
        archiveBtn.setText("Архив"); // Arhiv
        archiveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно архива"); // Otkrivaem okno arhiva
                // to do...
            }
        });
        archiveBtn.setPrefSize(150.0, 180.0);
        archiveBtn.setAlignment(Pos.BOTTOM_CENTER);

        FlowPane root = new FlowPane();
        root.getChildren().addAll(checkBookBtn, compareBooksBtn, archiveBtn);
        primaryStage.setScene(new Scene(root, 600, 400));
        root.setPadding(new Insets(110, 55, 110, 55));
        root.setHgap(20);
        root.setVgap(20);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void openCheckBookWindow(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Check book");
        BorderPane checkBookWindow = new BorderPane();

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("Файл"); // File
        Menu helpMenu = new Menu("Помощь"); // Pomosh'

        // Create MenuItems
        MenuItem openFileItem = new MenuItem("Выбрать документ"); // Vibrat' document
        MenuItem exitItem = new MenuItem("Выход"); // Vihod

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, exitItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        // Govno iz jopi
        final FileChooser fileChooser = new FileChooser();
        openFileItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                File file = fileChooser.showOpenDialog(stage);
                System.out.println("Open file: " + file);
                // Тут присобачить ваши функции (Tut prisobachit' vashi funkcii)
                ProgressIndicator waiting = new ProgressIndicator();
                checkBookWindow.setRight(null);
                checkBookWindow.setBottom(null);
                checkBookWindow.setCenter(waiting);
                params = Reader.checkAndRead(file);

                // textArea
                TextArea textArea = new TextArea();
                textArea.setPrefColumnCount(20);
                textArea.setPrefRowCount(45);
                textArea.editableProperty().set(false);
                Label lbl1 = new Label("Часто повторяющиеся термины"); // Chasto povtoryaushiesya termini

                // indicator
                // ВАЖНО! - использовать "pi.setProgress(_значение_);" чтобы установить результат обработки
                ProgressIndicator pi = new ProgressIndicator(0);
                pi.setPrefSize(80, 80);
                pi.setMaxSize(80, 80);
                pi.setMinSize(80,80);
                pi.setProgress(params[6]);
                Label lbl2 = new Label("Процент найденных описаний терминов:"); // Procent naidenih opisaniy terminov

                //bars
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis1 = new NumberAxis();
                yAxis1.autoRangingProperty().set(false);
                yAxis1.setLowerBound(0);
                yAxis1.setUpperBound(100);
                yAxis1.setLabel("Количество терминов"); // Kolichestvo terminov
                NumberAxis yAxis2 = new NumberAxis();
                yAxis2.autoRangingProperty().set(false);
                yAxis2.setLowerBound(0);
                yAxis2.setUpperBound(1);
                yAxis2.setLabel("Средняя сложность терминов"); // Srednyaya slozhnost' terminov
                NumberAxis yAxis3 = new NumberAxis();
                yAxis3.setLabel("Количество слов"); // Kolichestvo slov
                NumberAxis yAxis4 = new NumberAxis();
                yAxis4.setLabel("Связность терминов"); // Svyaznost' terminov
                NumberAxis yAxis5 = new NumberAxis();
                yAxis5.setLabel("Примерное время изучения (ч)"); // Primernoe vremya izucheniya (ch)
                NumberAxis yAxis6 = new NumberAxis();
                yAxis6.setLabel("Сложность книги"); // Slozhnost' knigi

                // Create a BarChart
                BarChart<Number, String> barChart1 = new BarChart<>(yAxis1, xAxis);
                BarChart<Number, String> barChart2 = new BarChart<>(yAxis2, xAxis);
                BarChart<Number, String> barChart3 = new BarChart<>(yAxis3, xAxis);
                BarChart<Number, String> barChart4 = new BarChart<>(yAxis4, xAxis);
                BarChart<Number, String> barChart5 = new BarChart<>(yAxis5, xAxis);
                BarChart<Number, String> barChart6 = new BarChart<>(yAxis6, xAxis);

                // Series
                // ВАЖНО! - вместо аргумента xValue создать переменную со значением 0 и в неё получать результат обработки
                XYChart.Series<Number, String> dataSeries1 = new XYChart.Series<>();
                dataSeries1.getData().add(new XYChart.Data<>(params[0], ""));

                XYChart.Series<Number, String> dataSeries2 = new XYChart.Series<>();
                dataSeries2.getData().add(new XYChart.Data<>(params[3], ""));

                XYChart.Series<Number, String> dataSeries3 = new XYChart.Series<>();
                dataSeries3.getData().add(new XYChart.Data<>(params[1], ""));

                XYChart.Series<Number, String> dataSeries4 = new XYChart.Series<>();
                dataSeries4.getData().add(new XYChart.Data<>(params[4], ""));

                XYChart.Series<Number, String> dataSeries5 = new XYChart.Series<>();
                dataSeries5.getData().add(new XYChart.Data<>(params[2], ""));

                XYChart.Series<Number, String> dataSeries6 = new XYChart.Series<>();
                dataSeries6.getData().add(new XYChart.Data<>(params[5], ""));

                // Add Series to BarChart.
                barChart1.getData().add(dataSeries1);
                barChart2.getData().add(dataSeries2);
                barChart3.getData().add(dataSeries3);
                barChart4.getData().add(dataSeries4);
                barChart5.getData().add(dataSeries5);
                barChart6.getData().add(dataSeries6);

                //Sizing
                barChart1.setPrefSize(400, 150);
                barChart1.setMinSize(400,150);
                barChart1.setMaxSize(400,150);

                barChart2.setPrefSize(400, 150);
                barChart2.setMinSize(400,150);
                barChart2.setMaxSize(400,150);

                barChart3.setPrefSize(400, 150);
                barChart3.setMinSize(400,150);
                barChart3.setMaxSize(400,150);

                barChart4.setPrefSize(400, 150);
                barChart4.setMinSize(400,150);
                barChart4.setMaxSize(400,150);

                barChart5.setPrefSize(400, 150);
                barChart5.setMinSize(400,150);
                barChart5.setMaxSize(400,150);

                barChart6.setPrefSize(400, 150);
                barChart6.setMinSize(400,150);
                barChart6.setMaxSize(400,150);

                //Colors
                for(Node n:barChart1.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #B22222;");
                }
                for(Node n:barChart2.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #FF6347;");
                }
                for(Node n:barChart3.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #FFD700;");
                }
                for(Node n:barChart4.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #2E8B57;");
                }
                for(Node n:barChart5.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #4682B4;");
                }
                for(Node n:barChart6.lookupAll(".default-color0.chart-bar")) {
                    n.setStyle("-fx-bar-fill: #4B0082;");
                }

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(30, 10, 10, 10));
//        grid.gridLinesVisibleProperty().set(true);

                GridPane gridRight = new GridPane();
                gridRight.setHgap(10);
                gridRight.setVgap(10);
                gridRight.setPadding(new Insets(10, 10, 10, 10));
//        gridRight.gridLinesVisibleProperty().set(true);

                GridPane gridBottom = new GridPane();
                gridBottom.setHgap(10);
                gridBottom.setVgap(10);
                gridBottom.setPadding(new Insets(10, 10, 10, 10));
//        gridBottom.gridLinesVisibleProperty().set(true);

                gridRight.add(lbl1, 0, 0);
                gridRight.add(textArea, 0, 1);

                GridPane.setValignment(lbl2, VPos.CENTER);
                GridPane.setHalignment(lbl2, HPos.LEFT);
                gridBottom.add(lbl2, 0, 0);
                GridPane.setValignment(pi, VPos.BOTTOM);
                GridPane.setHalignment(pi, HPos.CENTER);
                gridBottom.add(pi, 0, 1);

                grid.add(barChart1, 0, 0);
                grid.add(barChart2, 1, 0);
                grid.add(barChart3, 0, 1);
                grid.add(barChart4, 1, 1);
                grid.add(barChart5, 0, 2);
                grid.add(barChart6, 1, 2);

                checkBookWindow.setCenter(grid);
                checkBookWindow.setRight(gridRight);
                checkBookWindow.setBottom(gridBottom);

            }
        });

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        stage.setScene(new Scene(checkBookWindow, 1150, 750));
        checkBookWindow.setTop(menuBar);
        stage.show();
        stage.setResizable(false);

        // Hide this current window
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
