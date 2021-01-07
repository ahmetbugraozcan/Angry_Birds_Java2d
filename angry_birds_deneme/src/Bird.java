import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird {
    //bu sınıfları (domuz ve kuş) extend edilebilen bi süper classa çevirirsek daha iyi olabilir .
    double birdX;
    double birdY;

    double birdWidth;

    double birdHeight;
    BufferedImage birdImage;
    public Bird(double birdX, double birdY, double birdHeight, double birdWidth){
        this.birdHeight = birdHeight;
        this.birdWidth = birdWidth;
        this.birdX = birdX;
        this.birdY = birdY;
        try {
            birdImage = ImageIO.read(new FileImageInputStream(new File("bird.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Rectangle getBounds() {
        return new Rectangle((int)birdX, (int)birdY, (int)birdWidth, (int)birdHeight);
    }
}
