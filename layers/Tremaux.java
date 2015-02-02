import mazesimulator.*;

layer Tremaux {
    class Simulator {
	private boolean back = false;
	private boolean foundLoop = false;

	public Node getNextNode() {
	    if (foundLoop) {
		foundLoop = false;
		back = true;
		path[pathLength] = 3;
		pathLength++;
		angle = (angle+180)%360;
		return tmpForFoundLoop;
	    }
	    
	    Node next = mazeData.getNextNode(angle, currentNode);
	    if (next == null) return null;
	    
	    if (!next.isTraced()) {
		next.setTraced();
		if (back) back = false;
		return next;
	    } else if (back) {
		return next;
	    } else {
		foundLoop = true;
		tmpForFoundLoop = currentNode;
		return next;
	    }
	}

	public int selectTurn() {
	    int dir = proceed();
	    if (dir == 3) {
		back = true;
	    }
	    return dir;
	}

	public void simplify() {
	    proceed();
	    if (path[pathLength - 1] != 3) back = false;
	}

	public void turnAndSimplify() {
	    if (!foundLoop) {
		proceed();
	    }
	}
    }

    class SimulatorView {
	public void setEnabledAlgorithm() {
	    proceed();
	    tremaux.setEnabled(false);
	    lefthand.setEnabled(true);
	    righthand.setEnabled(true);
	}
    }
}
