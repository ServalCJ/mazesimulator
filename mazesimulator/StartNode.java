package mazesimulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class StartNode extends Node {
	
    public StartNode(int x, int y) {
	super(x,y);
	color = Color.RED;
    }
	
    public void paint(Graphics2D g) {
	g.setColor(color);
	Ellipse2D ellipse = new Ellipse2D.Double(getRealX()-3, getRealY()-3, 6, 6);
	g.fill(ellipse);
    }
	
    public String toString() {
	return super.toString() + "_S";
    }
	
}
