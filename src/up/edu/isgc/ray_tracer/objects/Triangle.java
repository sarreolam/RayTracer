package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 */
public class Triangle implements IIntersectable{

    public final double EPSILON = 0.00000000000000001;
    private Vector3D[] vertices;
    private Vector3D[] normals;

    /**
     * @param v0 position1
     * @param v1 position2
     * @param v2 position3
     */
    public Triangle(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(v0, v1, v2);
        setNormals(null);
    }
    /**
     * @param vertices Vector3D[3]
     * @param normals Vector3D[3]
     */
    public Triangle(Vector3D[] vertices, Vector3D[] normals) {
        if(vertices.length == 3){
            setVertices(vertices[0], vertices[1], vertices[2]);
        } else {
            setVertices(Vector3D.ZERO(),Vector3D.ZERO(),Vector3D.ZERO());
        }
        setNormals(normals);
    }

    /**
     *
     * @return vertices Vector3D[]
     */
    public Vector3D[] getVertices() {
        return vertices;
    }

    /**
     *
     * @param vertices Vector3D[]
     */
    private void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }
    /**
     * @param v0 position1
     * @param v1 position2
     * @param v2 position3
     */
    public void setVertices(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(new Vector3D[]{v0, v1, v2});
    }
    /**
     * @return normal Vector3D
     */
    public Vector3D getNormal(){
        Vector3D normal = Vector3D.ZERO();
        Vector3D[] normals = this.normals;

        if(normals ==null) {
            Vector3D[] vertices = getVertices();
            Vector3D v = Vector3D.substract(vertices[1], vertices[0]);
            Vector3D w = Vector3D.substract(vertices[0], vertices[2]);
            normal = Vector3D.normalize(Vector3D.crossProduct(v, w));
        } else{
            for(int i = 0; i < normals.length; i++){
                normal.setX(normal.x() + normals[i].x());
                normal.setY(normal.y() + normals[i].y());
                normal.setZ(normal.z() + normals[i].z());
            }
            normal.setX(normal.x() / normals.length);
            normal.setY(normal.y() / normals.length);
            normal.setZ(normal.z() / normals.length);
        }
        return normal;
    }
    /**
     * @return normals Vector3D[]
     */
    public Vector3D[] getNormals() {
        if(normals == null) {
            Vector3D normal = getNormal();
            setNormals(new Vector3D[]{normal, normal, normal});
        }
        return normals;
    }
    /**
     * @param n0 position
     * @param n1 position
     * @param n2 position
     */
    public void setNormals(Vector3D n0, Vector3D n1, Vector3D n2){
        setNormals(new Vector3D[]{n0, n1, n2});
    }
    /**
     * @param normals Vector3D[]
     */
    private void setNormals(Vector3D[] normals) {
        this.normals = normals;
    }
    /**
     * @param ray to calculate intersection
     * @return intersection if got hit
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null);

        Vector3D[] vert = getVertices();
        Vector3D v2v0 = Vector3D.substract(vert[2], vert[0]);
        Vector3D v1v0 = Vector3D.substract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double det = Vector3D.dotProduct(v2v0, vectorP);
        double invDet = 1.0 / det;
        Vector3D vectorT = Vector3D.substract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector3D.dotProduct(vectorT, vectorP);

        if (!(u < 0 || u > 1)) {
            Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
            double v = invDet * Vector3D.dotProduct(ray.getDirection(), vectorQ);
            if (!(v < 0 || (u + v) > (1.0 + EPSILON))) {
                double t = invDet * Vector3D.dotProduct(vectorQ, v1v0);
                intersection.setDistance(t);
            }
        }

        return intersection;
    }
}
