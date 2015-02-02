package mazesimulator;

global contextgroup MazeUI() {
    activate EditingMaze 
	from SimulatorView.startEditor to SimulatorView.startSolver;
    activate SolvingMaze 
	from SimulatorView.startSolver to SimulatorView.solved;
    activate RunningMaze 
    	from SimulatorView.solved to SimulatorView.never;
    activate Debugging 
	from SimulatorView.startDebug to SimulatorView.endDebug;
    context Printing is in cflow(call(void Simulator.printPath()));
    activate UnderDebugging when Debugging && when Printing;
}
