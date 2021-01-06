package Passengers;/*
 * will not get of at the destination floor; instead, gets off the next time
the doors open after already missing the destination. This mistake only happens once per
passenger. 

When leaving the elevator, if they got off on the wrong floor, schedule the passenger
to reappear on that floor in 5 seconds without changing their destination. If they got off on the
correct floor, tells the passenger to schedule the next step of their travel.
*/

import Elevator.Elevator;
import Events.PassengerNextDestinationEvent;
import src.Simulation;

public class DistractedDebarking implements DebarkingStrategy {
	private int  mistakeHasHappened = 0;

	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {

	int currf = elevator.getCurrentFloorNumber();
	int dest = passenger.getDestination();
		if (mistakeHasHappened == 0 && dest == currf) {
				mistakeHasHappened = 1;
				return false;
				}
		if (dest != currf && mistakeHasHappened == 1) {
//				if (currf < dest && elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
//		return false;
//		} else if (currf < dest && elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN){
//		return false;
//		}
		mistakeHasHappened = 2;
		return true;
		}
		if (mistakeHasHappened == 2 && currf == dest) {
		mistakeHasHappened = 3;
		return true;
		} else if (mistakeHasHappened == 2 && currf != dest) {
		mistakeHasHappened = 3;
		return false;
		} else {
		if (currf == dest){
		return true;
		}
		return false;
		}

	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator){
			// TODO Auto-generated method stub
		if (mistakeHasHappened==2 && passenger.getDestination()!=elevator.getCurrentFloor().getNumber()) {

			Simulation s = elevator.getBuilding().getSimulation();
			PassengerNextDestinationEvent event = new PassengerNextDestinationEvent(s.currentTime() + 5,
					passenger, elevator.getCurrentFloor());
			s.scheduleEvent(event);
		}else {
			passenger.scheduleNextDestination(elevator.getCurrentFloor());
		}
	}

}
