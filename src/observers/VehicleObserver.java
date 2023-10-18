package observers;

import models.Road;
import models.Vehicle;

public interface VehicleObserver {
    void vehicleHasBeenRemoved(Vehicle vehicle);
    void vehicleHasMoved(Road newRoad);
}
