import mazesimulator.*;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JScrollPane;

layer Debugging {
    class Simulator {
	public void printPath() {
	    Node n = mazeData.getStartNode();
	    int angle = mazeData.getAngle();
	    mazeData.resetDebugColor();
	    for (int i=0; i<pathLength; i++) {
		n = n.updateCurrentNode(mazeData.getNextNode(angle, n));
		n.setTraced();
		angle = turn(path[i],angle);
		debugText.append(decodePath(path[i]));
	    }
	    n = n.updateCurrentNode(mazeData.getNextNode(angle, n));
	    debugText.append("\n");
	    view.setDebugArea(debugText.toString());
	}

	private String decodePath(int path) {
	    if (path == 0) return "R";
	    else if (path == 1) return "S";
	    else if (path == 2) return "L";
	    else if (path == 3) return "B";
	    else return "";
	}
    }

    class SimulatorView {
	deactivate {
	    if (simulator != null) simulator.resetDebug();
	}
	
	public void setToolPane() {
	    proceed();
	    JScrollPane scrollPane = new JScrollPane(debugArea);
	    toolPane.add(scrollPane, BorderLayout.CENTER);
	    toolPane.revalidate();
	}

	public void setButtons() {
	    buttons.removeAll();
	    buttons.add(start);
	    buttons.add(stop);
	    buttons.add(reset);
	    buttons.add(stop_debug);
	    buttons.revalidate();
	}

	public void setMenuBar() {
	    menuBar.removeAll();
	    JMenu maze = new JMenu("Maze");
	    maze.add(start_menu);
	    maze.add(stop_menu);
	    maze.add(reset_menu);
	    maze.add(stop_debug_menu);
	    menuBar.add(maze);
	    JMenu algorithm = new JMenu("Algorighm");
	    algorithm.add(lefthand);
	    algorithm.add(righthand);
	    algorithm.add(tremaux);
	    setEnabledAlgorithm();
	    menuBar.add(algorithm);
	    menuBar.revalidate();
	}
    }
}
