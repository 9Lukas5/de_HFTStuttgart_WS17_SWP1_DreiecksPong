// (C)2018 Lukas Wiest
package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.*;
import static de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong.Aufgabe8_Dreieckspong.FRAME;

/**
 *
 * @author Lukas Wiest
 */
public class MainMenu extends javax.swing.JPanel
{
    private JButton btnStartNewGame;            // generates and switches to new InGame instance
    private JButton btnExit;                    // leaves program
    private JTextField height;                  // for input of playfield height
    private JTextField width;                   // and width

    public MainMenu()
    {
        initComponents();
        FRAME.getContentPane().removeAll();     // remove everything from JFrame
        FRAME.getContentPane().add(this);       // add ourselfs to the JFrame
        FRAME.pack();                           // make everyone get his preffered size
    }

    private void initComponents()
    {
        this.setLayout(new GridLayout(3, 2, 10, 0));

        this.btnStartNewGame = new JButton("neues Spiel starten");
        this.add(btnStartNewGame);

        this.btnExit = new JButton("Beenden");
        this.btnExit.addActionListener(al ->
        {
            System.exit(0);
        });
        this.add(this.btnExit);

        this.add(new JLabel("Breite:"));
        this.add(new JLabel("Höhe:"));
        this.add(width = new JFormattedTextField(NumberFormat.getIntegerInstance()));
        this.add(height = new JFormattedTextField(NumberFormat.getIntegerInstance()));
        this.width.setText("400");
        this.height.setText("300");

        this.btnStartNewGame.addActionListener(al ->
        {
            int fieldWidth = 0;                 // variable for playfield dimension creation
            int fieldHeight = 0;
            try                                 // try to set the vars from the textfields
            {
                fieldWidth = Integer.valueOf(this.width.getText().replaceAll("\\.", ""));
                fieldHeight = Integer.valueOf(this.height.getText().replaceAll("\\.", ""));
            }
            catch (NumberFormatException ex)    // if this failed, set the default again
            {
                fieldWidth = 400;
                fieldHeight = 300;
            }
            finally                             // create new InGame instance which leaves MainMenu
            {
                new InGame(new Dimension(fieldWidth, fieldHeight));
            }
        });
    }
}
