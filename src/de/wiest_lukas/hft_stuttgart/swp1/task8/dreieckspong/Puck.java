package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.geom.Point2D;

public class Puck extends Point2D.Double
{
    private int diameter;
    private DirectionVector unitVector;

    public Puck()
    {
        super();
        double xCoord = 3.;
        double YCoord = 1.;
        double unit = 1.;
        double length = Math.sqrt(xCoord * xCoord + YCoord * YCoord);
        unit /= length;
        this.unitVector = new DirectionVector(new double[] {unit * xCoord, unit * YCoord});
    }

    public void setDiameter(int diameter)
    {
        this.diameter = diameter;
    }

    public void setUnitVector(double x, double y)
    {
        double unit = 1.;
        double length = Math.sqrt(x * x + y * y);
        unit /= length;
        this.unitVector = new DirectionVector(new double[] {unit * x, unit * y});
    }

    public int getDiameter()
    {
        return this.diameter;
    }

    public DirectionVector getUnitVector()
    {
        return this.unitVector;
    }

    public void move()
    {
        this.setLocation(
                this.getX() + this.unitVector.getValue(0),
                this.getY() + this.unitVector.getValue(1)
        );
    }
}
