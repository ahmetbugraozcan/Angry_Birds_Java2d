import java.awt.*;

public class main {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameScreen gameScreen = new GameScreen();
                Game game = new Game();
                gameScreen.add(game);

                gameScreen.setVisible(true);


            }
        });


    }
}
