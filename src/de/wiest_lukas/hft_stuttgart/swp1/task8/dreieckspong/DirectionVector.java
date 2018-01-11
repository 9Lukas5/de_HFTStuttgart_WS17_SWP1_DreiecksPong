package de.wiest_lukas.hft_stuttgart.swp1.task8.dreieckspong;

public class DirectionVector
{
    private double[] values;

    public DirectionVector(int dimensions)
    {
        this.values = new double[dimensions];
    }

    public DirectionVector(double[] values)
    {
        this.values = values;
    }

    public double scalar(DirectionVector vc)
    {
        double toReturn = 0;

        for (int i=0; i < this.values.length; i++)
        {
            toReturn += this.values[i] * vc.values[i];
        }

        return toReturn;
    }

    public double length()
    {
        double toReturn = 0;

        for (double value: this.values)
        {
            toReturn += Math.pow(value, 2);
        }

        return Math.sqrt(toReturn);
    }

    public double getValue(int dimension)
    {
        return this.values[dimension];
    }
}