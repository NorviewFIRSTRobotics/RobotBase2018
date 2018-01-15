package frc.team1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.strongback.Strongback;
import org.strongback.components.Gyroscope;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    protected static TankDrive drive;
    private ContinuousRange driveSpeed, turnSpeed;

    private EnumAuto startPos;
    private DualGyro gyro;

    @Override
    public void robotInit() {
        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        drive = new TankDrive(left, right);
        gyro = new DualGyro(Hardware.AngleSensors.gyroscope(0),Hardware.AngleSensors.gyroscope(1));
        gyro.zero();

        //TODO initialize with dashboard
        startPos = EnumAuto.LEFT;

        initControls();
    }


    @Override
    public void autonomousInit() {
        Strongback.disable();
        Strongback.start();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // Kill anything if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        drive.arcade(driveSpeed.read(), turnSpeed.read());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

    private void initControls() {
        Gamepad controller = Hardware.HumanInterfaceDevices.logitechDualAction(0);
        driveSpeed = controller.getLeftY();
        turnSpeed = controller.getLeftX();
    }


}