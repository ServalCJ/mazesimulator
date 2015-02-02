import mazesimulator.*;
import javax.swing.JMenu;

layer EditingMaze {
    class SimulatorView {
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
}
