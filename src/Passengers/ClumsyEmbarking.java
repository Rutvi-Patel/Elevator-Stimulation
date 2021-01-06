package Passengers;

import Elevator.Elevator;
import Logging.Logger;

/**
 *Requests the destination floor and also the floor that comes before the destination. 
 *For example, if moving down to floor 5, we request both floor 5 and 6.
 */

public class ClumsyEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		int clumz = 0;
		if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
			clumz = passenger.getDestination() + 1;
			if (elevator.getCurrentFloor().getNumber() == clumz){
				elevator.requestFloor(passenger.getDestination());
			}else {
				if (passenger.getDestination() == elevator.getNumFloors()){
					elevator.requestFloor(passenger.getDestination());
				}else {
					elevator.requestFloor(passenger.getDestination() + 1);
					elevator.requestFloor(passenger.getDestination());
				}
			}
		}

		
		else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
			clumz = passenger.getDestination() - 1;
			if (elevator.getCurrentFloor().getNumber() == clumz){
				elevator.requestFloor(passenger.getDestination());
			}else {
				if (passenger.getDestination() == 1){
					elevator.requestFloor(passenger.getDestination());
				}else {
					elevator.requestFloor(passenger.getDestination() - 1);
					elevator.requestFloor(passenger.getDestination());
				}
			}
		}
		Logger.getInstance().logEvent(passenger.getPassengerName() + " " + passenger.getId() + " clumsily requested floors " + passenger.getDestination() + " and " + clumz + " on elevator " + elevator.mNumber);
	}
}
