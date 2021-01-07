import javax.swing.*;
import java.awt.*;

public class GameScreen extends JFrame {

    //ayarlamalar tam oturmadığından 1366x768 en iyi değerdir. her ne kadar objeleri ekran boyutuna oranla yerleştirmeye çalışsam da kaymalar oluyor
    public static int gameScreenWidth = 1366;
    public static int gameScreenHeight = 768;


    public GameScreen(){

        setFocusable(false);
        setResizable(false);
        setTitle("Angry Birds");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(gameScreenWidth, gameScreenHeight);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);


    }



}
