package Events;

import Elevator.Elevator;
import Elevator.OperationMode;
import src.Simulation;
import Elevator.DisableMode;


/**
 * A simulation event that sets an elevator's mode and calls its tick() method.
 */

public class ElevatorModeEvent extends SimulationEvent {
	private Elevator.ElevatorState mNewState;
	private OperationMode mNewOperatingMode;
	private Elevator mElevator;
	long startTime;
	public static boolean a = false;

	
	public ElevatorModeEvent(long scheduledTime, Elevator.ElevatorState newState, 
			Elevator elevator, OperationMode newOperationMode) {
		super(scheduledTime);
		this.setPriority(0);
		mNewState = newState;
		mElevator = elevator;
		if (newOperationMode instanceof DisableMode && !a){
			a= true;
			startTime = scheduledTime;
		}

		mNewOperatingMode = newOperationMode;

	}
	@Override
	public void execute(Simulation sim) {
		if (mNewOperatingMode instanceof  DisableMode){
			if (getScheduledTime()>=startTime+300){
				mElevator.enable(mElevator);
			}else{
				mElevator.setMode(mNewOperatingMode);
				mElevator.setState(mNewState);
				mElevator.tick();
			}
		}else {

			mElevator.setMode(mNewOperatingMode);
			mElevator.setState(mNewState);
			mElevator.tick();
		}
		
	}
	
	@Override
	public String toString() {
		return super.toString() + mElevator;
	}
}
