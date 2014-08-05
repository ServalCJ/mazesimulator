package mazesimulator;

global contextgroup MazeUI() {
    activate EditingMaze when
	active SimulatorView.startEditor until SimulatorView.startSolver;
    activate SolvingMaze when
	active SimulatorView.startSolver until SimulatorView.solved;
    activate RunningMaze when
    	active SimulatorView.solved until SimulatorView.never;
    activate Debugging when
	active SimulatorView.startDebug until SimulatorView.endDebug;
    context Printing cflow(call(void Simulator.printPath()));
    activate UnderDebugging when Debugging && Printing;
}
