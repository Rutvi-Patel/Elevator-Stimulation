package Passengers;

import Buildings.Floor;
import Buildings.FloorObserver;
import Elevator.ElevatorObserver;
import Elevator.Elevator;

/**
 * A passenger that is either waiting on a floor or riding an elevator.
 */
public class Passenger implements FloorObserver, ElevatorObserver {
	
	// An enum for determining whether a Passenger is on a floor, an elevator, or busy (visiting a room in the building).
	public enum PassengerState {
		WAITING_ON_FLOOR,
		ON_ELEVATOR,
		BUSY
	}
	
	// A cute trick for assigning unique IDs to each object that is created. (See the constructor.)
	private static int mNextId;
	protected static int nextPassengerId() {
		return ++mNextId;
	}
	
	private int mIdentifier;
	private PassengerState mCurrentState;
	
	//Adding six fields to this class
	private String mPassengerName;
	private String mShortName;
	private BoardingStrategy mBoardingStrategy;
	private DebarkingStrategy mDebarkingStrategy;
	private EmbarkingStrategy mEmbarkingStrategy;
	private TravelStrategy mTravelStrategy;
	
	
	//Change constructor to initialize above values
	public Passenger(String passengerName, String shortName, BoardingStrategy boarding, EmbarkingStrategy embarking,
			DebarkingStrategy debarking, TravelStrategy travel) {
		mIdentifier = nextPassengerId();
		mCurrentState = PassengerState.WAITING_ON_FLOOR;
		
		mPassengerName = passengerName;
		mShortName = shortName;
		mBoardingStrategy = boarding;
		mDebarkingStrategy = debarking;
		mEmbarkingStrategy = embarking;
		mTravelStrategy = travel;
		
	}
	
	//I added
	public void scheduleNextDestination(Floor currentFloor) {
		mTravelStrategy.scheduleNextDestination(this, currentFloor);
	}
	
	
	public void setState(PassengerState state) {
		mCurrentState = state;
	}
	
	/**
	 * Gets the passenger's unique identifier.
	 */
	public int getId() {
		return mIdentifier;
	}
	
	/**
	 * Handles an elevator arriving at the passenger's current floor.
	 */
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// This is a sanity check. A Passenger should never be observing a Floor they are not waiting on.
		if (floor.getWaitingPassengers().contains(this) && mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			//Elevator.Direction elevatorDirection = elevator.getCurrentDirection();
			
			// TODO: check if the elevator is either NOT_MOVING, or is going in the direction that this passenger wants.
			// If so, this passenger becomes an observer of the elevator.
			
			if (elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING) {
				elevator.addObserver(this);
			}
			
			else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP ) {
				if (elevator.getCurrentFloor().getNumber() < getDestination()) {
		               elevator.addObserver(this);
		            }
		         } else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
		            if (elevator.getCurrentFloor().getNumber() > getDestination()) {
		               elevator.addObserver(this);
		            }
		         }
		      }
			
		
		// This else should not happen if your code is correct. Do not remove this branch; it reveals errors in your code.
		else {
			throw new RuntimeException("Passenger " + toString() + " is observing Floor " + floor.getNumber() + " but they are " +
			 "not waiting on that floor.");
		}
	}
	
	/**
	 * Handles an observed elevator opening its doors. Depart the elevator if we are on it; otherwise, enter the elevator.
	 */

	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// The elevator is arriving at our destination. Remove ourselves from the elevator, and stop observing it.
		// Does NOT handle any "next" destination...
		if (mCurrentState == PassengerState.ON_ELEVATOR &&elevator.isDoorOpen()){//&& elevator.getCurrentFloor().getNumber() == getDestination()) {
			// TODO: remove this passenger from the elevator, and as an observer of the elevator. Call the
			boolean LeaveElevator;
//			System.out.println(elevator.getObservers());
			LeaveElevator = mDebarkingStrategy.willLeaveElevator(this, elevator);
			if (LeaveElevator) {
				mDebarkingStrategy.departedElevator(this, elevator);
				elevator.removePassenger(this);
				elevator.removeObserver(this);
				setState(PassengerState.BUSY);
			}
		}
		// The elevator has arrived on the floor we are waiting on. If the elevator has room for us, remove ourselves
		// from the floor, and enter the elevator.;
		else if (mCurrentState == PassengerState.WAITING_ON_FLOOR &&
				elevator.getCurrentFloor().getWaitingPassengers().contains(this)) {
			// TODO: determine if the passenger will board the elevator using willBoardElevator.
			// If so, remove the passenger from the current floor, and as an observer of the current floor;
			// then add the passenger as an observer of and passenger on the elevator. Then set the mCurrentState
			// to ON_ELEVATOR.

//				System.out.println(elevator.getCurrentFloor().getWaitingPassengers());

			
			if (mBoardingStrategy.willBoardElevator(this, elevator)) {
//				elevator.addObserver(this);
				elevator.addPassenger(this);
				elevator.getCurrentFloor().removeObserver(this);
				elevator.getCurrentFloor().removeWaitingPassenger(this);
				mEmbarkingStrategy.enteredElevator(this, elevator);
				for (Elevator e: elevator.getBuilding().getmElevators()){
					if (e!=elevator){
						e.removeObserver(this);
					}
				}


				setState(PassengerState.ON_ELEVATOR);
			}
			else {
				elevator.removeObserver(this);

				if (this.getDestination()>elevator.getCurrentFloorNumber()) {
					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_UP);
				}else if (this.getDestination()<elevator.getCurrentFloorNumber()){
					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_DOWN);
				}

			}
		}
	}
	
	/**
	 * Returns the passenger's current destination (what floor they are traveling to).
	 */
	public int getDestination() {
		return mTravelStrategy.getDestination();
	}
	
	/**
	 * Called to determine whether the passenger will board the given elevator that is moving in the direction the
	 * passenger wants to travel.
	 */
//	protected abstract boolean willBoardElevator(Elevator elevator);
	
	/**
	 * Called when the passenger is departing the given elevator.
	 */
//	protected abstract void leavingElevator(Elevator elevator);
	
	// This will be overridden by derived types.
	@Override
	public String toString() {
		return getShortName()+getId();
	}
	
	@Override
	public void directionRequested(Floor sender, Elevator.Direction direction) {
		// Don't care.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Don't care about this.
	}
	
	// The next two methods allow Passengers to be used in data structures, using their id for equality. Don't change 'em.
	@Override
	public int hashCode() {
		return Integer.hashCode(mIdentifier);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger)o;
		return mIdentifier == passenger.mIdentifier;
	}

	
	//I added all getter methods
	public String getPassengerName() {
		return mPassengerName;
	}

	public String getShortName() {
		return mShortName;
	}

	public BoardingStrategy getBoardingStrategy() {
		return mBoardingStrategy;
	}

	public DebarkingStrategy getDebarkingStrategy() {
		return mDebarkingStrategy;
	}

	public EmbarkingStrategy getEmbarkingStrategy() {
		return mEmbarkingStrategy;
	}
	
	public TravelStrategy getTravelStrategy() {
		return mTravelStrategy;
	}
	
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Don't care.
	}
}

