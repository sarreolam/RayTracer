package up.edu.isgc.ray_tracer.lights;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public class PointLight extends Light{
    /**
     * @param position Vector3D
     * @param intensity double
     * @param color color of light
     *
     */
    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);
    }
    /**
     *
     * @param intersection normally the closest intersection to calculate the intensity which its hitting the object
     * @return NDotL double
     */
    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D distance = Vector3D.substract( getPosition(),intersection.getPosition());
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.normalize(distance)), 0.0);
    }
}
