package general;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import gameobjects.Block;
import geometry.Point;
import geometry.Rectangle;

public class BlocksCreator implements BlockCreator {

    private int width;
    private int height;
    private int hitPoints;
    private List<String> fill;
    private Color stroke;

    public BlocksCreator(Map<String, String> block, List<String> fills){
        this.width = Integer.parseInt(block.get("width"));
        this.height = Integer.parseInt(block.get("height"));
        this.hitPoints = Integer.parseInt(block.get("hitPoints"));
        setStroke(block.get("stroke"));  
        this.fill = fills;
    }

    @Override
    public Block create(int xpos, int ypos) {
        Point point = new Point(xpos, ypos);
        return new Block(new Rectangle(point, width, height), hitPoints, stroke, fill);
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public Color setStroke(String color){
        ColorsParser colorParser = new ColorsParser();
        return this.stroke = colorParser.colorFromString(color);
    }
}
