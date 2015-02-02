import mazesimulator.*;

layer RightHandRule {
    class Simulator {
	public int selectTurn() {
	    if (mazeData.getNextNode((angle+90)%360, currentNode)!=null) {
		return 0;
	    } else if (mazeData.getNextNode((angle)%360, currentNode)!=null) {
		return 1;
	    } else if (mazeData.getNextNode((angle+270)%360, currentNode)!=null) {
		return 2;
	    } else {
		return 3;
	    }
	}
    }

    class SimulatorView {
	public void setEnabledAlgorithm() {
	    proceed();
	    tremaux.setEnabled(true);
	    lefthand.setEnabled(false);
	    righthand.setEnabled(true);
	}
    }
}
