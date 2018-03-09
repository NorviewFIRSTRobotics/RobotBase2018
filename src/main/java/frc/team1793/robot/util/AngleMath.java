package frc.team1793.robot.util;

public class AngleMath {
    //Shoulder length: 34in
    //Wrist length: 18in
    //Max wrist angle: 90deg


    private static final double SHOULDER_LENGTH = 34.0;
    private static final double WRIST_LENGTH = 18.0;
    private static final double MAX_LENGTH = 41.0;

//    public static void main(String[] args){
//        System.out.println(lawOfCos(SHOULDER_LENGTH, WRIST_LENGTH, MAX_LENGTH));
//    }

    public static double lawOfCos(double a, double b, double c) {
        return Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
    }
}
