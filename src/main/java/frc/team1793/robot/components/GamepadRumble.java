package frc.team1793.robot.components;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import org.strongback.components.Switch;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.DirectionalAxis;
import org.strongback.components.ui.Gamepad;
import org.strongback.function.IntToBooleanFunction;
import org.strongback.function.IntToIntFunction;

import java.util.function.BiConsumer;
import java.util.function.IntToDoubleFunction;

public interface GamepadRumble extends Gamepad {
    public static GamepadRumble xbox360(int port) {
        Joystick joystick = new Joystick(port);
//        verifyJoystickConnected(joystick);
        joystick.getButtonCount();
        return GamepadRumble.create(joystick::getRawAxis,
                joystick::getRawButton,
                joystick::getPOV,
                () -> joystick.getRawAxis(0),
                () -> joystick.getRawAxis(1) * -1,
                () -> joystick.getRawAxis(4),
                () -> joystick.getRawAxis(5) * -1,
                () -> joystick.getRawAxis(2),
                () -> joystick.getRawAxis(3),
                () -> joystick.getRawButton(5),
                () -> joystick.getRawButton(6),
                () -> joystick.getRawButton(1),
                () -> joystick.getRawButton(2),
                () -> joystick.getRawButton(3),
                () -> joystick.getRawButton(4),
                () -> joystick.getRawButton(8),
                () -> joystick.getRawButton(7),
                () -> joystick.getRawButton(9),
                () -> joystick.getRawButton(10),
                joystick::setRumble);
    }

    public abstract void setRumble(GenericHID.RumbleType rumbleType, double value);

    public static GamepadRumble create(IntToDoubleFunction axisToValue, IntToBooleanFunction buttonNumberToSwitch,
                                 IntToIntFunction dPad, ContinuousRange leftX, ContinuousRange leftY, ContinuousRange rightX, ContinuousRange rightY,
                                 ContinuousRange leftTrigger, ContinuousRange rightTrigger, Switch leftBumper, Switch rightBumper, Switch buttonA,
                                 Switch buttonB, Switch buttonX, Switch buttonY, Switch startButton, Switch selectButton, Switch leftStick,
                                 Switch rightStick, BiConsumer<GenericHID.RumbleType, Double> rumbler) {
        return new GamepadRumble() {
            @Override
            public void setRumble(GenericHID.RumbleType rumbleType, double value) {
                rumbler.accept(rumbleType, value);
            }

            @Override
            public ContinuousRange getAxis(int axis) {
                return () -> axisToValue.applyAsDouble(axis);
            }

            @Override
            public Switch getButton(int button) {
                return () -> buttonNumberToSwitch.applyAsBoolean(button);
            }

            @Override
            public DirectionalAxis getDPad(int pad) {
                return () -> dPad.applyAsInt(pad);
            }

            @Override
            public ContinuousRange getLeftX() {
                return leftX;
            }

            @Override
            public ContinuousRange getLeftY() {
                return leftY;
            }

            @Override
            public ContinuousRange getRightX() {
                return rightX;
            }

            @Override
            public ContinuousRange getRightY() {
                return rightY;
            }

            @Override
            public ContinuousRange getLeftTrigger() {
                return leftTrigger;
            }

            @Override
            public ContinuousRange getRightTrigger() {
                return rightTrigger;
            }

            @Override
            public Switch getLeftBumper() {
                return leftBumper;
            }

            @Override
            public Switch getRightBumper() {
                return rightBumper;
            }

            @Override
            public Switch getA() {
                return buttonA;
            }

            @Override
            public Switch getB() {
                return buttonB;
            }

            @Override
            public Switch getX() {
                return buttonX;
            }

            @Override
            public Switch getY() {
                return buttonY;
            }

            @Override
            public Switch getStart() {
                return startButton;
            }

            @Override
            public Switch getSelect() {
                return selectButton;
            }

            @Override
            public Switch getLeftStick() {
                return leftStick;
            }

            @Override
            public Switch getRightStick() {
                return rightStick;
            }
        };
    }
}
