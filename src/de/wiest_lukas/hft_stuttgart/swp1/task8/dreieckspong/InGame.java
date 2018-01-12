// (C)2018 Lukas Wiest
package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import static de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong.Aufgabe8_Dreieckspong.FRAME;
import de.wiest_lukas.lib.AACPlayer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author Lukas Wiest
 */
public class InGame extends JPanel
{
    private JPanel stats;                   // JPanel holding the points etc on the right side
    private JTextField score;               // textfield showing the current score
    private JLabel muted;                   // label for showing the user that the audio is muted

    private PlayFieldPanel playground;      // JPanel containing the actual playfield
    private PongLine left;                  // left side of triangle
    private PongLine right;                 // right side of triangle
    private PongLine player;                // bottom side of triangle / player
    private Puck puck;                      // moving puck

    private Timer puckMovage;               // this timer moves the puck
    private Timer refresher;                // this timer calls the JFrames repaint method
    private Timer playerMove;               // this timer moves the player (bottom triangle side)
    private int playerDirection;            // set's the direction the player has to be moved

    private AACPlayer music;                // musicPlayer instance for background music
    private AACPlayer ponged;               // and the hit sound

    public InGame(Dimension playFieldSize)
    {
        this.initComponents();                  // init ourselfs
        this.initStatsPanel();                  // init statistics panel and all it's components
        this.initPlayfieldPanel(playFieldSize); // init playfield and all it's components
        this.initAudio();                       // init the audio playback

        FRAME.getContentPane().removeAll();     // remove everything from frame
        FRAME.getContentPane().add(this);       // add us to it
        FRAME.pack();                           // get us room
        FRAME.setFocusable(true);               // make sure the frame...
        this.setFocusable(true);                // ...and we are both focusable
        this.requestFocusInWindow();            // request focus
    }

    private void initComponents()
    {
        this.setVisible(true);                  // we want to be seen
        this.setLayout(new BorderLayout());     // and have a BorderLayout

        this.addKeyListener(new KeyListener()   // we need to listen to the user to play
        {
            @Override
            public void keyTyped(KeyEvent ke) { }

            @Override
            public void keyPressed(KeyEvent ke)
            {
                switch (ke.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        playerDirection = -5;   // set the playerDirection
                        playerMove.start();     // and start the player movement timer
                        break;

                    case KeyEvent.VK_RIGHT:
                        playerDirection = 5;    // see above
                        playerMove.start();
                        break;

                    case KeyEvent.VK_C:         // if CTRL+C got pressed, exit ingame
                        if (ke.getModifiers() == KeyEvent.CTRL_MASK)
                        {
                            exitIngame();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke)
            {
                switch (ke.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                        playerMove.stop();      // stop the player movement timer
                        break;

                    case KeyEvent.VK_M:
                        if (music != null)      // if music is not null
                            music.toggleMute(); // toggle the mute
                        if (ponged != null)     // same for the hit sound
                            ponged.toggleMute();

                        // invert the visibility of the muted label
                        muted.setVisible(!muted.isVisible());
                        break;
                }
            }
        });

        this.refresher = new Timer(5, al ->
        {
            FRAME.repaint();                    // repaint all 5ms as this is the fastest change ever made
        });
        refresher.start();

        this.playerMove = new Timer(25, al ->
        {
            movePlayer(playerDirection);
        });
    }

    private void initStatsPanel()
    {
        // surrounding JPanel with BorderLayout for optical reasons
        JPanel statsContainer = new JPanel(new BorderLayout());
        this.stats = new JPanel();
        // actual Panel with a BoxLayout
        this.stats.setLayout(new BoxLayout(this.stats, BoxLayout.PAGE_AXIS));

        // add actual Panel to the outer one to the Norht, as this is only using the full width,
        // but the preferred height
        statsContainer.add (this.stats, BorderLayout.NORTH);
        // then add the surrounding panel to this (the InGame panel) on the east
        this.add(statsContainer, BorderLayout.EAST);

        this.stats.add(new JLabel("Punkte: "));
        this.score = new JTextField();
        this.score.setEditable(false);
        this.score.setText("0");
        this.score.setHorizontalAlignment(SwingConstants.RIGHT);
        this.score.setBackground(Color.white);
        // make this unfocusable, instead we wouldn't be able to get the focus back to the
        // InGame Panel afterwards
        this.score.setFocusable(false);
        this.stats.add(this.score);

        this.muted = new JLabel("muted");
        // this is changed if we toggle the mute to visible
        this.muted.setVisible(false);
        this.stats.add(this.muted);
    }

    private void initPlayfieldPanel(Dimension playFieldSize)
    {
        this.setLines(playFieldSize);       // init the lines
        this.setPuck();                     // init the puck

        // generate new playground panel
        this.playground = new PlayFieldPanel(playFieldSize, this.left, this.right, this.puck, this.player);
        // and add it to the Center
        this.add(this.playground, BorderLayout.CENTER);
    }

    private void setLines(Dimension playFieldSize)
    {
        // start Vector will be in the middle and 10 pixels down
        int startX = (int) (playFieldSize.getWidth() / 2.);
        int startY = 10;

        // destination Vector will be near the ground and the sides
        int destX = 5;
        int destY = (int) (playFieldSize.getHeight()) - 10;
        left = new PongLine(startX, startY, destX, destY, PongLine.Inside.LEFT);

        destX = (int) (playFieldSize.getWidth()) - 5;
        right = new PongLine(startX, startY, destX, destY, PongLine.Inside.RIGHT);

        this.player = new PongLine (
                this.left.getX1() - 25 ,
                this.left.getY2(),
                this.left.getX1() + 25,
                this.left.getY2(),
                PongLine.Inside.LEFT
        );
    }

    private void setPuck()
    {
        this.puck = new Puck();
        // for the beginning, set the start point relatively centerd
        this.puck.setLocation(this.left.getX1(), this.left.getY2() / 2);
        // the puck should be 10px by 10px
        this.puck.setDiameter(10);

        this.puckMovage = new Timer(30, al ->
        {
            puck.move();
            checkForCollission();
        });
        this.puckMovage.start();
    }

    /**
     * Init the Audio playback.
     * 
     * - exports the audio files from the jar to the systems tmp folder<br>
     * - creates AACPlayer instances for each file<br>
     * - enables the repeat for the background music<br>
     */
    private void initAudio()
    {
        URL backgroundMusicURL = getClass().getResource("/de/wiest_lukas/hft_stuttgart/swp1/task8/dreieckspong/music/backgroundMusic.m4a");
        URL pongURL = getClass().getResource("/de/wiest_lukas/hft_stuttgart/swp1/task8/dreieckspong/music/pong.m4a");
        try
        {
            File backgroundMusic = File.createTempFile("PongBackground", ".m4a");
            try(InputStream is = backgroundMusicURL.openStream(); OutputStream os = new FileOutputStream(backgroundMusic))
            {
                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, bytesRead);
                }
            }

            File pongSound = File.createTempFile("PongEffect", ".m4a");
            try(InputStream is = pongURL.openStream(); OutputStream os = new FileOutputStream(pongSound))
            {
                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, bytesRead);
                }
            }

