package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

import java.awt.geom.Point2D;

public class Puck extends Point2D.Double
{
    private int diameter;
    private DirectionVector unitVector;

    public Puck()
    {
        super();
        this.unitVector = new DirectionVector(2);
        this.unitVector.setVector(new double[] {Math.random(), Math.random()}, true);
    }

    public void setDiameter(int diameter)
    {
        this.diameter = diameter;
    }

    public void setUnitVector(double x, double y)
    {
        this.unitVector.setVector(new double[] {x, y}, true);
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
