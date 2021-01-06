package Passengers;

import Elevator.Elevator;

/**
 * AttentiveDebarking is a debarking strategy that has a passenger only get off on the destination floor. 
 * When leaving the elevator, tells the passenger to schedule the next "step" of their travel.
 */

public class AttentiveDebarking implements DebarkingStrategy {

	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		return passenger.getDestination() == elevator.getCurrentFloor().getNumber();
	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator) {
		passenger.scheduleNextDestination(elevator.getCurrentFloor());
		
	}

}
