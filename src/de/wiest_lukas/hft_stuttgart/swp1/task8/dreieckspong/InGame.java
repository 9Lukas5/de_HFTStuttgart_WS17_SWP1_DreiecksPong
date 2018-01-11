package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

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
    private final JFrame parent;

    private PlayFieldPanel playground;
    private final Dimension playFieldSize;

    private JPanel stats;
    private JTextField score;

    private PongLine left;
    private PongLine right;
    private PongLine player;
    private Puck puck;

    private Timer puckMovage;
    private Timer refresher;
    private Timer playerMove;
    private int playerDirection;

    private AACPlayer music;
    private AACPlayer ponged;
    private JLabel muted;

    public InGame(JFrame parent, Dimension playFieldSize)
    {
        this.playFieldSize = playFieldSize;
        this.setLines();
        this.setPuck();
        this.initComponents();
        this.parent = parent;
        this.parent.getContentPane().removeAll();
        this.parent.getContentPane().add(this);
        this.parent.pack();
        this.parent.setFocusable(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void initComponents()
    {
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);

        this.playground = new PlayFieldPanel(this.playFieldSize, this.left, this.right, this.puck, this.player);
        this.add(this.playground, BorderLayout.CENTER);

        this.stats = new JPanel();
        this.stats.setLayout(new BoxLayout(this.stats, BoxLayout.PAGE_AXIS));
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.add (this.stats, BorderLayout.NORTH);
        this.add(statsContainer, BorderLayout.EAST);
        this.stats.add(new JLabel("Punkte: "));
        this.score = new JTextField();
        this.score.setEditable(false);
        this.score.setText("0");
        this.score.setHorizontalAlignment(SwingConstants.RIGHT);
        this.score.setBackground(Color.white);
        this.score.setFocusable(false);
        this.stats.add(this.score);

        this.muted = new JLabel("muted");
        this.muted.setVisible(false);
        this.stats.add(this.muted);

        this.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent ke) { }

            @Override
            public void keyPressed(KeyEvent ke)
            {
                switch (ke.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        playerDirection = -5;
                        playerMove.start();
                        // movePlayer(-5);
                        break;

                    case KeyEvent.VK_RIGHT:
                        playerDirection = 5;
                        playerMove.start();
                        // movePlayer(5);
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
                        movePlayer(playerDirection);
                        playerMove.stop();
                        break;

                    case KeyEvent.VK_M:
                        if (music != null)
                            music.toggleMute();
                        if (ponged != null)
                            ponged.toggleMute();

                        muted.setVisible(!muted.isVisible());
                        break;
                }
            }
        });

        this.puckMovage = new Timer(40, al ->
        {
            puck.move();
            checkForCollission();
        });
        this.puckMovage.start();

        this.refresher = new Timer(60/1000, al ->
        {
            this.parent.repaint();
        });
        refresher.start();

        this.playerMove = new Timer(25, al ->
        {
            movePlayer(playerDirection);
        });

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

    private void setLines()
    {
        // start Vector will be in the middle and 10 pixels down
        int startX = (int) (this.playFieldSize.getWidth() / 2.);
        int startY = 10;

        // destination Vector will be near the ground and the sides
        int destX = 5;
        int destY = (int) (this.playFieldSize.getHeight()) - 10;
        left = new PongLine(startX, startY, destX, destY, PongLine.Position.LEFT);

        destX = (int) (this.playFieldSize.getWidth()) - 5;
        right = new PongLine(startX, startY, destX, destY, PongLine.Position.RIGHT);

        this.player = new PongLine (
                this.left.getX1() - 25 ,
                this.left.getY2(),
                this.left.getX1() + 25,
                this.left.getY2(),
                PongLine.Position.BOTTOM
        );
    }

    private void setPuck()
    {
        this.puck = new Puck();
        this.puck.setLocation(this.left.getX1(), this.left.getY2() / 2);
        this.puck.setDiameter(10);
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
            {
                this.puckMovage.stop();
                this.refresher.stop();
                if (this.music != null)
                    this.music.stop();
                new AfterGame(this.parent, this.score.getText());
            }
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
        DirectionVector lineVector = new DirectionVector(new double[]{line.getX2() - line.getX1(), line.getY2() - line.getY1()});
        DirectionVector puckVector = puck.getUnitVector();

        double angle = (Math.acos(puckVector.scalar(lineVector) / ((lineVector.length() * puckVector.length()))));
        while (angle < 2*Math.PI)
            angle += 2*Math.PI;
        while (angle > 2*Math.PI)
            angle -= 2*Math.PI;

        // System.out.println(Math.toDegrees(angle));

        if (line.position == PongLine.Position.RIGHT)
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

        // System.out.println(Math.toDegrees(angle));

        double newX;
        double newY;

        newX = (puckVector.getValue(0) * Math.cos(angle) - puckVector.getValue(1) * Math.sin(angle));// * -1;
        newY = (puckVector.getValue(0) * Math.sin(angle) + puckVector.getValue(1) * Math.cos(angle));// * -1;

        // System.out.println("X: " + newX);
        // System.out.println("Y: " + newY);

        puck.setUnitVector(newX, newY);

        if (this.ponged != null)
            this.ponged.play();

    }

}
