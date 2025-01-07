package up.edu.isgc.ray_tracer;

import java.util.Vector;
/**
 * @author Arreola and Jafet
 *
 * */
public class Vector3D {

    private static final Vector3D ZERO = new Vector3D(0.0, 0.0, 0.0);
    private double x, y, z;

    /**
     * @param x x
     * @param y y
     * @param z z
     *
     * */
    public Vector3D(double x, double y, double z){
        setVector(x, y, z);
    }

    /**
     * @param x x
     * @param y y
     * @param z z
     *
     * */
    public void setVector(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }
    /**
     * @return x
     *
     * */
    public double x() {
        return x;
    }
    /**
     * @param x x
     *
     * */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * @return y
     *
     * */
    public double y() {
        return y;
    }
    /**
     * @param y y
     *
     * */
    public void setY(double y) {
        this.y = y;
    }
    /**
     * @return z
     *
     * */
    public double z() { return z;}
    /**
     * @param z z
     *
     * */
    public void setZ(double z) {this.z = z; }

    public Vector3D clone(){
        return new Vector3D(x(), y(), z());
    }
    /**
     * @return ZERO
     *
     * */
    public static Vector3D ZERO(){
        return ZERO.clone();
    }

    @Override
    public String toString(){
        return "Vector3D{" +
                "x=" + x() +
                ", y=" + y() +
                ", z=" + z() +
                "}";
    }
    /**
     * @param v1 Vector3D
     * @param v2 Vector3D
     *
     * */
    public static Vector3D substract(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x() - v2.x(),v1.y() - v2.y(),v1.z() - v2.z());
    }
    /**
     * @param v1 Vector3D
     * @param v2 Vector3D
     *
     * */
    public static Vector3D add(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x() + v2.x(),v1.y() + v2.y(),v1.z() + v2.z());
    }
    /**
     * @param v1 Vector3D
     * @param v2 Vector3D
     *
     * */
    public static double dotProduct(Vector3D v1, Vector3D v2){
        return  v1.x()* v2.x() + v1.y()* v2.y()+ v1.z()* v2.z();
    }
    /**
     * @param vectorB Vector3D
     * @param vectorA Vector3D
     *
     * */
    public static Vector3D crossProduct(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D((vectorA.y() * vectorB.z()) - (vectorA.z() * vectorB.y()),
                (vectorA.z() * vectorB.x()) - (vectorA.x() * vectorB.z()),
                (vectorA.x() * vectorB.y()) - (vectorA.y() * vectorB.x()));
    }
    /**
     * @param vectorA Vector3D
     *
     * */
    public static double magnitude (Vector3D vectorA){
        return Math.sqrt(dotProduct(vectorA, vectorA));
    }
    /**
     * @param vectorA Vector3D
     *
     * */
    public static Vector3D normalize(Vector3D vectorA){
        double mag = Vector3D.magnitude(vectorA);
        return new Vector3D(vectorA.x() / mag, vectorA.y() / mag, vectorA.z() / mag);
    }
    /**
     * @param vectorA Vector3D
     * @param scalar Int
     *
     * */
    public static Vector3D scalarMultiplication(Vector3D vectorA, double scalar){
        return new Vector3D(vectorA.x() * scalar, vectorA.y() * scalar, vectorA.z() * scalar);
    }


}
