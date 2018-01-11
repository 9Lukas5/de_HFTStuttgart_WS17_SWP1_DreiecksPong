package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author Lukas Wiest
 */
public class AfterGame extends JPanel
{
    private JFrame parent;
    private JTextField player;
    private JTextField score;
    private JButton next;
    private JButton skip;

    public AfterGame (JFrame parent, String score)
    {
        this.parent = parent;
        initComponents(score);
        this.parent = parent;
        this.parent.getContentPane().removeAll();
        this.parent.getContentPane().add(this);
        this.parent.pack();
        this.parent.setFocusable(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void initComponents(String score)
    {
        this.setLayout(new GridLayout(2, 4, 0, 0));

        JLabel tmp = new JLabel("Spieler");
        tmp.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(tmp);
        tmp = new JLabel("Punkte");
        tmp.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(tmp);
        this.add(new JLabel());
        this.add(new JLabel());

        this.player = new JTextField();
        this.player.setBackground(Color.white);
        this.player.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.player);

        this.score = new JTextField(score);
        this.score.setBorder(null);
        this.score.setFocusable(false);
        this.score.setEditable(false);
        this.score.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.score);

        this.next = new JButton("Speichern");
        this.next.addActionListener(al ->
        {
            new Highscores(this.parent, this.player.getText(), Integer.valueOf(score));
        });
        this.add(this.next);

        this.skip = new JButton("Ueberspringen");
        this.skip.addActionListener(al ->
        {
            new Highscores(this.parent, null, Integer.valueOf(score));
        });
        this.add(this.skip);

    }
}
