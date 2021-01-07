import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    BufferedImage backGround;
    int backGroundX;
    int backGroundY;
    int backGroundWidth;
    int backGroundHeight;

    BufferedImage backGroundImage;
    public Background(int backGroundX, int backGroundY, int backGroundHeight, int backGroundWidth){
        this.backGroundX = backGroundX;
        this.backGroundY = backGroundY;
        this.backGroundHeight = backGroundHeight;
        this.backGroundWidth = backGroundWidth;

        try {
            backGroundImage = ImageIO.read(new FileImageInputStream(new File("background.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
