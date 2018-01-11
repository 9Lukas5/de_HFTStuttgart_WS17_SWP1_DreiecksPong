package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import javax.swing.JFrame;

/**
 *
 * @author Lukas Wiest
 */
public class Aufgabe8_Dreieckspong
{
    protected static final String baseFolder = ".62wilu1bif_SWP1_DreiecksPong";

    public static void main(String[] args)
    {
        JFrame frame;

        frame = new JFrame();
        frame.setTitle("SWP1 Dreieckspong - (c) Lukas Wiest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(200, 400);
        frame.setResizable(false);
        frame.setVisible(true);

        new MainMenu(frame);
    }
}
