package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;

import static up.edu.isgc.ray_tracer.tools.Clamp.clamp;
/**
 * @author Arreola and Jafet
 */
public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double shininess;
    private double reflection;
    private double refraction;

    /**
     * @param v3 position
     * @param color color of the model
     */
    public Object3D(Vector3D v3, Color color){
        setPosition(v3);
        setColor(color);
        setShininess(16);
        setReflection(0);
        setRefraction(0);
    }
    /**
     * @return refraction
     */

    public double getRefraction() {
        return refraction;
    }

    /**
     * @param refraction double
     */
    public void setRefraction(double refraction) {
        if(getReflection()!=0){
            setReflection(0);
        }
        this.refraction = refraction;
    }
    /**
     * @return reflection
     */

    public double getReflection() {
        return reflection;
    }
    /**
     * @param reflection double
     */
    public void setReflection(double reflection) {
        reflection = clamp(reflection, 0,1);
        if(getRefraction()!=0){
            setRefraction(0);
        }
        this.reflection = reflection;
    }
    /**
     * @param shininess double
     */
    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    /**
     * @return shininess double
     */
    public double getShininess() {
        return shininess;
    }
    /**
     * @return color
     */

    public Color getColor() {
        return color;
    }
    /**
     * @return position Vector3
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * @param color Color of the object
     */
    public void setColor(Color color) {
        this.color = color;
    }
    /**
     * @param position Vector3D
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }
    public abstract Intersection getIntersection(Ray ray);


}
