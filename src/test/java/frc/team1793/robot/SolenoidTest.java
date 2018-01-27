package frc.team1793.robot;

import frc.team1793.robot.commands.SolenoidExtendCommand;
import frc.team1793.robot.commands.SolenoidRetractCommand;
import frc.team1793.robot.components.SolenoidSet;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;

import static org.fest.assertions.Assertions.assertThat;

public class SolenoidTest {
    private final long START_TIME_MS = 1000;
    private SolenoidSet solenoid;
    private CommandTester tester;

    @Before
    public void before(){
        solenoid = new SolenoidSet(Mock.manualSolenoid(), Mock.manualSolenoid());
    }

    @Test
    public void extendTest(){
        tester = new CommandTester(new SolenoidExtendCommand(solenoid));
        assertThat(solenoid.isStopped()).isTrue();
        tester.step(START_TIME_MS);

        tester.step(START_TIME_MS + 1999);
        assertThat(solenoid.isExtending()).isTrue();
    }

    @Test
    public void retractTest(){
        tester = new CommandTester(new SolenoidRetractCommand(solenoid));
        assertThat(solenoid.isStopped()).isTrue();
        tester.step(START_TIME_MS);

        tester.step(START_TIME_MS + 1999);
        assertThat(solenoid.isRetracting()).isTrue();
    }

}
