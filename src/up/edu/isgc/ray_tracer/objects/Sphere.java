package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public class Sphere extends Object3D {
    private double radius;

    /**
     * @param v3 Vector3D position
     * @param radius double
     * @param color Color of the object
     */
    public Sphere(Vector3D v3, double radius, Color color){
        super(v3, color);
        setRadius(radius);}

    /**
     * @param radius double
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
    /**
     * @return  radius double
     */
    public double getRadius() {
        return radius;
    }
    /**
     * @param ray to calculate intersection
     * @return intersection if got hit
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D L = Vector3D.substract(getPosition(),ray.getOrigin());
        double tca = Vector3D.dotProduct(L, ray.getDirection());
        double L2 = Math.pow(Vector3D.magnitude(L), 2);
        double d2 = L2 - Math.pow(tca, 2);
        if(d2 >= 0){
            double d = Math.sqrt(d2);
            double t0 = tca - Math.sqrt(Math.pow(getRadius(), 2) - Math.pow(d, 2));
            double t1 = tca + Math.sqrt(Math.pow(getRadius(), 2) - Math.pow(d, 2));

            double distance = Math.min(t0, t1);
            Vector3D position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
            Vector3D normal = Vector3D.normalize(Vector3D.substract(position, getPosition()));
            return new Intersection(position, distance, normal, this);
        }
        return null;
    }





}
