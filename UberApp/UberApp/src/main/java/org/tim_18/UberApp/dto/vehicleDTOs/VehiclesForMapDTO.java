package org.tim_18.UberApp.dto.vehicleDTOs;

import java.util.Set;

public class VehiclesForMapDTO {
    private Set<VehicleDTO> inUse;
    private Set<VehicleDTO> outOfUse;

    public VehiclesForMapDTO() {
    }

    public VehiclesForMapDTO(Set<VehicleDTO> inUse, Set<VehicleDTO> outOfUse) {
        this.inUse = inUse;
        this.outOfUse = outOfUse;
    }

    public Set<VehicleDTO> getInUse() {
        return inUse;
    }

    public void setInUse(Set<VehicleDTO> inUse) {
        this.inUse = inUse;
    }

    public Set<VehicleDTO> getOutOfUse() {
        return outOfUse;
    }

    public void setOutOfUse(Set<VehicleDTO> outOfUse) {
        this.outOfUse = outOfUse;
    }
}
