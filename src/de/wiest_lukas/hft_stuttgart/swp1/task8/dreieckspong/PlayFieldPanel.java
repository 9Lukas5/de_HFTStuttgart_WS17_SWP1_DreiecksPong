package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class PlayFieldPanel extends JPanel
{
    private final Line2D left;
    private final Line2D right;
    private final Puck puck;
    private final Line2D player;

    public PlayFieldPanel(Dimension playFieldSize, Line2D left, Line2D right, Puck puck, Line2D player)
    {
        this.left = left;
        this.right = right;
        this.puck = puck;
        this.player = player;

        initComponents(playFieldSize);
    }

    private void initComponents(Dimension playFieldSize)
    {
        this.setPreferredSize(playFieldSize);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics gc)
    {
        super.paintComponent(gc);
        Graphics2D g2d = (Graphics2D) gc;
        g2d.setColor(Color.GREEN);

        g2d.setStroke(new BasicStroke(3));
        g2d.draw(left);
        g2d.draw(right);
        g2d.draw(player);

        // draw the circle around the pucks point, instead of down and right from the puck
        g2d.fillOval(
                (int) puck.getX() - (puck.getDiameter() / 2),
                (int) puck.getY() - (puck.getDiameter() / 2),
                puck.getDiameter(),
                puck.getDiameter()
        );
    }
}
