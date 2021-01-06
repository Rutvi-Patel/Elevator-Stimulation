package Passengers;

import Elevator.Elevator;
import Logging.Logger;
import com.sun.security.jgss.GSSUtil;

/**
 * Only requests the passenger's destination floor
 */

public class ResponsibleEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		elevator.requestFloor(passenger.getDestination());
		Logger.getInstance().logEvent(passenger.getPassengerName()+" "+passenger.getId()+ " responsibly requested floor "+ passenger.getDestination()+" on elevator "+ elevator.mNumber );
	}

}
