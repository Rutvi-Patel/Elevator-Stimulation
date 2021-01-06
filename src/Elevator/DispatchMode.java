package Elevator;

import Buildings.Floor;

/**
 * A DispatchMode elevator is in the midst of a dispatch to a target floor in order to handle a request in a target
 * direction. The elevator will not stop on any floor that is not its destination, and will not respond to any other
 * request until it arrives at the destination.
 */

public class DispatchMode implements OperationMode {
	
	// The destination floor of the dispatch.
	private Floor mDestination;
	// The direction requested by the destination floor; NOT the direction the elevator must move to get to that floor.
	private Elevator.Direction mDesiredDirection;
	
	public DispatchMode(Floor destination, Elevator.Direction desiredDirection) {
		mDestination = destination;
		mDesiredDirection = desiredDirection;

	}
	
	// TODO: implement the other methods of the OperationMode interface.
	// Only Idle elevators can be dispatched.
	// A dispatching elevator ignores all other requests.
	// It does not check to see if it should stop of floors that are not the destination.
	// Its flow of ticks should go: IDLE_STATE -> ACCELERATING -> MOVING -> ... -> MOVING -> DECELERATING.
	//    When decelerating to the destination floor, change the elevator's direction to the desired direction,
	//    announce that it is decelerating, and then schedule an operation change in 3 seconds to
	//    ActiveOperation in the DOORS_OPENING state.
	// A DispatchOperation elevator should never be in the DOORS_OPENING, DOORS_OPEN, or DOORS_CLOSING states.
	
	
	@Override
	public String toString() {
		return "Dispatching to " + mDestination.getNumber() + " " + mDesiredDirection;
	}

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		// TODO Auto-generated method stub
//		 elevator.getCurrentFloor().getNumber() == floor.getNumber();
		 return  false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {
		// TODO Auto-generated method stub
		// Must add ourselves as an observer of our floor, since we are dispatching on.
		elevator.getCurrentFloor().addObserver(elevator);
		
		if (elevator.getCurrentFloor().getNumber() > targetFloor.getNumber()) {
			elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
		}
		else {
			elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
		}
		
		elevator.getFloorsRequested().set(targetFloor.getNumber() - 1, true);
		// TODO: you will need to create this method in the Elevator class, along with ElevatorModeEvent to support it.
		elevator.scheduleModeChange(new DispatchMode(targetFloor, targetDirection),
		Elevator.ElevatorState.IDLE_STATE, 0);
		
	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void tick(Elevator elevator) {
		// TODO Auto-generated method stub

		switch (elevator.getCurrentState()) {
			case IDLE_STATE:
				if (!elevator.disableIsCalled && elevator.getBuilding().getSimulation().disableMode==1 && mDestination.getNumber()!=1){
					elevator.disableIsCalled = true;
					elevator.disable(elevator);
				}else {

					if (mDestination.getNumber() < elevator.getCurrentFloor().getNumber()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
					} else if (mDestination.getNumber() > elevator.getCurrentFloor().getNumber()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
					}

					elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 0);

				}
				break;


			case ACCELERATING:

				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 3);

				break;
			case MOVING:

				if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
					int temp = elevator.getCurrentFloor().getNumber() + 1;
					if (temp <= elevator.getBuilding().getFloorCount())
						elevator.setCurrentFloor(elevator.getBuilding().getFloor(temp));
				} else {
					int temp1 = elevator.getCurrentFloor().getNumber() - 1;
					if (temp1 >= 1) {
						elevator.setCurrentFloor(elevator.getBuilding().getFloor(temp1));
					}
				}

				if (mDestination.getNumber() == elevator.getCurrentFloorNumber()) {
//						System.out.println("I decelarate mrequest");
					elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);
				}else {

					elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
				}
					break;

					case DECELERATING:
						elevator.getFloorsRequested().set(elevator.getCurrentFloor().getNumber() - 1, false);

						boolean keepGoing;
						if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP &&
								(elevator.getFloorsRequested().lastIndexOf(true) >= elevator.getCurrentFloor().getNumber()
										|| elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_UP))) {

							keepGoing = true;
						} else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN &&
								((elevator.getFloorsRequested().indexOf(true) < elevator.getCurrentFloor().getNumber() && elevator.getFloorsRequested().indexOf(true) > -1)
										|| elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_DOWN))) { // finish this, but beware: indexOf returns -1 if not found
							keepGoing = true;
						} else {
							keepGoing = false;
						}

						if (!keepGoing) {
							if (elevator.getFloorsRequested().indexOf(true) < 0) {
								elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
							} else {
								if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
									elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
								} else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
									elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
								}
							}
						}
						elevator.getCurrentFloor().clearDirection(mDesiredDirection);
						elevator.announceElevatorDecelerating();
						elevator.scheduleModeChange(new ActiveMode(),
								Elevator.ElevatorState.DOORS_OPENING, 3);
						break;
		}


		
	}
}
