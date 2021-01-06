package Passengers;

import src.Simulation;



import java.util.Random;

public class StonerFactory implements PassengerFactory {

	private int mWeight;
	
	public StonerFactory(int weight) {
		// TODO Auto-generated constructor stub
		mWeight = weight;
	}

	@Override
	public String factoryName() {
		// TODO Auto-generated method stub
		return "Stoner";
	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return "S";	
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
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ClumsyEmbarking();
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ConfusedDebarking();
	}

}
