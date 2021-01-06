package Passengers;

import Elevator.Elevator;

/**
 *Only debarks on floor 1, and leaves the building regardless of what their destination initially was.
 */

public class ConfusedDebarking implements DebarkingStrategy {

	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		return elevator.getCurrentFloorNumber() == 1;	
	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		elevator.removePassenger(passenger);

	}
}
