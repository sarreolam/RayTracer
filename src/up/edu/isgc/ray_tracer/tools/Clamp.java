package up.edu.isgc.ray_tracer.tools;
/**
 * @author Arreola
 * Clamps the number between two numbers
 * */
public class Clamp {
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}