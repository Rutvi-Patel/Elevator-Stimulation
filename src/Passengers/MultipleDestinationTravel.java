package Passengers;

import Buildings.Floor;
import Events.PassengerNextDestinationEvent;
import src.Simulation;

import java.util.List;

public class MultipleDestinationTravel implements TravelStrategy {

	List<Integer> mDestinationFloors;
	private List<Long> mDurationAmounts;
	
	public MultipleDestinationTravel(List<Integer> destinations, List<Long> durations) {
		// TODO Auto-generated constructor stub
		mDestinationFloors = destinations;
		mDurationAmounts = durations;
	}

	@Override
	public int getDestination() {
		// TODO Auto-generated method stub
		if (mDestinationFloors.isEmpty()) {
			return 1;
		}
		return mDestinationFloors.get(0);
	}

	@Override
	public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
		// TODO Auto-generated method stub
//		currentFloor.removeWaitingPassenger(passenger);
		if (mDestinationFloors.isEmpty() && getDestination() == 1) {
			currentFloor.getWaitingPassengers().remove(passenger);
			currentFloor.removeObserver(passenger);
			System.out.println("Passenger " + passenger.getShortName()+passenger.getId()  +" Leaving the elevator.");
		}
		
		else {
			mDestinationFloors.remove(0);
			Simulation s = currentFloor.getBuilding().getSimulation();
	        PassengerNextDestinationEvent next = new PassengerNextDestinationEvent(s.currentTime() + mDurationAmounts.get(0),
	        		passenger, currentFloor);
			mDurationAmounts.remove(0);

	        s.scheduleEvent(next);
		}
	
	}
}
