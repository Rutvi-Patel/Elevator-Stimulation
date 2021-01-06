package Passengers;

import Buildings.Building;
import src.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerFactory implements PassengerFactory {

	private int mWeight;
	
	public WorkerFactory(int weight) {
		// TODO Auto-generated constructor stub
		mWeight = weight;
	}

	@Override
	public String factoryName() {
		// TODO Auto-generated method stub
		return "Worker";
	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return "W";
	}

	@Override
	public int factoryWeight() {
		// TODO Auto-generated method stub
		return mWeight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ThresholdBoarding(4);
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		// TODO Auto-generated method stub

		ArrayList<Integer> f = new ArrayList<>();
		ArrayList<Long> d = new ArrayList<>();
		Random r = simulation.getRandom();
		long duration;
		int lowFloor = 2;
		int highFloor = simulation.getBuilding().getFloorCount();
		int rfloor = 1;
		int x = r.nextInt(4) +2;
		int lastFloor = 0;
		for (int i =0; i<x; i++) {
			rfloor = r.nextInt((highFloor-lowFloor)+1)+lowFloor;
			while (lastFloor==rfloor){
				rfloor = r.nextInt((highFloor-lowFloor)+1)+lowFloor;
			}
			lastFloor = rfloor;

			f.add(rfloor);
		}

		for (int i=0;i<x;i++) {

			duration = (long)(r.nextGaussian() * 180 + 600);
//	long duration = r.nextGaussian()*(20SPAWN_STDEV_DURATION) +(60*SPAWN_MEAN_DURATION);
//	duration = (int)(r.nextGaussian()*(20*SPAWN_STDEV_DURATION)+(60*SPAWN_MEAN_DURATION));
			d.add(duration);
		}

//		System.out.println(f);
		return new MultipleDestinationTravel(f,d);
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
