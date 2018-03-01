package frc.team1793.robot;

import com.sun.javafx.geom.Vec2d;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ArmTest extends Application {
    private static final double SHOULDER_LENGTH = 340, WRIST_LENGTH = 180;
    public static final int W = 800;
    public static final int H = 800;
    private static Vec2d shoulderStart;

    private static DoubleProperty shoulderAngle = new SimpleDoubleProperty();
    private static DoubleProperty wristAngle = new SimpleDoubleProperty();


    private static DoubleProperty wristGoal = new SimpleDoubleProperty(0);
    private static DoubleProperty shoulderGoal = new SimpleDoubleProperty(0);


    private static double lawOfCosine(double a, double b, double c) {
        return Math.acos(((a * a) + (b * b) - (c * c)) / (2 * a * b));
    }

    private static Vec2d angles(double x, double y) {
        double d = distance(x, y);
        double D1 = Math.atan2(y, x);
        double D2 = lawOfCosine(d, SHOULDER_LENGTH, WRIST_LENGTH);
        double A1 = D1 + D2;
        double A2 = lawOfCosine(SHOULDER_LENGTH, WRIST_LENGTH, d);
        return new Vec2d(Math.abs(Math.PI*2+A1), Math.abs(Math.PI*2+A2));
    }

    public static void main(String[] args) {
        shoulderStart = new Vec2d(W / 2, H / 2);
        launch(args);
    }

    private static double distance(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    private double x = -Math.sqrt(2)*50, y = -Math.sqrt(2)*50;

    @Override
    public void start(Stage primaryStage) {
        Vec2d angles = new Vec2d(Math.toRadians(90),Math.toRadians(180));
        shoulderGoal.set(angles.x);
        wristGoal.set(angles.y);
        System.out.printf("x=%s, y=%s: A1=%s (%s°), A2=%s (%s°)\n", x, y, angles.x, Math.toDegrees(angles.x), angles.y, Math.toDegrees(angles.y));
        Timeline shoulderTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(shoulderAngle, 0)
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(shoulderAngle, shoulderGoal.get())
                )
        );
        shoulderTimeline.setCycleCount(1);
        shoulderTimeline.setOnFinished(actionEvent -> shoulderTimeline.stop());

        Timeline wristTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(wristAngle, 0)
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(wristAngle, wristGoal.get())
                )
        );
        wristTimeline.setCycleCount(1);

        primaryStage.setTitle("ARmShit");
        Group root = new Group();
        Canvas canvas = new Canvas(W, H);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw2DShapes(canvas.getGraphicsContext2D());
            }
        };

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        timer.start();

        shoulderTimeline.play();
        wristTimeline.play();

    }

    private void draw2DShapes(GraphicsContext gc) {
        double wristX1 = Math.cos(shoulderAngle.doubleValue()) * SHOULDER_LENGTH + shoulderStart.x;
        double wristY1 = -Math.sin(shoulderAngle.doubleValue()) * SHOULDER_LENGTH + shoulderStart.y;
        double wristX2 = Math.cos(wristAngle.doubleValue()-(Math.PI-shoulderAngle.doubleValue())) * WRIST_LENGTH + wristX1;
        double wristY2 = -Math.sin(wristAngle.doubleValue()-(Math.PI-shoulderAngle.doubleValue())) * WRIST_LENGTH + wristY1;

        gc.setFill(Color.rgb(255, 255, 255));
        gc.fillRect(0, 0, W, H);
        //grid
        gc.setLineWidth(0.5);
        gc.setStroke(Color.rgb(0, 0, 0));
        for (int i = -W/2; i <= W/2; i+=10) {
            gc.strokeLine(W/2+i, 0, W/2+i, H);
            gc.strokeLine(0,H/2+i, W,H/2+i);
        }
        gc.setLineWidth(5);
//        gc.setStroke(Color.rgb(0, 0, 255));
//        gc.strokeLine(W / 2 + x, H / 2 - y, W / 2 + x, H / 2 - y);
        gc.setStroke(Color.rgb(255, 0, 0));
        gc.setLineWidth(5);
        gc.strokeLine(shoulderStart.x, shoulderStart.y, wristX1, wristY1);
        gc.setStroke(Color.rgb(0, 255, 0));
        gc.strokeLine(wristX1, wristY1, wristX2, wristY2);
    }


}
