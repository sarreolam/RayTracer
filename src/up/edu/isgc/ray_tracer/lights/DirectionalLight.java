package up.edu.isgc.ray_tracer.lights;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public class DirectionalLight extends Light{

    private Vector3D direction;
    /**
     * @param direction Vector3D
     * @param intensity double
     * @param color color of light
     *
     */
    public DirectionalLight(Vector3D direction, Color color, double intensity) {
        super(Vector3D.ZERO(), color, intensity);
        setDirection(direction);

    }

    /**
     *
     * @return normalized direction
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

        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}
