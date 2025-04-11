package org.example.Utils.Valdation;

import java.awt.image.BufferedImage;

/**
 * Classe che fornisce metodi per validare le immagini delle tracce.
 */
public class ImageValidator {

    private final int requiredWidth;
    private final int requiredHeight;

    /**
     * Costruttore che specifica le dimensioni richieste per l'immagine.
     *
     * @param requiredWidth La larghezza richiesta in pixel
     * @param requiredHeight L'altezza richiesta in pixel
     */
    public ImageValidator(int requiredWidth, int requiredHeight) {
        this.requiredWidth = requiredWidth;
        this.requiredHeight = requiredHeight;
    }

    /**
     * Valida che l'immagine abbia le dimensioni corrette.
     *
     * @param image L'immagine da validare
     * @throws IllegalArgumentException Se l'immagine non ha le dimensioni corrette
     */
    public void validateImage(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("L'immagine non può essere null");
        }

        if (image.getWidth() != requiredWidth || image.getHeight() != requiredHeight) {
            throw new IllegalArgumentException(
                    String.format("Le dimensioni dell'immagine devono essere %dx%d pixel",
                            requiredWidth, requiredHeight)
            );
        }
    }

    /**
     * Verifica se l'immagine ha un formato supportato.
     *
     * @param fileName Il nome del file dell'immagine
     * @return true se il formato è supportato, false altrimenti
     */
    public boolean isSupportedFormat(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg");
    }
}