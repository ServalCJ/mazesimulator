package mazesimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class MazeFileReader {
	
    private Vector<Edge> mazeData;
    private int mode;
    private HashMap<String,Node> nodes;
    private Node start = null;
    private int angle;
    private File file;
    
    class MazeReaderException extends Exception { }
	
    public MazeFileReader(File file) {
	mazeData = new Vector<Edge>();
	nodes = new HashMap<String,Node>();
	mode = 3;
	this.file = file;
    }
	
    private void processLine(String str) throws MazeReaderException {
	String[] splitted = null;
	try {
	    switch (mode) {
	    case 0:
		splitted = str.split("_");
		Node n = null;
		int x = Integer.valueOf(splitted[1]);
		int y = Integer.valueOf(splitted[2]);
		if (splitted.length > 3) {
		    if (splitted[3].equals("S")) {
			n = new StartNode(x,y);
			start = n;
		    } else if (splitted[3].equals("G")) {
			n = new GoalNode(x,y);
		    } else {
			n = new Node(x,y);
		    }
		} else {
		    n = new Node(x,y);
		}
		nodes.put(str, n);
		break;
	    case 1:
		splitted = str.split(",");
		Node n1 = nodes.get(splitted[0]);
		Node n2 = nodes.get(splitted[1]);
		if (n1 != null && n2 != null) {
		    Edge e = new Edge(n1,n2);
		    mazeData.add(e);
		}
		break;
	    case 2:
		angle = Integer.valueOf(str);
		break;
	    case 3: break;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new MazeReaderException();
	}
    }

    public MazeData readMazeFile() {
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(file));
	} catch (FileNotFoundException e) {
	    System.err.println("File " + file.getName() + " is not found.");
	    return null;
	}
	try {
	    String str = "";
	    while (true) {
		str = br.readLine();
		if (str == null) break;
		if (str.equals("")) continue;
		else if (str.equals("---nodes---")) {
		    mode = 0;
		    continue;
		} else if (str.equals("---edges---")) {
		    mode = 1;
		    continue;
		} else if (str.equals("---angle---")) {
		    mode = 2;
		    continue;
		}
		processLine(str);
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (MazeReaderException e) {
	    System.err.println("Unsupported file: " + file.getName());
	    return null;
	}
	if (start == null) {
	    System.err.println("Start node not initialized!");
	    return null;
	}
	return new MazeData(mazeData, start, angle);
    }
	
}
