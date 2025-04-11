package org.example.Service.Coordinate;

import org.example.Model.Position;

public class CoordinateConverter {

    /**
     * Converte una posizione dal sistema di coordinate del gioco (x,y)
     * al sistema di coordinate della griglia (riga,colonna).
     *
     * @param gamePosition La posizione nel sistema di coordinate del gioco
     * @return La posizione convertita nel sistema di coordinate della griglia
     */
    public Position gameToGrid(Position gamePosition) {
        // Nel gioco, x è la riga e y è la colonna
        // Nella griglia dell'immagine, x è la colonna e y è la riga
        return new Position(gamePosition.getY(), gamePosition.getX());
    }

    /**
     * Converte una posizione dal sistema di coordinate della griglia (riga,colonna)
     * al sistema di coordinate del gioco (x,y).
     *
     * @param gridPosition La posizione nel sistema di coordinate della griglia
     * @return La posizione convertita nel sistema di coordinate del gioco
     */
    public Position gridToGame(Position gridPosition) {
        // Inverso della conversione precedente
        return new Position(gridPosition.getY(), gridPosition.getX());
    }

    /**
     * Converte coordinate pixel dell'immagine in coordinate della griglia.
     *
     * @param pixelX La coordinata X in pixel
     * @param pixelY La coordinata Y in pixel
     * @param cellSize La dimensione di una cella in pixel
     * @return La posizione convertita in coordinate di griglia
     */
    public Position pixelToGrid(int pixelX, int pixelY, int cellSize) {
        int col = pixelX / cellSize;
        int row = pixelY / cellSize;
        return new Position(col, row);
    }

    /**
     * Converte coordinate della griglia in coordinate pixel centrali della cella.
     *
     * @param gridPosition La posizione nella griglia
     * @param cellSize La dimensione di una cella in pixel
     * @return Una coppia di coordinate pixel che rappresentano il centro della cella
     */
    public int[] gridToCenterPixel(Position gridPosition, int cellSize) {
        int centerX = gridPosition.getX() * cellSize + (cellSize / 2);
        int centerY = gridPosition.getY() * cellSize + (cellSize / 2);
        return new int[] {centerX, centerY};
    }

}
