
package Elevator;

import Buildings.Floor;

public class DisableMode implements OperationMode {

    @Override
    public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
        return false;
    }

    @Override
    public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {

    }

    @Override
    public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {

    }

    @Override
    public void tick(Elevator elevator) {
        elevator.scheduleModeChange(new DisableMode(), Elevator.ElevatorState.IDLE_STATE,300);

    }

    public String toString() {
        return "Disable";
    }


}


