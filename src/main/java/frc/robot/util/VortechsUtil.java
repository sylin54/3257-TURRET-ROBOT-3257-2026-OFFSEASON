package frc.robot.util;

public class VortechsUtil {
    public static boolean isOnTarget(double value1, double value2, double tolerance) {
        double difference = Math.abs(value1 - value2);

        return difference < tolerance;
    }
}
