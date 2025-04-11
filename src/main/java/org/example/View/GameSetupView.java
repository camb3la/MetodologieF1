package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Controller.GameController;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.Model.Grid.GridFactory;
import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.OnGameEndListener;
import org.example.Utils.ErrorHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class GameSetupView {
    private TextField selectedImagePath;
    private Spinner<Integer> botSpinner;
    private TextField playerNameField;
    private final VBox playersPanel;
    private final ArrayList<String> playerNames;
    private File selectedFile;
    private final Stage stage;
    private Stage gameStage;

    // Dimensioni richieste per l'immagine
    private static final int REQUIRED_WIDTH = 1920;
    private static final int REQUIRED_HEIGHT = 1080;
    private static final int CELL_SIZE = 20;

    public GameSetupView(Stage stage) {
        this.stage = stage;
        this.playerNames = new ArrayList<>();

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Image selection section
        VBox imageSection = createImageSection();

        // Bot selection section
        HBox botSection = createBotSection();

        // Player addition section
        VBox playerSection = createPlayerSection();

        // Players list
        playersPanel = new VBox(5);
        playersPanel.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(playersPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(150);

        // Start button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame());

        // Add all sections to main container
        mainContainer.getChildren().addAll(
                imageSection,
                new Separator(),
                botSection,
                new Separator(),
                playerSection,
                scrollPane,
                startButton
        );

        Scene scene = new Scene(mainContainer, 600, 500);
        stage.setTitle("Game Setup");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    private VBox createImageSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER_LEFT);

        HBox imageRow = new HBox(10);
        Label imageLabel = new Label("Track Image:");
        selectedImagePath = new TextField();
        selectedImagePath.setEditable(false);
        selectedImagePath.setPrefWidth(300);
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> selectImage());

        imageRow.getChildren().addAll(imageLabel, selectedImagePath, browseButton);
        section.getChildren().add(imageRow);

        return section;
    }

    private HBox createBotSection() {
        HBox section = new HBox(10);
        section.setAlignment(Pos.CENTER_LEFT);

        Label botLabel = new Label("Number of Bots:");
        botSpinner = new Spinner<>(0, 10, 0);
        botSpinner.setEditable(true);

        section.getChildren().addAll(botLabel, botSpinner);
        return section;
    }

    private VBox createPlayerSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER_LEFT);

        HBox inputRow = new HBox(10);
        Label playerLabel = new Label("Player Name:");
        playerNameField = new TextField();
        playerNameField.setPrefWidth(200);
        Button addButton = new Button("Add Player");
        addButton.setOnAction(e -> addPlayer());

        inputRow.getChildren().addAll(playerLabel, playerNameField, addButton);
        section.getChildren().add(inputRow);

        return section;
    }

    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFile = file;
            selectedImagePath.setText(file.getPath());
        }
    }

    private void addPlayer() {
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            showError("Please enter a player name");
            return;
        }

        if (playerNames.contains(playerName)) {
            showError("Player name already exists");
            return;
        }

        playerNames.add(playerName);
        updatePlayersList();
        playerNameField.clear();
    }

    private void updatePlayersList() {
        playersPanel.getChildren().clear();

        for (String name : playerNames) {
            HBox playerEntry = new HBox(10);
            playerEntry.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(name);
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> {
                playerNames.remove(name);
                updatePlayersList();
            });

            playerEntry.getChildren().addAll(nameLabel, removeButton);
            playersPanel.getChildren().add(playerEntry);
        }
    }

    private void startGame() {
        try {
            if (selectedFile == null) {
                showError("Please select an image");
                return;
            }

            if (playerNames.isEmpty()) {
                showError("Please add at least one player");
                return;
            }

            // Log dell'inizio del processo
            System.out.println("Starting game setup...");

            // Carica l'immagine
            BufferedImage image = ImageIO.read(selectedFile);
            System.out.println("Image loaded successfully: " + selectedFile.getPath());

            if (image == null) {
                throw new IllegalStateException("Failed to load image");
            }

            if (image.getWidth() != REQUIRED_WIDTH || image.getHeight() != REQUIRED_HEIGHT) {
                showError("Image must be " + REQUIRED_WIDTH + "x" + REQUIRED_HEIGHT + " pixels");
                return;
            }

            System.out.println("Creating grid...");
            // Utilizziamo la factory per creare la griglia invece del costruttore diretto
            IGrid grid = GridFactory.createGrid(image, CELL_SIZE, REQUIRED_WIDTH, REQUIRED_HEIGHT);
            System.out.println("Grid created successfully");

            System.out.println("Creating GridView...");
            GridView gridView = new GridView(grid, image);
            ScrollPane scrollPane = new ScrollPane(gridView);
            scrollPane.setPannable(true);
            System.out.println("GridView created successfully");

            System.out.println("Creating players...");
            List<Player> players = createPlayers(grid);
            System.out.println("Created " + players.size() + " players successfully");

            System.out.println("Initializing game controller...");
            GameController controller = new GameController(grid, players, gridView);
            System.out.println("Game controller initialized successfully");

            controller.setOnGameEndListener(new OnGameEndListener() {
                @Override
                public void newGame() {
                    gameStage.close();
                    stage.show();
                }

                @Override
                public void quitGame() {
                    gameStage.close();
                    stage.close();
                }
            });

            gameStage = new Stage();
            gameStage.setTitle("Race Game");
            Scene gameScene = new Scene(scrollPane);
            gameStage.setScene(gameScene);
            gameStage.setMaximized(true);

            stage.hide();
            gameStage.show();
            System.out.println("Game started successfully");

        } catch (Exception e) {
            ErrorHandler.handleError("Failed to start game", e);
            e.printStackTrace();
        }
    }

    private List<Player> createPlayers(IGrid grid) {
        int totalPlayers = playerNames.size() + botSpinner.getValue();
        List<Position> startPositions = grid.getStartingPositions(totalPlayers);
        List<Player> players = new ArrayList<>();

        // Crea i giocatori umani
        for (int i = 0; i < playerNames.size(); i++) {
            players.add(new Player(playerNames.get(i), startPositions.get(i), false));
        }

        // Crea i bot
        for (int i = 0; i < botSpinner.getValue(); i++) {
            String botName = "Bot " + (i + 1);
            players.add(new Player(botName, startPositions.get(playerNames.size() + i), true));
        }

        return players;
    }

    public void show() {
        stage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}