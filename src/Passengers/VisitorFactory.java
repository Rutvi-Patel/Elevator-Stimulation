package Passengers;

import src.Simulation;

import java.util.Random;

public class VisitorFactory implements PassengerFactory {

	private int mWeight;
	
	public VisitorFactory(int weight) {
		mWeight = weight;	
	}

	@Override
	public String factoryName() {
		// TODO Auto-generated method stub
		return "Visitor";
	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return "V";
	}

	@Override
	public int factoryWeight() {
		// TODO Auto-generated method stub
		return mWeight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new CapacityBoarding();
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		// TODO Auto-generated method stub


		Random r = simulation.getRandom();
		int lowFloor = 2;
		int highFloor = simulation.getBuilding().getFloorCount();
		int randomFLoorNumber = r.nextInt((highFloor-lowFloor)+1)+lowFloor;

		int vTime = (int)(r.nextGaussian() * 1200 + 3600);
		return new SingleDestinationTravel(randomFLoorNumber,vTime);
/*		int minFloor = 2;
		int maxFloor = mBuilding.getFloorCount();
		int randomFloor = r.nextInt((maxFloor - minFloor) + 1) + minFloor;		
		
	    // Look up the documentation for the .nextGaussian() method of the Random class.
	    double val = r.nextGaussian() * 1200 + 3600;

//	    VisitorPassenger visitor = new VisitorPassenger(randomFloor, (int) Math.round(val)); 
		return new SingleDestinationTravel(randomFloor, (int) val);
		
*/
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ResponsibleEmbarking();
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new AttentiveDebarking();
	}
}
