package com.conceptualGraph;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class HelloFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Conceptual graph");

        Button checkBookBtn = new Button();
        checkBookBtn.setText("Проверить книгу");
        checkBookBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно проверки книги");

                openCheckBookWindow(event);
            }
        });
        checkBookBtn.setPrefSize(150.0, 180.0);
        checkBookBtn.setAlignment(Pos.BOTTOM_CENTER);

        Button compareBooksBtn = new Button();
        compareBooksBtn.setText("Сравнить книги");
        compareBooksBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно сравнения книг");
                // to do...
            }
        });
        compareBooksBtn.setPrefSize(150.0, 180.0);
        compareBooksBtn.setAlignment(Pos.BOTTOM_CENTER);

        Button archiveBtn = new Button();
        archiveBtn.setText("Архив");
        archiveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Открываем окно архива");
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
        // Create MenuBar
        MenuBar menuBar = new MenuBar();
        // Create menus
        Menu fileMenu = new Menu("Файл");
        Menu helpMenu = new Menu("Помощь");
        // Create MenuItems
        MenuItem openFileItem = new MenuItem("Выбрать документ");
        MenuItem exitItem = new MenuItem("Выход");
        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, exitItem);
        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        // textArea
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(20);
        textArea.setPrefRowCount(10);
        Label lbl = new Label("Test text label");

        BorderPane checkBookWindow = new BorderPane();
        Stage stage = new Stage();
        stage.setTitle("Check book");
        stage.setScene(new Scene(checkBookWindow, 1200, 800));
        checkBookWindow.setTop(menuBar);
        checkBookWindow.setBottom(lbl);
        checkBookWindow.setRight(textArea);
        stage.show();
        stage.setResizable(false);
        // Hide this current window
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
