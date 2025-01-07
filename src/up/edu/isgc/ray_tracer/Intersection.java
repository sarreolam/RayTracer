package up.edu.isgc.ray_tracer;

import up.edu.isgc.ray_tracer.objects.Object3D;
/**
 * @author Arreola and Jafet
 *
 * */
public class Intersection {
    private double distance;
    private Vector3D position;
    private Vector3D normal;
    private Object3D object;


    /**
     * @param position Vector3D
     * @param distance double
     * @param normal Vector3D
     * @param object Object3D
     *
     * */
    public Intersection(Vector3D position, double distance, Vector3D normal, Object3D object) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }
    /**
     * @return position Vector3D
     *
     * */
    public Vector3D getPosition() {
        return position;
    }
    /**
     * @return distance double
     *
     * */
    public double getDistance() {
        return distance;
    }
    /**
     * @return object Object3D
     *
     * */
    public Object3D getObject() {
        return object;
    }
    /**
     * @return normal Vector3D
     *
     * */
    public Vector3D getNormal() {
        return normal;
    }
    /**
     * @param  distance double
     *
     * */
    public void setDistance(double distance) {
        this.distance = distance;
    }
    /**
     * @param  normal Vector3D
     *
     * */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }
    /**
     * @param  position Vector3D
     *
     * */
    public void setPosition(Vector3D position) {
        this.position = position;
    }
    /**
     * @param  object Object3D
     *
     * */
    public void setObject(Object3D object) {
        this.object = object;
    }
}
