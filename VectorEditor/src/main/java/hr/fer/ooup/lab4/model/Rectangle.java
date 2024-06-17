package hr.fer.ooup.lab4.model;

public class Rectangle {
    private int x; //x koordinata gornjeg lijevog kuta
    private int y; //y koordinara gornjeg lijevog kuta
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    };

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
         return width;
    }

    public int getHeight() {
        return height;
    }
}
