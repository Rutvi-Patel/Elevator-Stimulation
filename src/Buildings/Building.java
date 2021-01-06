package Buildings;

import Elevator.ElevatorObserver;
import Elevator.Elevator;
import src.Simulation;

import java.util.*;

public class Building implements ElevatorObserver, FloorObserver {

	private List<Elevator> mElevators = new ArrayList<>();
	private List<Floor> mFloors = new ArrayList<>();
	private Simulation mSimulation;


	private Queue<FloorRequest> mWaitingFloors = new ArrayDeque<>();


	String upDownEmojiStr;
	String downEmojiStr;
	String upEmojiStr;

	public Building(int floors, int elevatorCount, Simulation sim) {
		mSimulation = sim;

		// Construct the floors, and observe each one.
		for (int i = 0; i < floors; i++) {
			Floor f = new Floor(i + 1, this);
			f.addObserver(this);
			mFloors.add(f);
		}

		// Construct the elevators, and observe each one.
		for (int i = 0; i < elevatorCount; i++) {
			Elevator elevator = new Elevator(i + 1, this);
			elevator.addObserver(this);
			for (Floor f : mFloors) {
				elevator.addObserver(f);
			}
			mElevators.add(elevator);
		}


		//Creating the emojis ****************************************************

		int[] surrogates = {0x2195, 0xFE0F};
		upDownEmojiStr = new String(surrogates, 0, surrogates.length);
//		System.out.println(upDownEmojiStr);
//		System.out.println("\u2195\uFE0F");

		int[] surrogate1 = {0x2B06, 0xFE0F};
		downEmojiStr = new String(surrogate1, 0, surrogate1.length);
//		System.out.println(downEmojiStr);
//		System.out.println("\u2B06\uFE0F");

		int[] surrogate2 = {0x2B07, 0xFE0F};
		upEmojiStr = new String(surrogate2, 0, surrogate2.length);
//		System.out.println(upEmojiStr);
//		System.out.println("\u2B07\uFE0F");

	}


	public String toString() {

		String output = "";
		String x = "|";
		String y = "";
		ArrayList<String> arr = new ArrayList<>();

		for (int i = this.getFloorCount() - 1; i > -1; i--) {
			for (int j = 0; j < mElevators.size(); j++) {
				if (mElevators.get(j).getCurrentFloor().getNumber() - 1 == i) {
					x += " X |";
				} else {
					x += "   |";
				}
			}
			for (int h = 0; h < mFloors.get(i).getWaitingPassengers().size(); h++) {
				if (mFloors.get(i).getWaitingPassengers().get(h).getDestination()>i+1){
					arr.add(downEmojiStr);
				}else if (mFloors.get(i).getWaitingPassengers().get(h).getDestination()<i+1){
					arr.add(upEmojiStr);
				}
			}
//			System.out.println(arr);
			if (arr.contains(downEmojiStr) && arr.contains(upEmojiStr)){
				y += upDownEmojiStr;
			}else if (!arr.contains(downEmojiStr) && !arr.isEmpty()){
				y += upEmojiStr;
			}else if (!arr.contains(upEmojiStr)  && !arr.isEmpty()){
				y += downEmojiStr;
			}else {

			}
			arr.clear();
			for (int k = 0; k < mFloors.get(i).getWaitingPassengers().size(); k++) {
				y += mFloors.get(i).getWaitingPassengers().get(k) + "->" + (mFloors.get(i).getWaitingPassengers().get(k).getDestination()) + "  ";
			}
			//Setting up display for return and resetting others
			output += String.format("%2d : %s %s \n", (i + 1), x, y);
			x = "|";
			y = "";
		}
		for (int i = 0; i < mElevators.size(); i++) {
			output += mElevators.get(i).toString() + "\n";
		}
		return output;
	}


	public int getFloorCount() {
		return mFloors.size();
	}

	public Floor getFloor(int floor) {
		return mFloors.get(floor - 1);
	}

	public Simulation getSimulation() {
		return mSimulation;
	}

	public List<Elevator> getmElevators() {
		return mElevators;
	}


	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Have to implement all interface methods even if we don't use them.
	}

	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Don't care.
	}

	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// TODO: if mWaitingFloors is not empty, remove the first entry from the queue and dispatch the elevator to that floor.
//		System.out.println(mWaitingFloors);
		if (!mWaitingFloors.isEmpty()) {
			FloorRequest requ = mWaitingFloors.poll();
//			System.out.println("Remove from the mWaiting list elevator arriving: " + mWaitingFloors);
			elevator.dispatchTo(requ.mDestination, requ.mDirection);

		}
	}

	@Override
	public void elevatorArriving(Floor sender, Elevator elevator) {
		// TODO: add the floor mWaitingFloors if it is not already in the queue.
//		System.out.println(mWaitingFloors);
		mWaitingFloors.removeIf(f ->  f.mDestination.getNumber() == sender.getNumber() &&
				(elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING ||
						elevator.getCurrentDirection() == f.mDirection));

		sender.clearDirection(elevator.getCurrentDirection());
//		System.out.println("Remove from the mWaiting list elevator arriving: " + mWaitingFloors);

	}

	@Override
	public void directionRequested(Floor floor, Elevator.Direction direction) {
		// TODO: go through each elevator. If an elevator is idle, dispatch it to the given floor.
		// TODO: if no elevators are idle, then add the floor number to the mWaitingFloors queue.

		boolean dispatchElev = false;
//		boolean notRequested = false;

		for (Elevator e : mElevators) {
			if (e.canBeDispatchto(e,floor)) {
				e.dispatchTo(floor,direction);
				dispatchElev = true;
				break;
			}
		}

		if (!dispatchElev) {
			FloorRequest req = new FloorRequest(floor,direction);
//			System.out.println(mWaitingFloors);
				this.mWaitingFloors.add(req);
//			System.out.println("add to the mWaiting list" + mWaitingFloors);
		}
	}


	private class FloorRequest implements Comparable {
		private Floor mDestination;
		private Elevator.Direction mDirection;


		private FloorRequest(Floor destination, Elevator.Direction direction) {
			mDestination = destination;
			mDirection = direction;
		}
		public String toString(){
			return mDestination+" "+mDirection;
		}

		@Override
		public int compareTo(Object o) {
			if (this.toString().equals(o.toString())){
				return 0;
			}
			return 1;
		}
	}

}
