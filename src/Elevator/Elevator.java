package Elevator;

import Buildings.Building;
import Buildings.Floor;
import Buildings.FloorObserver;
import Events.ElevatorModeEvent;
import Events.ElevatorStateEvent;
import Passengers.Passenger;
import src.Simulation;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator implements FloorObserver {

	public enum ElevatorState {
		IDLE_STATE,
		DOORS_OPENING,
		DOORS_CLOSING,
		DOORS_OPEN,
		ACCELERATING,
		DECELERATING,
		MOVING
	}

	public enum Direction {
		NOT_MOVING,
		MOVING_UP,
		MOVING_DOWN
	}

	public int mNumber;
	private Building mBuilding;


	private ElevatorState mCurrentState = ElevatorState.IDLE_STATE;
	private Direction mCurrentDirection = Direction.NOT_MOVING;
	private Floor mCurrentFloor;

	private List<Passenger> mPassengers = new ArrayList<>();


	private List<ElevatorObserver> mObservers = new ArrayList<>();

	// TODO: declare a field to keep track of which floors have been requested by passengers.
	private ArrayList<Boolean> mFloorsRequested;

	private OperationMode mCurrentMode = new IdleMode();

	public Elevator(int number, Building bld) {
		mNumber = number;
		mBuilding = bld;
		mCurrentFloor = bld.getFloor(1);

		mFloorsRequested = new ArrayList<Boolean>(bld.getFloorCount());
		for (int i = 0; i < bld.getFloorCount(); i++) {
			mFloorsRequested.add(false);
		}
//		scheduleStateChange(ElevatorState.IDLE_STATE, 0);
		scheduleModeChange(mCurrentMode, ElevatorState.IDLE_STATE, 0);

	}

	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */

	void scheduleStateChange(ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorStateEvent(sim.currentTime() + timeFromNow, state, this));
	}

	void scheduleModeChange(OperationMode operatingMode, ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime() + timeFromNow, state, this, operatingMode));

	}

	/**
	 * Adds the given passenger to the elevator's list of passengers, and requests the passenger's destination floor.
	 */
	public void addPassenger(Passenger passenger) {
		// TODO: add the passenger's destination to the set of requested floors.
		mPassengers.add(passenger);

		mFloorsRequested.set(passenger.getDestination() - 1, true);
	}

	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);
	}

	/**
	 * Schedules the elevator's next state change based on its current state.
	 */
	public void tick() {
		// TODO: port the logic of your state changes from Project 1, accounting for the adjustments in the spec.
		// TODO: State changes are no longer immediate; they are scheduled using scheduleStateChange().
		mCurrentMode.tick(this);

	}


	/**
	 * Sends an idle elevator to the given floor.
	 */
	int targetFloor = 0;

	public void dispatchTo(Floor floor, Direction direction) {
		// TODO: if we are currently idle and not on the given floor, change our direction to move towards the floor.
		// TODO: set a floor request for the given floor, and schedule a state change to ACCELERATING immediately.
		targetFloor = floor.getNumber();
		if (mCurrentMode.canBeDispatchedToFloor(this, floor)){
		mCurrentMode.dispatchToFloor(this, floor, direction);
	}

}
public boolean disableIsCalled = false;

public OperationMode modeBefore;

	public void disable (Elevator elevator){
		modeBefore = elevator.mCurrentMode;
			scheduleModeChange(new DisableMode(), ElevatorState.IDLE_STATE,0);
	}

	public void enable (Elevator elevator){

		scheduleModeChange(modeBefore, ElevatorState.IDLE_STATE,0);
	}

