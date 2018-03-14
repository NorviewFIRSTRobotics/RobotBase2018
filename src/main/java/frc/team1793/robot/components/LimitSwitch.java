package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.DigitalInput;
import org.strongback.components.Switch;

public class LimitSwitch implements Switch {

    DigitalInput limitSwitch;

    public LimitSwitch(DigitalInput limitSwitch) {
        this.limitSwitch = limitSwitch;
    }

    public LimitSwitch(int port) {
        this(new DigitalInput(port));
    }

    @Override
    public boolean isTriggered() {
        return limitSwitch.get();
    }
}
