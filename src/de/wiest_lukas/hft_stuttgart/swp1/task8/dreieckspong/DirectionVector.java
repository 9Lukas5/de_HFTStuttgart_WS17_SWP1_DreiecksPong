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

    public void setVector(double[] values, boolean normalize)
    {
        if (values.length != this.values.length)
            throw new ArrayIndexOutOfBoundsException("mismatched dimensions");

        this.values = values;

        if (normalize)
            this.normalize();
    }

    public void normalize()
    {
        double length = 0;
        for (double tmp: this.values)
            length += tmp*tmp;

        length = Math.sqrt(length);

        for (int i=0; i < this.values.length; i++)
            this.values[i] = this.values[i]/length;
    }
}