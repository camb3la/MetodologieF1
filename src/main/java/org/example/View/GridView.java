package org.example.View;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import org.example.Model.Grid;
import org.example.Model.Player;
import org.example.Model.Position;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GridView extends Pane {
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    private final Grid grid;
    private final Rectangle[][] cellOverlays;
    private final List<Position> highlightedCells;
    private final Map<Player, Rectangle> playerMarkers;
    private OnCellSelectListener onCellSelectListener;
    private Player currentPlayer;


    public GridView(Grid grid, BufferedImage backgroundImg) {
        this.grid = grid;
        this.highlightedCells = new ArrayList<>();
        this.playerMarkers = new ConcurrentHashMap<>();
        this.cellOverlays = new Rectangle[grid.getRows()][grid.getCols()];

        this.setMinSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setMaxSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setupBackground(backgroundImg);
        createGrid();
        setOnMouseClicked(this::handleMouseClick);
    }

    private void setupBackground(BufferedImage backgroundImg) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(backgroundImg, "png", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image fxImage = new Image(inputStream);

            BackgroundImage bgImage = new BackgroundImage(
                    fxImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO,
                            BackgroundSize.AUTO,
                            false,
                            false,
                            true,
                            true)
            );

            setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dello sfondo: " + e.getMessage());
        }
    }

    private void createGrid() {
        int cellSize = grid.getCellSize();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Rectangle cell = new Rectangle(
                        col * cellSize,
                        row * cellSize,
                        cellSize,
                        cellSize
                );

                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.GRAY);
                cell.setStrokeWidth(0.5);

                cellOverlays[row][col] = cell;
                getChildren().add(cell);
            }
        }
    }

    public void highlightPossibleMoves(List<Position> possibleMoves) {
        if (possibleMoves == null || possibleMoves.isEmpty()) {
            return;
        }

        clearHighlights();
        highlightedCells.clear();

        for (Position pos : possibleMoves) {
            int row = pos.getX();
            int col = pos.getY();

            if (row >= 0 && row < cellOverlays.length &&
                    col >= 0 && col < cellOverlays[0].length) {

                Rectangle cell = cellOverlays[row][col];
                if (cell != null) {
                    highlightedCells.add(pos);
                    cell.setFill(Color.rgb(255, 255, 0, 0.8));  // Giallo semi-trasparente
                    cell.setStroke(Color.RED);
                    cell.setStrokeWidth(2);
                }
            }
        }
    }

    private void handleMouseClick(MouseEvent event) {
        int cellSize = grid.getCellSize();
        int col = (int) (event.getX() / cellSize);
        int row = (int) (event.getY() / cellSize);

        if (row >= 0 && row < grid.getRows() && col >= 0 && col < grid.getCols()) {
            Position clickedPosition = new Position(row, col);

            boolean isHighlighted = highlightedCells.stream()
                    .anyMatch(pos -> pos.getX() == row && pos.getY() == col);

            if (isHighlighted && onCellSelectListener != null) {
                onCellSelectListener.onCellSelected(clickedPosition);
            }
        }
    }

    public void clearHighlights() {
        for (Position pos : highlightedCells) {
            Rectangle cell = cellOverlays[pos.getX()][pos.getY()];
            if (cell != null) {
                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.GRAY);
                cell.setStrokeWidth(0.5);
            }
        }
        highlightedCells.clear();
    }

    public void updatePlayerMarker(Player player, Position position) {
        Rectangle existingMarker = playerMarkers.get(player);
        if (existingMarker != null) {
            getChildren().remove(existingMarker);
        }

        int cellSize = grid.getCellSize();
        Rectangle marker = new Rectangle(
                position.getY() * cellSize + cellSize/4,
                position.getX() * cellSize + cellSize/4,
                cellSize/2,
                cellSize/2
        );

        marker.setFill(player.getColor());

        if (player.equals(currentPlayer)) {
            marker.setStroke(Color.YELLOW);
            marker.setStrokeWidth(2);
        }

        getChildren().add(marker);
        playerMarkers.put(player, marker);
    }

    public void setCurrentPlayer(Player player) {
        if (currentPlayer != null) {
            Rectangle marker = playerMarkers.get(currentPlayer);
            if (marker != null) {
                marker.setStroke(Color.TRANSPARENT);
                marker.setStrokeWidth(1);
            }
        }

        this.currentPlayer = player;

        Rectangle marker = playerMarkers.get(player);
        if (marker != null) {
            marker.setStroke(Color.YELLOW);
            marker.setStrokeWidth(2);
        }
    }

    public void setOnCellSelectListener(OnCellSelectListener listener) {
        this.onCellSelectListener = listener;
    }

    public interface OnCellSelectListener {
        void onCellSelected(Position position);
    }
}