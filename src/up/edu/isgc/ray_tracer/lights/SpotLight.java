package up.edu.isgc.ray_tracer.lights;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public class SpotLight extends Light{

    private Vector3D direction;
    private double angle;
    /**
     * @param position Vector3D
     * @param direction Vector3D
     * @param angle double in which it will give of light
     * @param intensity double
     * @param color color of light
     *
     */
    public SpotLight(Vector3D position, Vector3D direction, double angle, double intensity, Color color) {
        super(position, color, intensity);
        setDirection(Vector3D.scalarMultiplication(direction, -1));
        setAngle(angle);
    }

    /**
     *
     * @return angle double
     */
    public double getAngle() {
        return angle;
    }
    /**
     *
     * @param angle double
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }
    /**
     *
     * @return direction Vector3D
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }
    /**
     *
     * @param direction Vector3D
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    /**
     *
     * @param intersection normally the closest intersection to calculate the intensity which its hitting the object
     * @return NDotL double
     */
    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D directionToLight = Vector3D.normalize(Vector3D.substract(getPosition(),intersection.getPosition()));


        double cosTheta = Vector3D.dotProduct(directionToLight, getDirection()) / (Vector3D.magnitude(directionToLight)* Vector3D.magnitude(getDirection()));

        if (Math.toDegrees(Math.acos(cosTheta)) < getAngle()) {
            return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);

        }
        return 0.0;

    }
}
