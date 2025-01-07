package up.edu.isgc.ray_tracer.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * @author Arreola
 * Saves the image
 * */
public class FileManager {
    public static void saveImage(BufferedImage image, String fileName, String type){
        File newFile = new File(fileName +"."+ type);
        try {
            ImageIO.write(image, type, newFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
