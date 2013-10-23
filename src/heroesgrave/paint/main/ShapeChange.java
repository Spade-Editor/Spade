package heroesgrave.paint.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class ShapeChange implements Change {
    private Shape changeShape;
    private int colour;
    private Stroke stroke;
    
    public ShapeChange(Shape changeShape, int colour) {
        this.changeShape = changeShape;
        this.colour = colour;
    }

    public ShapeChange(Shape changeShape, int colour, Stroke stroke) {
        this.changeShape = changeShape;
        this.colour = colour;
        this.stroke = stroke;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        Graphics2D g2d = image.createGraphics();
        Color oldColor = g2d.getColor();
        g2d.setColor(new Color(colour));
        Stroke oldStroke = null;
        if(stroke != null) {
            oldStroke = g2d.getStroke();
            g2d.setStroke(stroke);
        }
        g2d.draw(changeShape);
        g2d.setColor(oldColor);
        if(stroke != null) {
            g2d.setStroke(oldStroke);
        }
        
        return image;
    }

    @Override
    public BufferedImage revert(BufferedImage image) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean samePos(int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

}
