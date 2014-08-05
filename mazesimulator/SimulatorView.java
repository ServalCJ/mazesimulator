package mazesimulator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SimulatorView extends JFrame implements ActionListener {
	
    private Simulator simulator;
    private JButton start, stop, rerun, reset, debug, stop_debug;
    private JMenuBar menuBar;
    private JPanel toolPane, buttons;
    private JMenuItem start_menu, stop_menu, rerun_menu, reset_menu, debug_menu, stop_debug_menu, new_menu, open, save, close;
    private JMenuItem lefthand, righthand, tremaux;
    private MazeData mazeData = new MazeData();
    private JTextArea debugArea = new JTextArea();
    private MazeEditor editor;
    private Algorithm algo;

    eventDecl startEditor;
    eventDecl startSolver;
    eventDecl solved;
    eventDecl startDebug;
    eventDecl endDebug;

    public void addNode(Node n) {
	mazeData.addNode(n);
	if (n instanceof StartNode) mazeData.setStartNode(n);
	repaint();
    }
	
    public boolean hasEdge(int x1, int y1, int x2, int y2) {
	return mazeData.hasEdge(x1,y1,x2,y2);
    }
	
    public void removeEdge(int x1, int y1, int x2, int y2) {
	mazeData.removeEdge(x1,y1,x2,y2);
	repaint();
    }
	
    public MazeData getMazeData() { return mazeData; }
	
    public void setSimulatorFromMazeData() {
	mazeData.inferAngle();
	simulator = new Simulator(mazeData);
	simulator.setView(this);
	algo = new Algorithm(simulator);
	algo.subscribe(simulator);
	algo.subscribe(this);
    }
	
    public void setDebugArea(String s) {
	debugArea.setText(s);
	int length = debugArea.getText().length();
	debugArea.setCaretPosition(length);
    }
	
    private SimulatorView () { }
	
    private class MazePanel extends JPanel {
	public void paint(Graphics g) {
	    super.paint(g);
	    Graphics2D g2 = (Graphics2D)g;
	    float dash[] = {2.0f, 2.0f};
	    BasicStroke dashStroke = new BasicStroke(0.0f,
						     BasicStroke.CAP_BUTT,
						     BasicStroke.JOIN_MITER,
						     10.0f,
						     dash,
						     0.0f);
	    g2.setStroke(dashStroke);
	    g2.draw(new Line2D.Double(50,0,50,500));
	    g2.draw(new Line2D.Double(100,0,100,500));
	    g2.draw(new Line2D.Double(150,0,150,500));
	    g2.draw(new Line2D.Double(200,0,200,500));
	    g2.draw(new Line2D.Double(250,0,250,500));
	    g2.draw(new Line2D.Double(300,0,300,500));
	    g2.draw(new Line2D.Double(350,0,350,500));
	    g2.draw(new Line2D.Double(400,0,400,500));
	    g2.draw(new Line2D.Double(450,0,450,500));
			
	    g2.draw(new Line2D.Double(0,50,500,50));
	    g2.draw(new Line2D.Double(0,100,500,100));
	    g2.draw(new Line2D.Double(0,150,500,150));
	    g2.draw(new Line2D.Double(0,200,500,200));
	    g2.draw(new Line2D.Double(0,250,500,250));
	    g2.draw(new Line2D.Double(0,300,500,300));
	    g2.draw(new Line2D.Double(0,350,500,350));
	    g2.draw(new Line2D.Double(0,400,500,400));
	    g2.draw(new Line2D.Double(0,450,500,450));
	    
	    if (simulator != null) {
		simulator.paintMaze(g2);
	    } else {
		mazeData.paint(g2);
	    }
	}
    }
	
    public void setRerunnable() {
	setMenuBar();
	setButtons();
	setToolPane();
    }
	
    public void changeDebugMode() {
	setMenuBar();
	setToolPane();
    }
	
    public void changeMode() {
	startSolver();
	setMenuBar();
	setToolPane();
    }
	
    public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if (src == start || src == start_menu) {
	    if (simulator != null) simulator.start();
	} else if (src == stop || src == stop_menu) {
	    if (simulator != null) simulator.stop();
	} else if (src == rerun || src == rerun_menu) {
	    if (simulator != null) simulator.rerun();
	} else if (src == reset || src == reset_menu) {
	    if (simulator != null) simulator.reset();
	    setMenuBar();
	    setButtons();
	    setToolPane();
	} else if (src == debug || src == debug_menu) {
	    startDebug();
	    changeDebugMode();
	} else if (src == stop_debug || src == stop_debug_menu) {
	    endDebug();
	    changeDebugMode();
	} else if (src == new_menu) {
	    simulator = null;
	    mazeData = new MazeData();
	    setDebugArea("");
	    repaint();
	} else if (src == open) {
	    JFileChooser chooser = new JFileChooser();
	    int selected = chooser.showOpenDialog(this);
	    if (selected == JFileChooser.APPROVE_OPTION) {
		File file = chooser.getSelectedFile();
		MazeFileReader reader = new MazeFileReader(file);
		MazeData data = reader.readMazeFile();
		if (data.getStartNode() == null) {
		    System.err.println("Start node not initialized!");
		    return;
		}
		mazeData = data;
		simulator = new Simulator(data);
		simulator.setView(this);
		algo = new Algorithm(simulator);
		algo.subscribe(simulator);
		algo.subscribe(this);
		changeMode();
		repaint();
	    }
	} else if (src == save) {
	    JFileChooser chooser = new JFileChooser();
	    int selected = chooser.showOpenDialog(this);
	    if (selected == JFileChooser.APPROVE_OPTION) {
		File file = chooser.getSelectedFile();
		editor.write(file);
	    }
	} else if (src == close) {
	    System.exit(0);
	} else if (src == lefthand) {
	    if (simulator != null) {
		simulator.setLeftHandRule();
		simulator.unsetTremaux();
	    }
	    setMenuBar();
	} else if (src == righthand) {
	    if (simulator != null) {
		simulator.setRightHandRule();
		simulator.unsetTremaux();
	    }
	    setMenuBar();
	} else if (src == tremaux) {
	    if (simulator != null) {
		simulator.setLeftHandRule();
		simulator.setTremaux();
	    }
	    setMenuBar();
	}
    }

    public void setMenuBar() {
	menuBar.removeAll();
    }
	
    public void setButtons() {
	buttons.removeAll();
    }
    
    public void setToolPane() {
	toolPane.removeAll();
	setButtons();
	toolPane.add(buttons, BorderLayout.NORTH);
	toolPane.revalidate();
    }
    
    layer EditingMaze {
	public void setMenuBar() {
	    proceed();
	    JMenu file = new JMenu("File");
	    file.add(new_menu);
	    file.add(open);
	    file.add(save);
	    file.add(close);
	    menuBar.add(file);
	    menuBar.revalidate();
	}
    }

    layer RunningMaze {
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

    public void setEnabledAlgorithm() {
	if (simulator == null) {
	    lefthand.setEnabled(false);
	    righthand.setEnabled(false);
	    tremaux.setEnabled(false);
	} else {
	    lefthand.setEnabled(false);
	}
    }


    layer Tremaux {
	public void setEnabledAlgorithm() {
	    proceed();
	    tremaux.setEnabled(false);
	    lefthand.setEnabled(true);
	    righthand.setEnabled(true);
	}
    }

    layer RightHandRule {
	public void setEnabledAlgorithm() {
	    proceed();
	    tremaux.setEnabled(true);
	    lefthand.setEnabled(false);
	    righthand.setEnabled(true);
	}
    }


    layer SolvingMaze {
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

    layer Debugging {
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
	
    private void initialize() {
	setTitle("Maze Simulator");
	setSize(700,550);
	setBackground(Color.WHITE);
	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Container p = getContentPane();
	
	startEditor();
	menuBar = new JMenuBar();
	
	new_menu = new JMenuItem("New");
	new_menu.addActionListener(this);
	open = new JMenuItem("Open");
	open.addActionListener(this);
	save = new JMenuItem("Save");
	save.addActionListener(this);
	close = new JMenuItem("Close");
	close.addActionListener(this);
	start_menu = new JMenuItem("Start");
	start_menu.addActionListener(this);
	stop_menu = new JMenuItem("Stop");
	stop_menu.addActionListener(this);
	rerun_menu = new JMenuItem("Rerun");
	rerun_menu.addActionListener(this);
	reset_menu = new JMenuItem("Reset");
	reset_menu.addActionListener(this);
	debug_menu = new JMenuItem("Debug");
	debug_menu.addActionListener(this);
	stop_debug_menu = new JMenuItem("End Debug");
	stop_debug_menu.addActionListener(this);
	lefthand = new JMenuItem("Left-hand rule");
	lefthand.addActionListener(this);
	righthand = new JMenuItem("Right-hand rule");
	righthand.addActionListener(this);
	tremaux = new JMenuItem("Tremaux");
	tremaux.addActionListener(this);
	setMenuBar();
	setJMenuBar(menuBar);
	
	MazePanel mazePanel = new MazePanel();
	editor = new MazeEditor(this);
	mazePanel.addMouseListener(editor);
	p.add(mazePanel, BorderLayout.CENTER);
	
	toolPane = new JPanel();
	toolPane.setLayout(new BorderLayout());
	buttons = new JPanel();
	start = new JButton("START!");
	stop = new JButton("STOP");
	rerun = new JButton("RERUN");
	reset = new JButton("RESET");
	debug = new JButton("DEBUG");
	stop_debug = new JButton("ST.DEBUG");
	start.addActionListener(this);
	stop.addActionListener(this);
	rerun.addActionListener(this);
	reset.addActionListener(this);
	debug.addActionListener(this);
	stop_debug.addActionListener(this);
	
	setToolPane();
	
	p.add(toolPane, BorderLayout.EAST);
    }
    
    public static void main(String[] args) {
	SimulatorView view = new SimulatorView();
	view.initialize();
	view.setVisible(true);
    }

}
