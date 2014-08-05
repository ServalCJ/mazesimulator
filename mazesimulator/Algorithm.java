package mazesimulator;

contextgroup Algorithm(Simulator sim) {
    subscriberTypes: Simulator, SimulatorView;
    
    activate RightHandRule when if(sim.isRightHandRule());
    activate Tremaux when if(sim.isTremaux());
}
