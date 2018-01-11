package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author Lukas Wiest
 */
public class Highscores extends JPanel
{
    private JFrame parent;
    private LinkedList<String> highscoresPlayers;
    private LinkedList<Integer> highscoresScores;

    public Highscores(JFrame parent, String player, int score)
    {
        this.parent = parent;
        this.highscoresPlayers = new LinkedList<>();
        this.highscoresScores = new LinkedList<>();
        this.setLayout(new BorderLayout());

        File highscoresFile = new File(System.getProperty("user.home")
                + "/"
                + Aufgabe8_Dreieckspong.baseFolder
                + "/highscores.conf");

        if (highscoresFile.exists())
        {
            try (BufferedReader buf = new BufferedReader(new FileReader(highscoresFile)))
            {
                String line;

                while ((line = buf.readLine()) != null)
                {
                    String[] split = line.split(":");
                    if (split.length != 2)
                        continue;
                    highscoresPlayers.add(split[0]);
                    try
                    {
                        highscoresScores.add(Integer.valueOf(split[1]));
                    }
                    catch (NumberFormatException e)
                    {
                        highscoresPlayers.remove(highscoresPlayers.size() -1);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                File parentFile = highscoresFile.getParentFile();
                parentFile.mkdirs();
                highscoresFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (player != null && player.equals(""))
            player = null;

        int place = -1;
        for (int i=0; i < this.highscoresPlayers.size(); i++)
        {
            if (player == null)
                break;

            if (score > this.highscoresScores.get(i))
            {
                this.highscoresPlayers.add(i, player);
                this.highscoresScores.add(i, score);
                place = i;
                break;
            }

            if(score <= this.highscoresScores.getLast())
            {
                this.highscoresPlayers.addLast(player);
                this.highscoresScores.addLast(score);
                place = this.highscoresScores.size() - 1;
                break;
            }
        }

        if(this.highscoresPlayers.size() == 0 && player != null)
        {
            this.highscoresPlayers.add(player);
            this.highscoresScores.add(score);
            place = 0;
        }

        try(FileWriter out = new FileWriter(highscoresFile, false))
        {
            for (int i=0; i < this.highscoresPlayers.size() && i < 10; i++)
            {
                out.write(this.highscoresPlayers.get(i));
                out.write(":");
                out.write(String.valueOf(this.highscoresScores.get(i)));
                out.write("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane();
        this.add(scrollPane, BorderLayout.CENTER);

        HighscoreTableModel tModel = new HighscoreTableModel();
        tModel.setPlayedGame(place);
        JTable table = new JTable(tModel);
        HighscoreTableCellRenderer cellRenderer = new HighscoreTableCellRenderer();
        table.setDefaultRenderer(String.class, cellRenderer);
        table.setDefaultRenderer(Integer.class, cellRenderer);

        String[] columnNames = new String[]
        {
            "Platz",
            "Spieler",
            "Punkte"
        };

        for (String column: columnNames)
            tModel.addColumn(column);

        for (int i=0; i < 10 && i < this.highscoresPlayers.size(); i++)
        {
            tModel.addRow(new Object[]
            {
                (i + 1),
                this.highscoresPlayers.get(i),
                this.highscoresScores.get(i)
            });
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(5);

        scrollPane.setViewportView(table);

        JLabel bottom = new JLabel("Dein Punktestand: " + score);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(bottom, BorderLayout.SOUTH);

        this.parent = parent;
        this.parent.getContentPane().removeAll();
        this.parent.getContentPane().add(this);
        this.parent.setSize(new Dimension(300, 235));
        this.parent.revalidate();
        this.parent.repaint();

    }

    private class HighscoreTableModel extends DefaultTableModel
    {
        private int playedGame = -1;

        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            Class column;
            switch (columnIndex)
            {
                case 0:
                case 2:
                    column = Integer.class;
                    break;

                default:
                    column = String.class;
            }

            return column;
        }

        public void setPlayedGame(int row)
        {
            this.playedGame = row;
        }

        public Color getRowColor(int row)
        {
            if (row == playedGame)
                return Color.green;
            else
                return Color.white;
        }
    }

    private class HighscoreTableCellRenderer extends DefaultTableCellRenderer
    {
        public HighscoreTableCellRenderer()
        {
            super();
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            HighscoreTableModel model = (HighscoreTableModel) table.getModel();
            c.setBackground(model.getRowColor(row));
            return c;
        }
    }
}
