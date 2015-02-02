import mazesimulator.*;
import java.awt.Color;

layer UnderDebugging {
    class Edge {
	public void setTraced() {
	    proceed();
	    color = Color.GREEN;
	    src.setTraced();
	    dst.setTraced();
	}
    }

    class Node {
	public void setTraced() {
	    proceed();
	    color = Color.GREEN;
	}
    }
}
