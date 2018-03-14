package frc.team1793.robot.components;

import org.strongback.components.Solenoid;

import java.util.Arrays;
import java.util.List;

public class SolenoidSet implements Solenoid {
    private List<Solenoid> solenoids;

    public SolenoidSet(Solenoid... solenoids) {
        this.solenoids = Arrays.asList(solenoids);
    }

    @Override
    public Direction getDirection() {
        return solenoids.stream().findFirst().map(Solenoid::getDirection).orElse(Direction.STOPPED);
    }

    @Override
    public Solenoid extend() {
        solenoids.forEach(Solenoid::extend);

        return this;
    }

    @Override
    public Solenoid retract() {
        solenoids.forEach(Solenoid::retract);

        return this;
    }

    @Override
    public boolean isExtending() {
        return solenoids.stream().allMatch(Solenoid::isExtending);
    }

    @Override
    public boolean isRetracting() {
        return solenoids.stream().allMatch(Solenoid::isRetracting);
    }

    @Override
    public boolean isStopped() {
        return solenoids.stream().allMatch(Solenoid::isStopped);
    }
}
