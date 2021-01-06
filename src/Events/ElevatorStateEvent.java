package Events;
import Elevator.Elevator;
import src.Simulation;

/**
 * A simulation event that sets an elevator's state and calls its tick() method.
 */
public class ElevatorStateEvent extends SimulationEvent {
	private Elevator.ElevatorState mNewState;
	private Elevator mElevator;
	
	public ElevatorStateEvent(long scheduledTime, Elevator.ElevatorState newState, Elevator elevator) {
		super(scheduledTime);
		mNewState = newState;
		mElevator = elevator;
		this.setPriority(1);
	}
	@Override
	public void execute(Simulation sim) {
		mElevator.setState(mNewState);
		mElevator.tick();
	}
	
	@Override
	public String toString() {
		return super.toString() + mElevator;
	}
}
