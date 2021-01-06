package Passengers;

import Buildings.Floor;
import Events.PassengerNextDestinationEvent;
import src.Simulation;

public class SingleDestinationTravel implements TravelStrategy {

	private int mDestinationFloor;
	private long mDurationTime;
	
	public SingleDestinationTravel(int destinationFloor, long durationTime) {
		super();
		mDestinationFloor = destinationFloor;
		mDurationTime = durationTime;
	}

	@Override
	public int getDestination() {
		// TODO Auto-generated method stub
		return mDestinationFloor;
	}

	@Override
	public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
		// TODO Auto-generated method stub
		
		if (getDestination() == 1) {
	//		elevator.removePassenger(this);
	        System.out.println("Passenger" + passenger.getShortName()+passenger.getId()  +" leaving the building");
	      } 
		else {
			Simulation s = currentFloor.getBuilding().getSimulation();
	        PassengerNextDestinationEvent event = new PassengerNextDestinationEvent(s.currentTime() + mDurationTime,
	        		passenger, currentFloor);
	        
	        mDestinationFloor = 1;
	        s.scheduleEvent(event);

	      }
	}
}
