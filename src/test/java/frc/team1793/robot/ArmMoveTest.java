package frc.team1793.robot;

import frc.team1793.robot.components.Arm;
import org.junit.Before;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;

public class ArmMoveTest {
    private final long START_TIME_MS = 1000;
    private Arm arm;
    private CommandTester tester;

    @Before
    public void before(){ arm = new Arm(Mock.manualSolenoid(), Mock.angleSensor(), Mock.angleSensor(), Mock.stoppedMotor(), Mock.stoppedMotor()); }
}