public boolean canBeDispatchto(Elevator elevator, Floor floor){
		return mCurrentMode.canBeDispatchedToFloor(this,floor);
}
	
	// Simple accessors
	public Floor getCurrentFloor() {
		return mCurrentFloor;
	}
	
	//I added for easier access
	public int getCurrentFloorNumber() {
		return mCurrentFloor.getNumber();
	}
	
	public Direction getCurrentDirection() {
		return mCurrentDirection;
	}
	
	public Building getBuilding() {
		return mBuilding;
	}



	public ArrayList<Boolean> getFloorsRequested() {
		return mFloorsRequested;
	}
	public ElevatorState getCurrentState() {
		return mCurrentState;
	}

	public List<Passenger> getPassengers() {
		return mPassengers;
	}
	
	public int getNumFloors() {
		return mBuilding.getFloorCount();
	}

	public List<ElevatorObserver> getObservers() {
		return mObservers;
	}
	
	/**
	 * Returns true if this elevator is in the idle state.
	 * @return
	 */
	public boolean isIdle() {
		// TODO: complete this method.
		return this.getCurrentState() == ElevatorState.IDLE_STATE;
	}
	
	public boolean isDoorOpen() {	
		return this.getCurrentState() == ElevatorState.DOORS_OPEN;
		
	}

	public int passCount = this.getCapacity();
	// All elevators have a capacity of 10, for now.
	public int getCapacity() {
		return 10;
	}
	
	public int getPassengerCount() {
		return mPassengers.size();
	}
	
	// Simple mutators
	public void setState(ElevatorState newState) {
		mCurrentState= newState;
	}
	
	public void setMode (OperationMode operation) {
		mCurrentMode = operation;
	}
	
	public void setCurrentDirection(Direction direction) {
		mCurrentDirection = direction;
	}
	
	public void setCurrentFloor(Floor floor) {
		mCurrentFloor = floor;
	}
	
	// Observers
	public void addObserver(ElevatorObserver observer) {
		mObservers.add(observer);
	}
	
	public void removeObserver(ElevatorObserver observer) {
		mObservers.remove(observer);
	}
	
	public void announceElevatorIdle() {

		ArrayList<ElevatorObserver> copy = new ArrayList<>();
		for (ElevatorObserver each : mObservers){
			copy.add(each);
		}
//		System.out.println("In elevator announce idle: I was here");
		for (ElevatorObserver each : copy) {
			each.elevatorWentIdle(this);
		}
	}
	
	public void announceElevatorDecelerating() {
		
		ArrayList<ElevatorObserver> cache = new ArrayList<>();
		for(ElevatorObserver obs : mObservers){
			cache.add(obs);
		}
		
		for (ElevatorObserver e: cache) {
			e.elevatorDecelerating(this);
		}
	}


	public void announceDoorsOpened() {
		ArrayList<ElevatorObserver> copy2 = new ArrayList<>();
		for (ElevatorObserver each: mObservers){
			copy2.add(each);
		}
//		System.out.println(copy2);
		for (ElevatorObserver each: copy2){
			each.elevatorDoorsOpened(this);
		}
	}
	
	// FloorObserver methods
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// Not used.
	}
	
	/**
	 * Triggered when our current floor receives a direction request.
	 */
	@Override
	public void directionRequested(Floor sender, Direction direction) {
		// TODO: if we are currently idle, change direction to match the request. Then alert all our observers that we are decelerating,
		// TODO: then schedule an immediate state change to DOORS_OPENING.

		mCurrentMode.directionRequested(this , sender, direction);

		
//		if (this.isIdle()) {
//			this.setCurrentDirection(direction);
//
//			for(int i = 0; i < mObservers.size(); i++){
//				mObservers.get(i).elevatorDecelerating(this);
//			}
//			this.scheduleStateChange(ElevatorState.DOORS_OPENING, 0);
//
//		}
	}

	//I added here
	public void requestFloor(int floorNum) {
		mFloorsRequested.set(floorNum - 1, true);
	}
	
	// Voodoo magic.
	@Override
	public String toString() {
		String str = "";
		for (int i =0;i<mFloorsRequested.size();i++){
			if (mFloorsRequested.get(i).equals(true)){
				str = str + (i+1) + ",";
			}
		}


		return "Elevator " + mNumber +" ["+ mCurrentMode+"] "+ " - " + mCurrentFloor + " - " + getCurrentState() + " - " + mCurrentDirection + " - "
		 + "[" + mPassengers.stream().map(p -> p.getShortName()+Integer.toString(p.getId())).collect(Collectors.joining(", "))
		 + "]" + " {"+str+"}";
	}	
}
