package mazesimulator;

contextgroup Algorithm(Simulator sim) {
    subscriberTypes: Simulator, SimulatorView;
    
    activate RightHandRule if(sim.isRightHandRule());
    activate Tremaux if(sim.isTremaux());
}
