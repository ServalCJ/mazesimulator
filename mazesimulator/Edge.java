package mazesimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Edge {
    private Node src, dst;
    private Color color = Color.BLACK;
    private boolean traced = false;
    
    public Edge(Node src, Node dst) {
	this.src = src;
	this.dst = dst;
    }
    public Node getSrc() { return src; }
    public Node getDst() { return dst; }

    public void setTraced() {
	traced = true;
	color = Color.RED;
    }

    layer UnderDebugging {
	public void setTraced() {
	    proceed();
	    color = Color.GREEN;
	    src.setTraced();
	    dst.setTraced();
	}
    }

    public boolean equals(Object o) {
	if (o instanceof Edge) {
	    Edge e = (Edge)o;
	    return e.getSrc().equals(src) && e.getDst().equals(dst) ?
		e.getSrc().equals(dst) && e.getDst().equals(src) : false;
	} else {
	    return false;
	}
    }
    
    public void resetDebugColor() {
	if (traced) color = Color.RED;
	else color = Color.BLACK;
	src.resetDebugColor();
	dst.resetDebugColor();
    }
    
    public void reset() {
	traced = false;
	color = Color.BLACK;
	src.reset();
	dst.reset();
    }
    
    public void paint(Graphics2D g) {
	float dash[] = {10.0f, 0.0f};
	BasicStroke edgeStroke = new BasicStroke(6.0f,
						 BasicStroke.CAP_BUTT,
						 BasicStroke.JOIN_MITER,
						 10.f,
						 dash,
						 0.0f);
	g.setStroke(edgeStroke);
	g.setColor(color);
	g.draw(new Line2D.Double(src.getRealX(), src.getRealY(), dst.getRealX(), dst.getRealY()));
	src.paint(g);
	dst.paint(g);
    }
	
    public String toString() {
	return src + "," + dst;
    }
}
