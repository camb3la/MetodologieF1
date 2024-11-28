package org.example.Utils;


import javafx.scene.control.Alert;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class ErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());

    static {
        try {
            FileHandler fh = new FileHandler("game-errors.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleError(String context, Exception e) {
        String errorMessage = String.format("%s: %s", context, e.getMessage());
        LOGGER.log(Level.SEVERE, errorMessage, e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}