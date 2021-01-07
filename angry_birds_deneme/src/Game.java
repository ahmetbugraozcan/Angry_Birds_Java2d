import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


//Kuşun hareketlerini tam olarak dengeleyemedim çoğu zaman normal gitse de bazı noktalardan bıraktığımızda garip şekilde uçabiliyor


/* R TUŞU İLE OYUNU BAŞA SARABİLİRİZ.
    P TUŞU İLE OYUNU DURDURUP DEVAM ETTİREBİLİRİZ.
    EKRANA 1 KEZ TIKLAYARAK KUŞUN HIZLANMA YETENEĞİNİ KULLANABİLİRİZ.
* */

public class Game extends JPanel implements KeyListener, ActionListener, MouseMotionListener, MouseListener {

    Timer timer = new Timer(10, this);
    BufferedImage boxImage;

    {
        try {
            boxImage = ImageIO.read(new FileImageInputStream(new File("wooden_box.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // isDraggingBird --> bu değişken boş bir alanda mı yoksa kuşun üzerinde mi sürükleme yaptığımızı kontrol eder.
    // eğer kuşun üzerinde sürükleme yaptıysak kuş mouse imlecini takip eder.
    boolean isDraggingBird = false;

    //isReleased --> bu değişken true olduğu zaman sapandaki sürükleme işlemimiz bitmiş ve kuşu bırakmışız demektir.
    boolean isReleased = false;

    // releaseCounter --> bu değişken kuşu bir kez fırlattıktan sonra
    int releaseCounter = 0;

    //velocityX --> bu değişken kuşun x eksenindeki hızını belirler
    double velocityX = 0;

    //velocityY --> bu değişken kuşun y eksenindeki hızını belirler
    double velocityY = 0;

    // acceleration --> bu değişken kuşun ivmesini belirler
    double acceleration = 0.3;

    //oyunun durma devam etme durumunu kontrol eden değişken
    boolean isPaused = false;

    //Kuşun yeteneğini  1 kere kullanmasını sağlayan değişken
    boolean isSkillUsed = false;

    //Kuşu sürüklerken çıkan ses sonsuz kez çıkmasın diye böyle bir kontrol yaptık
    boolean isMusicPlayed = false;

    GameScreen gameScreen = new GameScreen();
    int birdHeight = gameScreen.getHeight() / 16;
    int birdWidth = gameScreen.getWidth() / 30;
    Catapult catapult = new Catapult(gameScreen.getWidth() / 21, gameScreen.getHeight() / 6, gameScreen.getWidth() / 8, gameScreen.getHeight() / 1.31);

    Pig pig = new Pig((catapult.catapultX) * 6, (catapult.catapultY), gameScreen.getHeight() / 10, gameScreen.getWidth() / 20);
    double boxX = pig.pigX;
    double boxY = pig.pigY;
    Bird bird = new Bird((catapult.catapultX), (catapult.catapultY), birdHeight, birdWidth);

    int birdFirstXPos = (int) bird.birdX;
    int birdFirstYPos = (int) (bird.birdY);
    Background background = new Background(0, -39, gameScreen.getHeight(), gameScreen.getWidth());


    // Kuşun sapana olan uzaklığını kontrol ettiğimiz obje. Bunun sayesinde ne kadar çektiğimize göre kuşun gittiği konum değişiyor
    AxisControlObject axisControlObject = new AxisControlObject(((int) (catapult.catapultX * 1.163)), (int) catapult.catapultY);


    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.drawImage(background.backGroundImage, background.backGroundX, background.backGroundY, background.backGroundWidth, background.backGroundHeight, this);

        g2d.drawImage(catapult.sapanImage, (int) catapult.catapultX, (int) catapult.catapultY, (int) catapult.catapultWidth, (int) catapult.catapultHeight, this);

        g2d.drawImage(bird.birdImage, (int) (bird.birdX), (int) (bird.birdY), (int) bird.birdWidth, (int) bird.birdHeight, this);

        //Domuzun üstünde durduğu temsili kutu
        g2d.setColor(Color.blue);
        g2d.drawImage(boxImage,(int)(boxX), (int)(boxY / 0.9),(int)(gameScreen.getWidth()/17),(int)(gameScreen.getHeight() / 9.6),this);
        g2d.setColor(Color.DARK_GRAY);
        if (isPaused) {
            g2d.setColor(Color.red);
            g.setFont(new Font(null, Font.PLAIN, 20));
            g2d.fillOval((int) (gameScreen.getWidth() / 1.12), (int) (gameScreen.getHeight() / 16), 15, 15);
            g2d.drawString("PAUSED", (int) (gameScreen.getWidth() / 1.1), (int) (gameScreen.getHeight() / 12));
            timer.stop();

        }


        if (pig != null) {
            g2d.drawImage(pig.pigImage, (int) (pig.pigX), (int) (pig.pigY), (int) pig.pigWidth, (int) pig.pigHeight, this);
        }

        if (releaseCounter < 1) {
            g2d.fillOval((int) bird.birdX, (int) (bird.birdY + 20), 13, 30);
            g2d.setColor(Color.black);
            g2d.drawLine((int) catapult.catapultX, (int) catapult.catapultY, (int) bird.birdX, (int) bird.birdY + 20);
            g2d.drawLine((int) catapult.catapultX + 60, (int) catapult.catapultY + 20, (int) bird.birdX + 3, (int) bird.birdY + 50);

        }

    }

    public void repaint() {

        super.repaint();
    }

    public Game() {
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(Color.cyan);
        timer.start();


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_R) {
            restart();
        }
        //P tuşuna tuşuna basıldığında oyun durur, tekrar basıldığında oyun devam eder
        if (e.getKeyCode() == KeyEvent.VK_P) {

            isPaused = !isPaused;
            if (!isPaused) {
                timer.start();
            }
        }


    }


    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        //eğer kuş bırakıldı ise hareket başlıyor.
        if (isReleased) {
            move();
        }
        if (bird.birdY >= (gameScreen.getHeight() / 1.1) && pig == null) {
            //DOMUZ VURULDU İSE AÇILAN DIALOG PANELİ
            JDialog jd = new JDialog((Dialog) null, "Tebrikler!");
            jd.setModal(true);
            jd.setLayout(null); // THIS IS A BAD IDEA //
            jd.setLocationRelativeTo(null);
            jd.setBounds(gameScreen.getWidth() / 2, gameScreen.getHeight() / 2, 500, 200);
            Container dialogContainer = jd.getContentPane();
            dialogContainer.setLayout(new BorderLayout());
            dialogContainer.add(new JLabel("                                      TEBRİKLER! DOMUZU BAŞARIYLA YOK ETTİNİZ.")
                    , BorderLayout.CENTER);
            JPanel panel1 = new JPanel();
            panel1.setLayout(new FlowLayout());
            JButton okButton = new JButton("Tekrar Oyna");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jd.setVisible(false);
                    restart();
                }
            });
            JButton cancelButton = new JButton("Çıkış Yap");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    jd.setVisible(false);
                    System.exit(0);
                }
            });

            panel1.add(okButton);
            panel1.add(cancelButton);
            dialogContainer.add(panel1, BorderLayout.SOUTH);
            jd.setResizable(false);
            jd.setVisible(true);
        }


        else if (bird.birdY >= (gameScreen.getHeight() / 1.1)) {
            playSound("bird_collision.wav");
            //DOMUZ VURULAMADI İSE AÇILAN DİALOG PANELİ
            JDialog jd = new JDialog((Dialog) null, "Başarısız oldun...");
            jd.setModal(true);
            jd.setLayout(null); // THIS IS A BAD IDEA //
            jd.setLocationRelativeTo(null);
            jd.setBounds(gameScreen.getWidth() / 2, gameScreen.getHeight() / 2, 500, 200);
            Container dialogContainer = jd.getContentPane();
            dialogContainer.setLayout(new BorderLayout());
            dialogContainer.add(new JLabel("                                                   DOMUZU YOK EDEMEDİNİZ.")
                    , BorderLayout.CENTER);

            JPanel panel1 = new JPanel();
            panel1.setLayout(new FlowLayout());

            JButton okButton = new JButton("Tekrar Dene");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jd.setVisible(false);

                    restart();
                }
            });
            JButton cancelButton = new JButton("Çıkış Yap");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    jd.setVisible(false);
                    System.exit(0);
                }
            });

            panel1.add(okButton);
            panel1.add(cancelButton);
            dialogContainer.add(panel1, BorderLayout.SOUTH);
            jd.setResizable(false);
            jd.setVisible(true);
        }
        repaint();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        //releaseCounter 1 den küçükse kuşu hiç sürükleyip bırakmamışızdır.bu durumda kuşun mouse imlecini takip etmesi gerekir.
        if (releaseCounter < 1 && !isPaused) {
            if (((e.getX() >= bird.birdX - 50) && (e.getX() <= bird.birdX + 50)) && ((e.getY() >= bird.birdY - 50) && (e.getY() <= bird.birdY + 50))) {
                if(!isMusicPlayed){
                    playSound("bird_selected.wav");
                    isMusicPlayed = true;
                }
                //burada kuşu sürüklediğimizi anladığımızdan dolayı suruklenenKusMu ifadesini true yaptık. mouseReleased metodunda bu değeri kullanacağız.
                isDraggingBird = true;

                //kuşun çekilme sınırını koyduğumuz alan
                bird.birdX = e.getX() - 30;
                bird.birdY = e.getY() - 30;
                if (bird.birdX <= gameScreen.getWidth() / 33) {
                    bird.birdX = gameScreen.getWidth() / 33;
                }
                if (bird.birdY <= gameScreen.getHeight() / 1.5 + 50) {
                    bird.birdY = gameScreen.getHeight() / 1.5 + 50;
                }
                if (bird.birdX >= gameScreen.getWidth() / 33 + 180) {
                    bird.birdX = gameScreen.getWidth() / 33 + 180;
                }
                if (bird.birdY >= gameScreen.getHeight() / 1.5 + 170) {
                    bird.birdY = gameScreen.getHeight() / 1.5 + 170;
                }

            } else {
                isDraggingBird = false;
            }
        }

    }

    //bölümü yeniden başlatan fonksiyon
    public void restart() {
        timer.start();
        isMusicPlayed = false;
        isDraggingBird = false;
        isReleased = false;
        releaseCounter = 0;
        velocityX = 0;
        velocityY = 0;
        acceleration = 0.3;
        isSkillUsed = false;
        bird.birdX = birdFirstXPos;
        bird.birdY = birdFirstYPos;
        isPaused = false;
        pig = new Pig((catapult.catapultX) * 6, (catapult.catapultY), gameScreen.getHeight() / 10, gameScreen.getWidth() / 20);
        bird = new Bird((catapult.catapultX), (catapult.catapultY), birdHeight, birdWidth);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Kırmızı kuşun ekrana tıklandığı zaman hızlanma özelliği vardı. Kuş bırakıldı ise ekrana tıklandığı zaman hızlanma yeteneğini kullanıyor.
        if (releaseCounter >= 1 && !isSkillUsed) {
            velocityX += 10;
            isSkillUsed = true;

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {


        //eğer sürüklenen obje kuş ise bu metod içerisinde bıraktığımız nesne de kuş olacaktır.
        // kuşun fırlatılma işlemlerine isReleased i true yaparak başlıyoruz. bu değeri actionPerformed metodumuzda kullanacağız.

        if (isDraggingBird) {

            isDraggingBird = !isDraggingBird;
            velocityX = (axisControlObject.axisControlX - bird.birdX) / 15;
            velocityY = (bird.birdY - axisControlObject.axisControlY) / 5.5;
            acceleration = Math.abs((bird.birdY - axisControlObject.axisControlY)) / 290;

            isReleased = true;
            releaseCounter++;
            playSound("bird_flying.wav");

        }
    }
    // belirtilen ses dosyasını yürüten metod
    void playSound(String soundFile) {
        File f = new File("./" + soundFile);
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(audioIn);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clip.start();
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void move() {

        //kuşun ekran sınırlarından çıkmaması için bu kontrolleri yaptık.
        if (bird.birdX >= (gameScreen.getWidth() / 1.06)) {
            bird.birdX = (gameScreen.getWidth() / 1.06);
        } else if (bird.birdX <= 0) {
            bird.birdX = 0;

        }
        //eğer zemine temas ettiyse kuşun hareket işlemleri sonlanıyor.
        if (bird.birdY >= (gameScreen.getHeight() / 1.1)) {
            bird.birdY = (gameScreen.getHeight() / 1.1);
            stop();
        }

        //atış hareketini gerçekleştirdiğimiz yerler.
        bird.birdX += velocityX;
        bird.birdY -= velocityY;
        velocityY -= acceleration;
        //ekranda bir domuz olduğu sürece domuz ile çarpışma var mı yok mu kontrol ediyoruz
        if (pig != null) {
            checkCollision();
        }
    }

    public void stop() {
        //isReleased actionPerformed metodumuzdaki move() metodunu çalıştırdığından dolayı false yaparak hareketi sonlandırıyoruz.
        
        isReleased = false;
        velocityY = 0;
        velocityX = 0;
        acceleration = 0;
        releaseCounter++;
    }

    //kuşun domuz objesiyle çarpışmasını kontrol ediyor. eğer çarpışırlarsa domuz objesi yok oluyor.
    public void checkCollision() {
        Rectangle birdRect = bird.getBounds();


        //domuz objesine çarptığımızda x hızımız sıfırlanıyor yani bir duvara çarpma etkisi yaşanıyor gibi düşünebiliriz
        Rectangle domuzRect = pig.getBounds();
        if (birdRect.intersects(domuzRect)) {
            playSound("bird_collision.wav");
            velocityX = -2;
            velocityY = -4;
            pig = null;
            playSound("pig_destroyed.wav");


        }
    }

}



