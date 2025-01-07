package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;

/**
 * @author Arreola and Jafet
 */
public interface IIntersectable {
    public abstract Intersection getIntersection(Ray ray);
}
