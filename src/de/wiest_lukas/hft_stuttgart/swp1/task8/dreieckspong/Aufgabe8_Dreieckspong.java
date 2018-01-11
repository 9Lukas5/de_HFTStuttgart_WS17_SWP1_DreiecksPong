// (C)2018 Lukas Wiest
package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import javax.swing.JFrame;

/**
 *
 * @author Lukas Wiest
 */
public class Aufgabe8_Dreieckspong
{
    // name of programs application data folder
    protected static final String BASEFOLDER = ".62wilu1bif_SWP1_DreiecksPong";

    protected static final JFrame FRAME = new JFrame();

    public static void main(String[] args)
    {
        FRAME.setTitle("SWP1 Dreieckspong - (c) Lukas Wiest");
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setLocation(200, 400);
        FRAME.setResizable(false);
        FRAME.setVisible(true);

        // generate new MainMenu (this automatically removes all contents from FRAME and adds
        // the new instance to the contentPane
        new MainMenu();
    }
}
