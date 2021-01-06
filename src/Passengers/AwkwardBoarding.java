package Passengers;

import Elevator.Elevator;
import Logging.Logger;

/**
 * An AwkwardBoarding is a boarding strategy constructed with an integer threshold; will board if the passenger count has 
 * not exceeded the threshold. The threshold grows by 2 each time the passenger decides not to board.
 */
public class AwkwardBoarding implements BoardingStrategy{

	private int mThresHold;
	
	public AwkwardBoarding (int threshold) {
		mThresHold = threshold;
	}
	
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		if (elevator.getPassengerCount() < mThresHold) {
			return true;
		}
		else {
			mThresHold += 2;
			Logger.getInstance().logEvent(passenger.getPassengerName() + " "+ passenger.getId() + " was too awkward to board " +
					"on elevator on floor " + elevator.getCurrentFloorNumber() + " now has threshold "+ (mThresHold-1));
			return false;
		}
	}
}
