package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;
import up.edu.isgc.ray_tracer.tools.Barycentric;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Arreola and Jafet
 */
public class Model3D extends Object3D{

    public List<Triangle> triangles;
    /**
     * @param v3 position
     * @param triangles all faces
     * @param color color of the model
     */
    public Model3D(Vector3D v3, Triangle[] triangles, Color color) {
        super(v3, color);
        setTriangles(triangles);
    }

    /**
     * @return triangles, faces
     */

    public List<Triangle> getTriangles() {
        return triangles;
    }

    /**
     * @param triangles faces
     *
     * the vertices move with the position
     */
    public void setTriangles(Triangle[] triangles) {
        Vector3D position = getPosition();
        Set<Vector3D> uniqueVertices = new HashSet<>();

        for(Triangle triangle : triangles){
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for(Vector3D vertex : uniqueVertices){
            vertex.setX(vertex.x() + position.x());
            vertex.setY(vertex.y() + position.y());
            vertex.setZ(vertex.z() + position.z());
        }
        this.triangles = Arrays.asList(triangles);
    }

    /**
     * @param ray to calculate intersection
     * @return intersection if got hit
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector3D position = Vector3D.ZERO();
        Vector3D normal = Vector3D.ZERO();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if (intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)) {
                distance = intersectionDistance;
                position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
                normal = Vector3D.ZERO();
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                Vector3D[] normals = triangle.getNormals();
                for (int i = 0; i < uVw.length; i++) {
                    normal = Vector3D.add(normal, Vector3D.scalarMultiplication(normals[i], uVw[i]));
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(position, distance, normal, this);
    }

}
