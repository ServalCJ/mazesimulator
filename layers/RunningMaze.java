import mazesimulator.*;
import javax.swing.JMenu;

layer RunningMaze {
    class Simulator {
	public void run() {
	    for (int i=0; i<pathLength; i++) {
		if (sleepIfInstructed()) continue;
		followSegment();
		angle = turn(path[i],angle);
		view.repaint();
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) { }
	    }
	    simulatorThread = null;
	    tmp = null;
	}
    }

    class SimulatorView {
	public void setMenuBar() {
	    proceed();
	    JMenu maze = new JMenu("Maze");
	    maze.add(start_menu);
	    maze.add(stop_menu);
	    maze.add(rerun_menu);
	    maze.add(reset_menu);
	    menuBar.add(maze);
	    menuBar.revalidate();
	}

	public void setButtons() {
	    proceed();
	    buttons.add(start);
	    buttons.add(stop);
	    buttons.add(rerun);
	    buttons.add(reset);
	    buttons.revalidate();
	}
    }
}
