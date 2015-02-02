package mazesimulator;

import java.awt.Graphics2D;

import java.util.Vector;

public class Simulator implements Runnable {
    private MazeData mazeData;
    private Node currentNode;
    private int angle;
    private int[] path = new int[100];
    private int pathLength = 0;
    private Node tmpForFoundLoop = null;
    private SimulatorView view = null;
    private Thread simulatorThread = null;
    private Thread tmp = null;
    private boolean reset = false;
    private StringBuffer debugText = new StringBuffer();
    private boolean tremaux = false;
    private boolean left = false;

    public Vector<Edge> getMaze() { return mazeData.getEdges(); }
    public void setView(SimulatorView view) { this.view = view; }
    
    public Simulator(MazeData mazeData) {
	currentNode = mazeData.getStartNode();
	angle = mazeData.getAngle();
	this.mazeData = mazeData;
	
	simulatorThread = new Thread(this);
	tmp = simulatorThread;
    }
    
    public void paintMaze(Graphics2D g) {
	for (Edge e: mazeData.getEdges()) {
	    e.paint(g);
	}
    }
    
    public void setTremaux() { tremaux = true; }
    public void unsetTremaux() { tremaux = false; }
    public void setLeftHandRule() { left = true; }
    public void setRightHandRule() { left = false; }
    
    public boolean isTremaux() { return tremaux; }
    public boolean isLeftHandRule() { return left; }
    public boolean isRightHandRule() { return !left; }
    
    private int turn(int direction, int angle) {
	if (direction == 2) { return (angle+270)%360; }
	else if (direction == 0) { return (angle+90)%360; }
	else if (direction == 3) { return (angle+180)%360; }
	else return angle;
    }
    
    public Node getNextNode() {
	Node next = mazeData.getNextNode(angle, currentNode);
	next.setTraced();
	return next;
    }

    private void followSegment() {
	currentNode = currentNode.updateCurrentNode(getNextNode());
	if (currentNode == null) {
	    System.err.println("out of maze");
	    System.exit(-1);
	}
    }

    public int selectTurn() {
	if (mazeData.getNextNode((angle+270)%360, currentNode)!=null) {
	    return 2;
	} else if (mazeData.getNextNode((angle)%360, currentNode)!=null) {
	    return 1;
	} else if (mazeData.getNextNode((angle+90)%360, currentNode)!=null) {
	    return 0;
	} else {
	    return 3;
	}
    }
    
    public void simplify() {
	if (pathLength < 3 || path[pathLength-2] != 3) {
	    return;
	}
	
	int totalAngle = 0;
	for (int i=1; i<=3; i++) {
	    switch(path[pathLength-i]) {
	    case 0:
		totalAngle += 90;
		break;
	    case 2:
		totalAngle += 270;
		break;
	    case 3:
		totalAngle += 180;
		break;
	    case 1:
		break;
	    }
	}
		
	totalAngle = totalAngle % 360;
	
	switch(totalAngle) {
	case 0:
	    path[pathLength - 3] = 1;
	    break;
	case 90:
	    path[pathLength - 3] = 0;
	    break;
	case 180:
	    path[pathLength - 3] = 3;
	    break;
	case 270:
	    path[pathLength - 3] = 2;
	    break;
	}
	
	pathLength = pathLength - 2;
    }
    
    private boolean sleepIfInstructed() {
	if (simulatorThread == null) {
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) { }
	    return true;
	} else {
	    return false;
	}
    }

    public void mazeSolved() { }

    public void run() { }

    public void printPath() { }

    public void turnAndSimplify() {
	int direction = selectTurn();
	path[pathLength] = direction;
	pathLength++;
	angle = turn(direction,angle);
	simplify();
    }
    
    public void start() {
	if (simulatorThread == null) {
	    simulatorThread = tmp;
	}
	if (simulatorThread != null && !simulatorThread.isAlive()) {
	    simulatorThread.start();
	}
    }
	
    public void stop() {
	simulatorThread = null;
    }
	
    public void rerun() {
	simulatorThread = new Thread(this);
	tmp = simulatorThread;
	mazeData.resetEdges();
	angle = mazeData.getAngle();
	view.repaint();
	currentNode = mazeData.getStartNode();
	simulatorThread.start();
    }
    
    public void reset() {
	if (simulatorThread != null) {
	    reset = true;
	    try {
		simulatorThread.join();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	reset = false;
	simulatorThread = new Thread(this);
	tmp = simulatorThread;
	mazeData.resetEdges();
	view.repaint();
	currentNode = mazeData.getStartNode();
	mazeData.inferAngle();
	angle = mazeData.getAngle();
	path = new int[100];
	pathLength = 0;
	tmpForFoundLoop = null;
	view.setDebugArea("");
	debugText = new StringBuffer();
    }
    
    public void resetDebug() {
	mazeData.resetDebugColor();
    }

}
