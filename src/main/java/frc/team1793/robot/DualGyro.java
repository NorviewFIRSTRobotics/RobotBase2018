package frc.team1793.robot;

import org.strongback.components.Gyroscope;

public class DualGyro implements Gyroscope {

    private final Gyroscope gyro1, gyro2;

    public DualGyro(Gyroscope gyro1, Gyroscope gyro2) {
        this.gyro1 = gyro1;
        this.gyro2 = gyro2;
    }

    @Override
    public double getRate() {
        return (gyro1.getRate() + gyro2.getRate())/2;
    }

    @Override
    public double getAngle() {
        return (gyro1.getAngle() + gyro2.getAngle())/2;
    }
}
