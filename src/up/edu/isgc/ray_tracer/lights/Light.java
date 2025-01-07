package up.edu.isgc.ray_tracer.lights;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;
import up.edu.isgc.ray_tracer.objects.IIntersectable;
import up.edu.isgc.ray_tracer.objects.Object3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public abstract class Light extends Object3D{
    /**
     * @param position Vector3D
     * @param intensity double
     * @param color color of light
     *
     */
    public Light(Vector3D position, Color color, double intensity) {
        super(position, color);
        setIntensity(intensity);
    }
    private double intensity;
    /**
     * @return intensity double
     *
     */
    public double getIntensity() {
        return intensity;
    }
    /**
     * @param  intensity double
     *
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public abstract double getNDotL(Intersection intersection);
    /**
     * @return basic intersection
     * */
    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
