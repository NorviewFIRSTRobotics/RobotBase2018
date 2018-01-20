package frc.team1793.robot;

import org.strongback.components.Gyroscope;

public class DualGyro implements Gyroscope {

    private final Gyroscope gyro1, gyro2;

    public DualGyro(Gyroscope gyro1, Gyroscope gyro2) {
        this.gyro1 = gyro1;
        this.gyro2 = gyro2;
    }

    @Override
    public Gyroscope zero() {
        gyro1.zero();
        gyro2.zero();
        return this;
    }

    @Override
    public double getRate() {
        return (gyro1.getRate() + gyro2.getRate())/2;
    }

    @Override
    public double getAngle() {
        double angle = ((gyro1.getAngle() + gyro2.getAngle())/2)%360;
        if(angle < 0){
            return 360 + angle;
        }
        return angle;
    }
}
