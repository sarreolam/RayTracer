package up.edu.isgc.ray_tracer;

import up.edu.isgc.ray_tracer.objects.Camera;
import up.edu.isgc.ray_tracer.objects.Sphere;
/**
 * @author Arreola and Jafet
 *
 * */
public class Ray {

    private Vector3D origin;

    private Vector3D direction;

    /**
     * @param origin Vector3D
     * @param direction Vector3D
     *
     * */
    public Ray(Vector3D origin, Vector3D direction){
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * @return Vector3D normalized
     *
     * */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }
    /**
     * @return Vector3D
     *
     * */
    public Vector3D getOrigin() {
        return origin;
    }
    /**
     * @param direction Vector3D
     *
     * */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
    /**
     * @param origin Vector3D
     *
     * */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

}
