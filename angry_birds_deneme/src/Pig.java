import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//bu sınıfları (domuz ve kuş) extend edilebilen bi süper classa çevirirsek daha iyi olabilir .
public class Pig {
    double pigX;
    double pigY;


    double pigWidth;

    double pigHeight;
    BufferedImage pigImage;
    public Pig(double pigX, double pigY, double pigHeight, double pigWidth){
        this.pigHeight = pigHeight;
        this.pigWidth = pigWidth;
        this.pigX = pigX;
        this.pigY = pigY;
        try {
            pigImage = ImageIO.read(new FileImageInputStream(new File("pig.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Rectangle getBounds() {
        return new Rectangle((int) pigX, (int) pigY, (int) pigWidth, (int) pigY);
    }
}
