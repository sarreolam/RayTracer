package up.edu.isgc.ray_tracer.objects;

import up.edu.isgc.ray_tracer.Intersection;
import up.edu.isgc.ray_tracer.Ray;
import up.edu.isgc.ray_tracer.Vector3D;

import java.awt.*;
/**
 * @author Arreola and Jafet
 *
 * creates the camera with spesifications for scene
 * */
public class Camera extends Object3D {
    //FOV[0] = Horizontal | FOV[1] = Vertical
    private double[] fieldOfView = new double[2];
    private double defaultZ = 10;
    private int[] resolution = new int[2];
    private double[] nearFarPlanes = new double[2];

    /**
     * @param position Vector3D
     * @param fovH double
     * @param fovV double
     * @param width int
     * @param height int
     * @param nearPlane double
     * @param farPlane double
     *
     * */
    public Camera(Vector3D position, double fovH, double fovV,
                  int width, int height, double nearPlane, double farPlane) {
        super(position, Color.BLACK);
        setFOV(fovH, fovV);
        setResolution(width, height);
        setNearFarPlanes(new double[]{nearPlane, farPlane});
    }
/**
 * @return fieldOfView
 * */
    public double[] getFieldOfView() {
        return fieldOfView;
    }
    /**
     * @param fieldOfView double[2]
     * */
    private void setFieldOfView(double[] fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
    /**
     * @return fieldOfView[0]
     * */
    public double getFOVHorizontal() {
        return fieldOfView[0];
    }
    /**
     * @return fieldOfView[1]
     * */
    public double getFOVVertical() {
        return fieldOfView[1];
    }

    /**
     * @param fovH double
     * */
    public void setFOVHorizontal(double fovH) {
        fieldOfView[0] = fovH;
    }
    /**
     * @param fovV double
     * */
    public void setFOVVertical(double fovV) {
        fieldOfView[1] = fovV;
    }
    /**
     * @param fovH double
     * @param fovV double
     * */
    public void setFOV(double fovH, double fovV) {
        setFOVHorizontal(fovH);
        setFOVVertical(fovV);
    }
    /**
     * @return defaultZ
     * */
    public double getDefaultZ() {
        return defaultZ;
    }
    /**
     * @param defaultZ double
     * */

    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }
    /**
     * @return resolution int[]
     * */
    public int[] getResolution() {
        return resolution;
    }
    /**
     * @param width int
     * */
    public void setResolutionWidth(int width) {
        resolution[0] = width;
    }
    /**
     * @param height int
     * */
    public void setResolutionHeight(int height) {
        resolution[1] = height;
    }
    /**
     * @param height int
     * @param width int
     * */
    public void setResolution(int width, int height) {
        setResolutionWidth(width);
        setResolutionHeight(height);
    }
    /**
     * @return resolution[0]
     * */
    public int getResolutionWidth() {
        return resolution[0];
    }
    /**
     * @return resolution[1]
     * */
    public int getResolutionHeight() {
        return resolution[1];
    }
    /**
     * @param resolution int[2]
     * */
    private void setResolution(int[] resolution) {
        this.resolution = resolution;
    }
    /**
     * @return nearFarPlanes
     * */
    public double[] getNearFarPlanes() {
        return nearFarPlanes;
    }
    /**
     * @return nearFarPlanes
     * */
    private void setNearFarPlanes(double[] nearFarPlanes) {
        this.nearFarPlanes = nearFarPlanes;
    }

    /**
     * @return Vector3D[][] with all the position and direction of the rays depending on the resolution and the fov
     * */

    public Vector3D[][] calculatePositionsToRay() {
        double angleMaxX = getFOVHorizontal() / 2.0;
        double radiusMaxX = getDefaultZ() / Math.cos(Math.toRadians(angleMaxX));

        double maxX = Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
        double minX = -maxX;

        double angleMaxY = getFOVVertical() / 2.0;
        double radiusMaxY = getDefaultZ() / Math.cos(Math.toRadians(angleMaxY));

        double maxY = Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
        double minY = -maxY;

        Vector3D[][] positions = new Vector3D[getResolutionWidth()][getResolutionHeight()];
        double posZ = defaultZ;

        double stepX = (maxX - minX) / getResolutionWidth();
        double stepY = (maxY - minY) / getResolutionHeight();
        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[x].length; y++) {
                double posX = minX + (stepX * x);
                double posY = maxY - (stepY * y);
                positions[x][y] = new Vector3D(posX, posY, posZ);
            }
        }
        return positions;
    }
    /**
     * @return basic intersection
     * */
    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}