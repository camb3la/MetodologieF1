package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.View.GameSetupView;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Crea e mostra la schermata di setup
        GameSetupView gameSetup = new GameSetupView(primaryStage);
        gameSetup.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

