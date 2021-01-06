package Passengers;

import Buildings.Building;
import src.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeliveryPersonFactory implements PassengerFactory {

	private int mWeight;
	
	public DeliveryPersonFactory(int weight) {
		// TODO Auto-generated constructor stub
		mWeight = weight;
	}

	@Override
	public String factoryName() {
		// TODO Auto-generated method stub
		return "Delivery Person";
	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return "DP";
	}

	@Override
	public int factoryWeight() {
		// TODO Auto-generated method stub
		return mWeight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ThresholdBoarding(6);
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		Random ran=simulation.getRandom();
		ArrayList<Integer> destination=new ArrayList<>();
		ArrayList<Long> schedule=new ArrayList<>();
		int FloorNeedVisit=ran.nextInt(simulation.getBuilding().getFloorCount()*2/3)+1;
		int newFloor;
		for(int i=0; i<FloorNeedVisit;i++){
			newFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
			while(destination.contains(newFloor)){
				newFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
			}
			destination.add(newFloor);

		}
		for(int i=0; i<FloorNeedVisit;i++){
			double tempschedule= 10*ran.nextGaussian()+60;
			schedule.add((long)tempschedule);
		}

		return new MultipleDestinationTravel(destination,schedule);
	}



	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ResponsibleEmbarking();
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new DistractedDebarking();
	}

}
