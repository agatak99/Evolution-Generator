package agh.po.Properties;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d
{
    public final int x;
    public final int y;

    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return String.format("(%d,%d)", this.x, this.y);

    }

    public boolean precedes(Vector2d other)

    {
        if(this.x<=other.x && this.y<=other.y) return true;
        else return false;
    }

    public  boolean follows(Vector2d other)
    {
        if (this.x>=other.x && this.y>=other.y) return true;
        else return false;
    }


    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x+other.x, this.y+other.y);
    }


    @Override
    public boolean equals(Object other)
    {
        if(this==other)
            return true;
        if(!(other instanceof Vector2d))
            return false;
        Vector2d that=(Vector2d) other;
        return (this.x==that.x && this.y==that.y);
    }

    @Override
    public int hashCode(){
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
