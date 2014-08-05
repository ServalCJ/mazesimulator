package mazesimulator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MazeEditor implements MouseListener, ActionListener {

    private JPopupMenu popup, rem_edge_pop, finalize_pop;
    private JMenuItem enter_node, enter_start_node, enter_goal_node, rem_edge, finalize;
    private SimulatorView view;
    private int mazeX, mazeY;
    private int x1, y1, x2, y2;
    private boolean hasStart = false, hasGoal = false;
	
    public MazeEditor(SimulatorView view) {
	popup = new JPopupMenu();
	enter_node = new JMenuItem("Enter node");
	popup.add(enter_node);
	enter_start_node = new JMenuItem("Enter start node");
	popup.add(enter_start_node);
	enter_goal_node = new JMenuItem("Enter goal node");
	popup.add(enter_goal_node);
	enter_node.addActionListener(this);
	enter_start_node.addActionListener(this);
	enter_goal_node.addActionListener(this);
	
	rem_edge_pop = new JPopupMenu();
	rem_edge = new JMenuItem("Remove edge");
	rem_edge_pop.add(rem_edge);
	rem_edge.addActionListener(this);
	
	finalize_pop = new JPopupMenu();
	finalize = new JMenuItem("Finalize maze");
	finalize_pop.add(finalize);
	finalize.addActionListener(this);
		
	this.view = view;
    }
	
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) {
	int x = e.getX();
	int y = e.getY();
	if ((x%50 >= 48 || x%50 <= 2) && (y%50 >= 48 || y%50 <= 2)) {
	    mazeX = (x+2)/50 - 1;
	    mazeY = 9 - (y+2)/50;
	    JComponent c = (JComponent)e.getSource();
	    popup.show(c, x, y);
	} else if (x%50 >= 47 || x%50 <= 3) {
	    JComponent c = (JComponent)e.getSource();
	    x1 = (x+3)/50-1;
	    y2 = 9 - y/50;
	    y1 = y2 - 1;
	    x2 = x1;
	    if (view.hasEdge(x1, y1, x2, y2))
		rem_edge_pop.show(c, x, y);
	} else if (y%50 >= 47 || y%50 <= 3) {
	    JComponent c = (JComponent)e.getSource();
	    x1 = x/50 - 1;
	    y1 = 9 - (y+3)/50;
	    x2 = x1 + 1;
	    y2 = y1;
	    if (view.hasEdge(x1, y1, x2, y2))
		rem_edge_pop.show(c, x, y);
	} else if (hasStart && hasGoal) {
	    JComponent c = (JComponent)e.getSource();
	    finalize_pop.show(c, x, y);
	}
    }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
	
    public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if (src == enter_node) {
	    Node n = new Node(mazeX, mazeY);
	    view.addNode(n);
	} else if (src == enter_start_node) {
	    Node n = new StartNode(mazeX, mazeY);
	    view.addNode(n);
	    popup.remove(enter_start_node);
	    hasStart = true;
	} else if (src == enter_goal_node) {
	    Node n = new GoalNode(mazeX, mazeY);
	    view.addNode(n);
	    popup.remove(enter_goal_node);
	    hasGoal = true;
	} else if (src == rem_edge) {
	    view.removeEdge(x1,y1,x2,y2);
	} else if (src == finalize) {
	    view.setSimulatorFromMazeData();
	    view.changeMode();
	}
    }
	
    public void write(File file) {
	MazeData mazeData = view.getMazeData();
	Vector<Node> nodes = new Vector<Node>();
	for (Node n: mazeData.getNodes()) {
	    if (!nodes.contains(n)) nodes.add(n);
	}
	Vector<Edge> edges = new Vector<Edge>();
	for (Edge e: mazeData.getEdges()) {
	    Node src = e.getSrc();
	    Node dst = e.getDst();
	    if (!nodes.contains(src)) nodes.add(src);
	    if (!nodes.contains(dst)) nodes.add(dst);
	    if (!edges.contains(e)) edges.add(e);
	}
	mazeData.inferAngle();
	int angle = mazeData.getAngle();
	try {
	    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
	    pw.println("---nodes---");
	    for (Node n: nodes) {
		pw.println(n);
	    }
	    pw.println("---edges---");
	    for (Edge e: edges) {
		pw.println(e);
	    }
	    pw.println("---angle---");
	    pw.println(angle);
	    pw.close();
	} catch (IOException e) {
	    
	}
    }

}
