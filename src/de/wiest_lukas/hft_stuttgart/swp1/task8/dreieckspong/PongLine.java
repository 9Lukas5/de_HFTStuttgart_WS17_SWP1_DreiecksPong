package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.geom.Line2D;

public class PongLine extends Line2D.Double
{
    private double puckDistanceCurrent = 0;
    private double puckDistanceBefore = 0;
    public final Inside inside;
    private final DirectionVector dVector;

    public enum Inside
    {
        LEFT,
        RIGHT,
    }

    public PongLine (double startX, double startY, double destX, double destY, Inside position)
    {
        super(startX, startY, destX, destY);
        this.inside = position;
        this.dVector = new DirectionVector(new double[] {destX - startX, destY - startY});
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

    public DirectionVector getDirectionVector()
    {
        return this.dVector;
    }
}
