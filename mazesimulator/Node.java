package mazesimulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Node {
    private int x, y;
    protected Color color;
    private boolean traced = false;
    
    public Node(int x, int y) {
	this.x=x;
	this.y=y;
	this.color = Color.BLACK;
    }
	
    public int getX() { return x; }
    public int getY() { return y; }
	
    public int compareTo(Node n) {
	return x==n.getX()&&y==n.getY() ? 0 : -1;
    }
	
    public boolean equals(Object o) {
	if (o instanceof Node) {
	    Node n = (Node)o;
	    return x==n.getX()&&y==n.getY();
	} else {
	    return false;
	}
    }
	
    public boolean equals(int x, int y) {
	return this.x==x&&this.y==y;
    }
	
    public boolean isTraced() { return traced; }
    
    public void setTraced() {
	traced = true;
	color = Color.RED;
    }

    layer UnderDebugging {
	public void setTraced() {
	    proceed();
	    color = Color.GREEN;
	}
    }
	
    public void resetDebugColor() {
	if (traced) color = Color.RED;
	else color = Color.BLACK;
    }
	
    public void reset() {
	traced = false;
	color = Color.BLACK;
    }
	
    public void paint(Graphics2D g) {
	g.setColor(color);
	Ellipse2D ellipse = new Ellipse2D.Double(getRealX()-3, getRealY()-3, 6, 6);
	g.fill(ellipse);
    }
	
    public int getRealX() {
	return x*50 + 50;
    }
	
    public int getRealY() {
	return 500 - y*50 - 50;
    }
	
    public Node updateCurrentNode(Node n) {
	color = n.color;
	n.setCurrentNode();
	return n;
    }
	
    public void setCurrentNode() {
	color = Color.BLUE;
    }
	
    public String toString() {
	return "node_" + x + "_" + y;
    }
}
