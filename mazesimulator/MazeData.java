package mazesimulator;

import java.awt.Graphics2D;
import java.util.Vector;

public class MazeData {

    private Vector<Node> nodes = new Vector<Node>();
    private Vector<Edge> edges = new Vector<Edge>();
    private Node start;
    private int angle;
	
    public MazeData() { }
    
    public MazeData(Vector<Edge> edges, Node start, int angle) {
	this.edges = edges;
	this.start = start;
	this.angle = angle;
    }
	
    private Node searchNode(int x, int y) {
	for (Node n: nodes) {
	    if (n.getX() == x && n.getY() == y) return n;
	}
	return null;
    }
	
    public Vector<Edge> getEdges() { return edges; }
    public Node getStartNode() { return start; }
    public int getAngle() { return angle; }
    public Vector<Node> getNodes() { return nodes; }
    
    public void setStartNode(Node n) { start = n; }
    
    public void addNode(Node n) {
	nodes.add(n);
	Node n1 = searchNode(n.getX()-1, n.getY());
	if (n1 != null) edges.add(new Edge(n1, n));
	Node n2 = searchNode(n.getX(), n.getY()-1);
	if (n2 != null) edges.add(new Edge(n2,n));
	Node n3 = searchNode(n.getX()+1,n.getY());
	if (n3 != null) edges.add(new Edge(n, n3));
	Node n4 = searchNode(n.getX(), n.getY()+1);
	if (n4 != null) edges.add(new Edge(n, n4));
    }
    
    public boolean hasEdge(int x1, int y1, int x2, int y2) {
	for (Edge e: edges) {
	    Node src = e.getSrc();
	    Node dst = e.getDst();
	    if (src.getX() == x1 && src.getY() == y1 && dst.getX() == x2 && dst.getY() == y2) return true;
	    if (src.getX() == x2 && src.getY() == y2 && dst.getX() == x1 && dst.getY() == y1) return true;
	}
	return false;
    }
    
    public void removeEdge(int x1, int y1, int x2, int y2) {
	Vector<Edge> newEdges = new Vector<Edge>();
	for (Edge e: edges) {
	    Node src = e.getSrc();
	    Node dst = e.getDst();
	    if (src.getX() == x1 && src.getY() == y1 && dst.getX() == x2 && dst.getY() == y2) {
		continue;
	    }
	    if (src.getX() == x2 && src.getY() == y2 && dst.getX() == x1 && dst.getY() == y1) {
		continue;
	    }
	    newEdges.add(e);
	}
	edges = newEdges;
    }
    
    private Node searchNode(int x, int y, Node n) {
	for (Edge edge: edges) {
	    Node src = edge.getSrc();
	    Node dst = edge.getDst();
	    if (src.equals(x,y)&&dst.equals(n)) {
		edge.setTraced();
		return src;
	    }
	    if (dst.equals(x,y)&&src.equals(n)) {
		edge.setTraced();
		return dst;
	    }
	}
	return null;
    }
    
    public Node getNextNode(int angle, Node n) {
	int x = n.getX();
	int y = n.getY();
	if (angle == 0) { y = y+1; }
	else if (angle == 90) { x = x+1; }
	else if (angle == 180) { y = y-1; }
	else if (angle == 270) { x = x-1; }
	return searchNode(x,y,n);
    }
	
    public void inferAngle() {
	int x = start.getX();
	int y = start.getY();
	if (hasEdge(x,y,x,y+1)) angle = 0;
	else if (hasEdge(x,y,x+1,y)) angle = 90;
	else if (hasEdge(x,y,x,y-1)) angle = 180;
	else if (hasEdge(x,y,x-1,y)) angle = 270;
    }
	
    public void paint(Graphics2D g) {
	for (Node n: nodes) n.paint(g);
	for (Edge e: edges) e.paint(g);
    }
    
    public void resetEdges() {
	for (Edge e: edges) e.reset();
    }
    
    public void resetDebugColor() {
	for (Edge e: edges) e.resetDebugColor();
    }
	
}
