package ru.spbau.mit.circuit.model.point;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point getCenter(Point a, Point b) {
        if ((a.x + b.x) % 2 != 0 || (a.y + b.y) % 2 != 0) {
            throw new InvalidPointException("Float coordinate value");
        }
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int distance(Point other) {
        return (int) Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) *
                (this.y - other.y));
    }

    public int distance(float x, float y) {
        return (int) Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    public boolean isInSquare(float x, float y, float dist) {
        return Math.abs(this.x - x) < dist && Math.abs(this.y - y) < dist;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return p.x == x && p.y == y;
        }
        return false;
    }
}