            this.music = new AACPlayer(backgroundMusic);
            this.music.enableRepeat();
            this.ponged = new AACPlayer(pongSound);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.music = null;
            this.ponged = null;
        }

        if (this.music != null)
            this.music.play();
    }

    private void movePlayer(int moveMent)
    {
        if (this.player.getX1() + moveMent < left.getX2() || this.player.getX2() + moveMent > right.getX2())
            return;

        this.player.setLine(
                this.player.getX1() + moveMent,
                this.player.getY1(),
                this.player.getX2() + moveMent,
                this.player.getY2()
        );
    }

    private void checkForCollission()
    {
        double tolerance = 3.;

        if (calcDistance(this.right) < tolerance && this.right.movesToMe())
            changeDirection(this.right);
        if (calcDistance(this.left) < tolerance && this.left.movesToMe())
            changeDirection(this.left);

        if (calcDistance(this.player) < tolerance && this.player.movesToMe())
        {
            if (puck.getX() >= (player.getX1() - tolerance) && puck.getX() <= (player.getX2() + tolerance))
            {
                changeDirection(player);
                try
                {
                    this.score.setText(String.valueOf(Integer.valueOf(this.score.getText()) + (100 - puckMovage.getDelay())));
                }
                catch (NumberFormatException e)
                {
                    // don't change score if something went wrong
                }
                if (puckMovage.getDelay() > 5)
                    puckMovage.setDelay(puckMovage.getDelay() - 5);
            }
            else
                exitIngame();
        }
    }

    private double calcDistance(PongLine line)
    {
        Point2D start = line.getP1();
        Point2D end = line.getP2();

        double toReturn = puck.getX() - start.getX();
        toReturn *= end.getY() - start.getY();
        toReturn -= (puck.getY() - start.getY()) * (end.getX() - start.getX());
        toReturn /= Math.sqrt(Math.pow(end.getX() - start.getX(), 2.) + Math.pow(end.getY() - start.getY(), 2.));
        line.setNewPuckDistance(Math.abs(toReturn));
        return Math.abs(toReturn);
    }

    private void changeDirection(PongLine line)
    {
        // print income angle
        DirectionVector lineVector = line.getDirectionVector();
        DirectionVector puckVector = puck.getUnitVector();

        // get angle between current direction and hit line
        double angle = (Math.acos(puckVector.scalar(lineVector) / ((lineVector.length() * puckVector.length()))));

        // dependant on which side of the line is on the inside of the playfield use the dedicated angle change
        if (line.inside == PongLine.Inside.RIGHT)
        {
            if (2* angle > Math.PI)
            {
                angle = -2 * (Math.PI - angle);
            }
            else
            {
                angle *= 2;
            }
        }
        else
        {
            if (2* angle > Math.PI)
            {
                angle = 2 * (Math.PI - angle);
            }
            else
            {
                angle *= -2;
            }
        }

        double newX;
        double newY;

        // calculate the new Direction Vectors values
        newX = (puckVector.getValue(0) * Math.cos(angle) - puckVector.getValue(1) * Math.sin(angle));
        newY = (puckVector.getValue(0) * Math.sin(angle) + puckVector.getValue(1) * Math.cos(angle));

        // set the pucks new Direction Vector
        puck.setUnitVector(newX, newY);

        // if the hit player is available, play it
        if (this.ponged != null)
            this.ponged.play();

    }

    private void exitIngame()
    {
        this.puckMovage.stop();
        this.refresher.stop();
        if (this.music != null)
            this.music.stop();

        new AfterGame(this.score.getText());
    }

}
