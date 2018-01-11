/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.*;

/**
 *
 * @author lukas
 */
public class MainMenu extends javax.swing.JPanel
{

    private final JFrame parent;
    private JButton btnStartNewGame;
    private JButton btnExit;
    private JTextField height;
    private JTextField width;

    public MainMenu(JFrame parent)
    {
        initComponents();
        this.parent = parent;
        this.parent.getContentPane().removeAll();
        this.parent.getContentPane().add(this);
        this.parent.pack();
    }

    private void initComponents()
    {
        this.setLayout(new GridLayout(3, 2, 10, 0));

        this.btnStartNewGame = new JButton("neues Spiel starten");
        this.add(btnStartNewGame);

        this.btnExit = new JButton("Beenden");
        this.btnExit.addActionListener(e ->
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

        this.btnStartNewGame.addActionListener(e ->
        {
            InGame inGame;
            int fieldWidth = 0;
            int fieldHeight = 0;
            try
            {
                fieldWidth = Integer.valueOf(this.width.getText().replaceAll("\\.", ""));
                fieldHeight = Integer.valueOf(this.height.getText().replaceAll("\\.", ""));
            }
            catch (NumberFormatException ex)
            {
                fieldWidth = 400;
                fieldHeight = 300;
            }
            finally
            {
                new InGame(this.parent, new Dimension(fieldWidth, fieldHeight));
                //new Highscores(this.parent, "Lukas", 65);
            }
        });
    }
}
