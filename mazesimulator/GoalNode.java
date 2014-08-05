package mazesimulator;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class GoalNode extends Node {
	
    public GoalNode(int x, int y) {
	super(x,y);
    }
	
    public void paint(Graphics2D g) {
	g.setColor(color);
	Ellipse2D ellipse = new Ellipse2D.Double(getRealX()-10, getRealY()-10, 20, 20);
	g.fill(ellipse);
    }
	
    public String toString() {
	return super.toString() + "_G";
    }
	
}
