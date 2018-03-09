package frc.team1793.robot.components;

import org.strongback.components.Gyroscope;

import java.util.Arrays;
import java.util.List;

public class GyroSet implements Gyroscope {

    private List<Gyroscope> gyros;

    public GyroSet(Gyroscope... gyros) {
        this.gyros = Arrays.asList(gyros);
    }

    @Override
    public Gyroscope zero() {
        gyros.forEach(Gyroscope::zero);
        return this;
    }

    @Override
    public double getRate() {
        return gyros.stream().mapToDouble(Gyroscope::getRate).average().orElse(0);
    }

    @Override
    public double getAngle() {
        double angle = gyros.stream().mapToDouble(Gyroscope::getAngle).average().orElse(0) % 360;
        if (angle < 0) {
            return 360 + angle;
        }
        return angle;
    }
}
