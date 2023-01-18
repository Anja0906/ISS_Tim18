package org.tim_18.UberApp.dto.Distance;

import lombok.Data;
import org.tim_18.UberApp.model.Driver;

@Data
public class DriverTime {
    Driver driver;
    int time;

    public DriverTime(Driver driver, int time) {
        this.driver = driver;
        this.time = time;
    }
}
