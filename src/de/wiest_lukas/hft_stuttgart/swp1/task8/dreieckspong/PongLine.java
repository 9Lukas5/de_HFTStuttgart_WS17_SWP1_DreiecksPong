package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.geom.Line2D;

public class PongLine extends Line2D.Double
{
    private double puckDistanceCurrent = 0;
    private double puckDistanceBefore = 0;
    public final Position position;

    public enum Position
    {
        LEFT,
        RIGHT,
        BOTTOM;
    }

    public PongLine (double startX, double startY, double destX, double destY, Position position)
    {
        super(startX, startY, destX, destY);
        this.position = position;
    }

    public void setNewPuckDistance(double distance)
    {
        this.puckDistanceBefore = this.puckDistanceCurrent;
        this.puckDistanceCurrent = distance;
    }

    public boolean movesToMe()
    {
        return this.puckDistanceBefore > this.puckDistanceCurrent;
    }
}
