import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Catapult {

    double catapultWidth;
    double catapultHeight;
    double catapultX;
    double catapultY;
    BufferedImage sapanImage;

    public Catapult(double catapultWidth, double catapultHeight, double catapultX, double catapultY){
        this.catapultWidth = catapultWidth;
        this.catapultHeight = catapultHeight;
        this.catapultX = catapultX;
        this.catapultY = catapultY;
        try {
            sapanImage = ImageIO.read(new FileImageInputStream(new File("sapan.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
