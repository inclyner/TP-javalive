package pt.isec.pa.javalife.ui.gui.res;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    private static final Map<String, Image> images = new HashMap<>();

    // Método para obter uma instância única de imagem para uma chave específica
    public static Image getInstance(String key) {
        return images.get(key);
    }

    // Método para carregar uma imagem de um arquivo e associá-la a uma chave
    public static void loadImage(String key, String path) {
        if (!images.containsKey(key)) {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                images.put(key, image);
            } catch (Exception e) {
                System.out.println("Erro ao carregar imagem: " + e.getMessage());
            }
        }
    }

}
