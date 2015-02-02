import mazesimulator.*;
import javax.swing.JMenu;

layer SolvingMaze {
    class Simulator {
	public void run() {
	    int count = 0;
	    while (!(currentNode instanceof GoalNode)) {
		if (reset) return;
		if (sleepIfInstructed()) continue;
		followSegment();
		printPath();
		view.repaint();
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) { }
		turnAndSimplify();
		count++;
	    }
	    System.out.println("count: " + count);
	    mazeSolved();
	    simulatorThread = null;
	    tmp = null;
	    view.endDebug();
	    view.solved();
	    view.setRerunnable();
	}
    }

    class SimulatorView {
	public void setMenuBar() {
	    proceed();
	    JMenu maze = new JMenu("Maze");
	    maze.add(start_menu);
	    maze.add(stop_menu);
	    maze.add(reset_menu);
	    maze.add(debug_menu);
	    menuBar.add(maze);
	    JMenu algorithm = new JMenu("Algorighm");
	    algorithm.add(lefthand);
	    algorithm.add(righthand);
	    algorithm.add(tremaux);
	    setEnabledAlgorithm();
	    menuBar.add(algorithm);
	    menuBar.revalidate();
	}

	public void setButtons() {
	    proceed();
	    buttons.add(start);
	    buttons.add(stop);
	    buttons.add(reset);
	    buttons.add(debug);
	    buttons.revalidate();
	}

    }
}
