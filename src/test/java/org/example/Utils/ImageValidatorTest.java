package org.example.Utils;

import org.example.Utils.Valdation.ImageValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ImageValidatorTest {

    private ImageValidator validator;
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    @BeforeEach
    public void setup() {
        validator = new ImageValidator(WIDTH, HEIGHT);
    }

    @Test
    public void testValidateImage_ValidDimensions() {
        BufferedImage validImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Non dovrebbe generare eccezioni
        assertDoesNotThrow(() -> validator.validateImage(validImage),
                "Un'immagine con dimensioni corrette non dovrebbe generare eccezioni");
    }

    @Test
    public void testValidateImage_InvalidDimensions() {
        // Immagine troppo piccola
        BufferedImage smallImage = new BufferedImage(WIDTH - 100, HEIGHT - 100, BufferedImage.TYPE_INT_RGB);
        Exception smallImageException = assertThrows(IllegalArgumentException.class,
                () -> validator.validateImage(smallImage),
                "Un'immagine con dimensioni errate dovrebbe generare un'eccezione");
        assertTrue(smallImageException.getMessage().contains("dimensioni"),
                "Il messaggio di errore dovrebbe menzionare le dimensioni");

        // Immagine troppo grande
        BufferedImage largeImage = new BufferedImage(WIDTH + 100, HEIGHT + 100, BufferedImage.TYPE_INT_RGB);
        Exception largeImageException = assertThrows(IllegalArgumentException.class,
                () -> validator.validateImage(largeImage),
                "Un'immagine con dimensioni errate dovrebbe generare un'eccezione");
        assertTrue(largeImageException.getMessage().contains("dimensioni"),
                "Il messaggio di errore dovrebbe menzionare le dimensioni");
    }

    @Test
    public void testValidateImage_NullImage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateImage(null),
                "Un'immagine null dovrebbe generare un'eccezione");
        assertTrue(exception.getMessage().contains("null"),
                "Il messaggio di errore dovrebbe menzionare che l'immagine è null");
    }

    @Test
    public void testIsSupportedFormat() {
        // Formati validi
        assertTrue(validator.isSupportedFormat("image.png"), "PNG dovrebbe essere un formato supportato");
        assertTrue(validator.isSupportedFormat("image.jpg"), "JPG dovrebbe essere un formato supportato");
        assertTrue(validator.isSupportedFormat("image.jpeg"), "JPEG dovrebbe essere un formato supportato");
        assertTrue(validator.isSupportedFormat("path/to/image.PNG"), "PNG maiuscolo dovrebbe essere un formato supportato");
        assertTrue(validator.isSupportedFormat("path/to/image.JPG"), "JPG maiuscolo dovrebbe essere un formato supportato");

        // Formati non validi
        assertFalse(validator.isSupportedFormat("image.bmp"), "BMP non dovrebbe essere un formato supportato");
        assertFalse(validator.isSupportedFormat("image.gif"), "GIF non dovrebbe essere un formato supportato");
        assertFalse(validator.isSupportedFormat("image.tiff"), "TIFF non dovrebbe essere un formato supportato");

        // Input non validi
        assertFalse(validator.isSupportedFormat(""), "Una stringa vuota non dovrebbe essere un formato supportato");
        assertFalse(validator.isSupportedFormat(null), "Un input null non dovrebbe essere un formato supportato");
    }
}