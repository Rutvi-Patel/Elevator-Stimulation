package Passengers;
import Elevator.Elevator;

/**
 * A Threshold Boarding is a boarding strategy constructed with an integer threshold; will board if the passenger count has 
 * not exceeded the threshold.
 */

public class ThresholdBoarding implements BoardingStrategy {
	int threshold;

	ThresholdBoarding(int threshold){
		this.threshold = threshold;
	}
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		if(threshold > elevator.getPassengerCount()){
			return true;
		}
		else
			return false;
	}
}