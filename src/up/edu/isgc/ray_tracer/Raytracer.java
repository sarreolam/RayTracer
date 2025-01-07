package up.edu.isgc.ray_tracer;


import up.edu.isgc.ray_tracer.lights.DirectionalLight;
import up.edu.isgc.ray_tracer.lights.Light;
import up.edu.isgc.ray_tracer.lights.PointLight;
import up.edu.isgc.ray_tracer.lights.SpotLight;
import up.edu.isgc.ray_tracer.objects.*;
import up.edu.isgc.ray_tracer.tools.FileManager;
import up.edu.isgc.ray_tracer.tools.ObjReader;

import javax.imageio.ImageIO;

import static up.edu.isgc.ray_tracer.tools.Clamp.clamp;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Raytracer {

    static BufferedImage image;

    static int nThreads = 20;

    static double[][] depthOfFieldValues;



    /**
     * @author Arreola;
     * This is the ray tracer of Arreola
     * It implements bling-phong shininess, refleccion and refraccion
     * Depth of Field
     * Rotation and scale of obj
     */
    public static void main(String[] args) {
        System.out.println(new Date());
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 80, 80,
                800, 800, 2, 60));
        //Your scene objects go here

        raytrace(scene01);
        FileManager.saveImage(image, "imagen", "png");
    }

/**
 *  @author Arreola and Jafet
 *  @param scene with objects and lights already included
 *
 *  Fills the static image
 *
 * */
    public static void raytrace(Scene scene){
        Camera mainCamera = scene.getCamera();
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();

        image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        depthOfFieldValues = new double[mainCamera.getResolutionWidth()][mainCamera.getResolutionHeight()];
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                Runnable runnable = draw(i, j, mainCamera, objects, lights, posRaytrace);
                executorService.execute(runnable);
            }
        }
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(300, TimeUnit.MINUTES)){
                executorService.shutdownNow();
            }
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }finally {
            if (!executorService.isTerminated()){
                System.err.println("Cancel non-finished");
            }
        }
        executorService.shutdownNow();
        System.out.println(new Date());
    }

    /**
     *  @author Arreola
     *  @param scene with objects and lights already included
     *  @param start the frame of the with in which it will be starting
     *  @param end the frame of the with in which it will be starting
     *  Fills the static image from start to end
     *
     * */
    public static void raytrace(Scene scene, int start, int end){
        Camera mainCamera = scene.getCamera();
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();

        image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = start; i < Math.min(end, posRaytrace.length); i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                Runnable runnable = draw(i, j, mainCamera, objects, lights, posRaytrace);
                executorService.execute(runnable);
            }
        }
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(300, TimeUnit.MINUTES)){
                executorService.shutdownNow();
            }
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }finally {
            if (!executorService.isTerminated()){
                System.err.println("Cancel non-finished");
            }
        }
        executorService.shutdownNow();
        System.out.println(new Date());
    }

    /**
     * @author Arreola and Jafet
     * @param ray the casting ray
     * @param objects all objects in scene
     * @param caster except for camera just to check if its colliding with itself
     * @param clippingPlanes so it doesnt go far and beyond
     *
     * @return the closest intersection it finds
     *
     *
     *
     * */
    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().z();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }else{
                if(!caster.equals(currObj)){
                    Intersection intersection = currObj.getIntersection(ray);
                    if (intersection != null) {
                        double distance = intersection.getDistance();
                        if (distance >= 0 &&
                                (closestIntersection == null || distance < closestIntersection.getDistance())  //){
                                &&
                                (clippingPlanes == null || (distance >= clippingPlanes[0] && distance <= clippingPlanes[1]))) {
                            closestIntersection = intersection;
                        }
                    }
                }
            }
        }
        return closestIntersection;
    }

    /**
     * @author Arreola and Jafet
     * @param original
     * @param otherColor
     *
     * @return added color
     * */
    public static Color addColor(Color original, Color otherColor) {
        float red = (float) clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    /**
     * @author Arreola and Jafet
     * @param x coordinate
     * @param y coordinate
     * @param mainCamera camera in scene
     * @param objects object in scene
     * @param lights lights in scene
     * @param posRaytrace ray calculated from the camera
     *
     * Method so the parallelism work well
     *
     * */
    public static Runnable draw(int x, int y, Camera mainCamera,List<Object3D> objects, List<Light> lights, Vector3D[][] posRaytrace){
        Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                Color color = decideColor(x, y, mainCamera, objects, lights, posRaytrace);
                setRGB(x, y, color);
            }
        };
        return aRunnable;
    }


    /**
     * @author Arreola
     * @param scene scene
     * @param imgName name of the image with extension
     * @param depthOfFieldDistance in which it looks good
     * @param depthOfFieldRange how much will look good
     * @param depthOfFieldBlur how much will it blur
     *
     * creates new image
     * */
    public static void doDepthOfField(Scene scene, String imgName, double depthOfFieldDistance, double depthOfFieldRange, int depthOfFieldBlur){
        File file1 = new File(imgName);
        BufferedImage img1;
        try {
            img1 = ImageIO.read(file1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image = new BufferedImage(scene.getCamera().getResolutionWidth(), scene.getCamera().getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight();y++) {
                if (!(depthOfFieldValues[x][y]==-999)){
                    if(depthOfFieldValues[x][y]<depthOfFieldDistance+depthOfFieldRange && depthOfFieldValues[x][y]>depthOfFieldDistance-depthOfFieldRange){
                        setRGB(x, y, new Color(img1.getRGB(x, y)));
                    }else{
                        int sumR = 0, sumG = 0, sumB = 0;
                        for (int dy = -depthOfFieldBlur; dy <= depthOfFieldBlur; dy++) {
                            for (int dx = -depthOfFieldBlur; dx <= depthOfFieldBlur; dx++) {
                                if ((x + dx)>0 && (x + dx)<image.getWidth() && (y + dy)>0 && (y + dy)<image.getHeight()){
                                    Color pixel = new Color(img1.getRGB(x + dx, y + dy));
                                    sumR += pixel.getRed();
                                    sumG += pixel.getGreen();
                                    sumB += pixel.getBlue();
                                }
                            }
                        }
                        int div = (depthOfFieldBlur+depthOfFieldBlur+1)*(depthOfFieldBlur+depthOfFieldBlur+1);
                        setRGB(x, y, new Color(sumR/div, sumG/div, sumB/div));
                    }
                }else{
                    setRGB(x, y, Color.BLACK);
                }
            }
        }
    }


    /**
     * @author Arreola
     * @param i coordinate x
     * @param j coordinate j
     * @param mainCamera camera in scene
     * @param objects object in scene
     * @param lights lights in scene
     * @param posRaytrace ray calculated from the camera
     *
     * Calculates the pixel color with rays bouncing until one of the many conditions fail
     *
     * */
    public static Color decideColor(int i, int j, Camera mainCamera,List<Object3D> objects, List<Light> lights, Vector3D[][] posRaytrace){

        int bounceLimit = 5;
        Vector3D pos = mainCamera.getPosition();
        double cameraZ = pos.z();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();

        double x = posRaytrace[i][j].x() + pos.x();
        double y = posRaytrace[i][j].y() + pos.y();
        double z = posRaytrace[i][j].z() + pos.z();

        Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
        Intersection closestIntersection = raycast(ray, objects, null,
                new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});
        Color pixelColor = Color.BLACK;
        int cont = 0;
        double reflection = 1;
        boolean refracction = false;

        if (closestIntersection != null) {
            do {
                if (cont != 0){
                    refracction = false;
                    Vector3D nextRayDirection = null;
                    Vector3D incident = Vector3D.normalize(Vector3D.substract(closestIntersection.getPosition(), ray.getOrigin()));
                    if (closestIntersection.getObject().getRefraction() != 0){
                        double cosI = Vector3D.dotProduct(Vector3D.scalarMultiplication(incident, -1), closestIntersection.getNormal());
                        double eta = 1 / closestIntersection.getObject().getRefraction();
                        double sinT2 = Math.pow(eta, 2) * (1.0 - Math.pow(cosI, 2));
                        if (sinT2 <= 1.0) {
                            double cosT = Math.sqrt(1.0 - sinT2);
                            nextRayDirection = Vector3D.add(Vector3D.scalarMultiplication(incident, eta), Vector3D.scalarMultiplication(closestIntersection.getNormal(),eta *cosI - cosT ));
                        }
                    }
                    if(nextRayDirection == null){
                        nextRayDirection = Vector3D.substract(incident,
                            Vector3D.scalarMultiplication(closestIntersection.getNormal(), 2* Vector3D.dotProduct(incident, closestIntersection.getNormal()))
                            );
                    }

                    ray = new Ray(closestIntersection.getPosition(), Vector3D.normalize(nextRayDirection));
                    closestIntersection = raycast(ray, objects, closestIntersection.getObject(), new double[]{0.0,nearFarPlanes[1]});
                }else{
                    depthOfFieldValues[i][j] = closestIntersection.getDistance();
                }
                if (closestIntersection != null) {
                    cont += 1;
                    Color objColor = closestIntersection.getObject().getColor();
                    double objReflection = 1- closestIntersection.getObject().getReflection();
                    double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                    if (closestIntersection.getObject().getRefraction() != 0){
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= 0.5;
                        }
                    }else{
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= objReflection;
                        }
                    }
                    objColor = new Color(
                            (float) clamp(objColors[0], 0.0, 1.0),
                            (float) clamp(objColors[1], 0.0, 1.0),
                            (float) clamp(objColors[2], 0.0, 1.0));


                    for (Light light : lights) {
                        Ray rayToLight;
                        Intersection intersectionBeforeLight = null;
                        if (light.getClass().equals(PointLight.class)) {
                            rayToLight = new Ray(closestIntersection.getPosition(),
                                    Vector3D.normalize(
                                            Vector3D.substract(light.getPosition(), closestIntersection.getPosition())));
                            intersectionBeforeLight = raycast(rayToLight, objects, closestIntersection.getObject(),
                                    new double[]{0, Vector3D.magnitude(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()))}
                            );
                        }
                        else if (light.getClass().equals(DirectionalLight.class)) {
                            rayToLight = new Ray(closestIntersection.getPosition(), Vector3D.scalarMultiplication(((DirectionalLight) light).getDirection(), -1));
                            intersectionBeforeLight = raycast(rayToLight, objects, closestIntersection.getObject(), new double[]{0.0,nearFarPlanes[1]});
                        }
                        else if (light.getClass().equals(SpotLight.class)) {
                            Vector3D directionToLight = Vector3D.normalize(Vector3D.substract(closestIntersection.getPosition(), light.getPosition()));
                            double cosTheta = Vector3D.dotProduct(directionToLight, ((SpotLight) light).getDirection()) / Vector3D.magnitude(directionToLight)* Vector3D.magnitude(((SpotLight) light).getDirection());
                            if (Math.toDegrees(Math.acos(cosTheta)) < ((SpotLight) light).getAngle()) {
                                rayToLight = new Ray(closestIntersection.getPosition(),  directionToLight);
                                intersectionBeforeLight = raycast(rayToLight, objects, closestIntersection.getObject(),
                                        new double[]{0,Vector3D.magnitude(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()))});

                            }
                        }

                        if (intersectionBeforeLight == null) {
                            double nDotL = light.getNDotL(closestIntersection);
                            Color lightColor = light.getColor();
                            double intensity = light.getIntensity() * nDotL;
                            if (light.getClass().equals(PointLight.class)) {
                                double distance = Vector3D.magnitude(Vector3D.substract(closestIntersection.getPosition(), light.getPosition()));
                                double diffuse = intensity / Math.pow(distance, 1);
                                Vector3D L_V = Vector3D.add(ray.getDirection(), Vector3D.normalize(
                                        Vector3D.substract(closestIntersection.getPosition(), light.getPosition())));
                                Vector3D halfVector = Vector3D.scalarMultiplication(L_V, 1 / Vector3D.magnitude(L_V));

                                double specular = Math.pow(Vector3D.dotProduct(closestIntersection.getNormal(), halfVector), closestIntersection.getObject().getShininess());
                                intensity = diffuse + specular;
                            }
                            else if(light.getClass().equals(SpotLight.class)){
                                Vector3D directionToLight = Vector3D.normalize(Vector3D.substract(closestIntersection.getPosition(), light.getPosition()));
                                double cosTheta = Vector3D.dotProduct(directionToLight, ((SpotLight) light).getDirection()) / Vector3D.magnitude(directionToLight)* Vector3D.magnitude(((SpotLight) light).getDirection());
                                if (Math.toDegrees(Math.acos(cosTheta)) < ((SpotLight) light).getAngle()) {
                                    double distance = Vector3D.magnitude(Vector3D.substract(closestIntersection.getPosition(), light.getPosition()));
                                    double diffuse = intensity / Math.pow(distance, 1);
                                    Vector3D L_V = Vector3D.add(ray.getDirection(), Vector3D.normalize(
                                            Vector3D.substract(closestIntersection.getPosition(), light.getPosition())));
                                    Vector3D halfVector = Vector3D.scalarMultiplication(L_V, 1 / Vector3D.magnitude(L_V));

                                    double specular = Math.pow(Vector3D.dotProduct(closestIntersection.getNormal(), halfVector), closestIntersection.getObject().getShininess());
                                    intensity = diffuse + specular;
                                }
                            }
                            double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                            objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};

                            for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                                objColors[colorIndex] *= intensity * lightColors[colorIndex] * reflection;
                            }
                            Color diffuse = new Color(
                                    (float) clamp(objColors[0], 0.0, 1.0),
                                    (float) clamp(objColors[1], 0.0, 1.0),
                                    (float) clamp(objColors[2], 0.0, 1.0));
                            pixelColor = addColor(pixelColor, diffuse);
                        }
                    }
                    if(closestIntersection.getObject().getRefraction()!=0){
                        refracction = true;
                    }else{
                        reflection = reflection * (closestIntersection.getObject().getReflection());
                    }
                }
            } while (cont < bounceLimit && closestIntersection != null && (reflection > 0 || refracction));

        }else{
            depthOfFieldValues[i][j] = -999;
        }
        return pixelColor;
    }

/**
 * @author Arreola
 * @param x coordinate x
 * @param y coordinate j
 * @param pixelColor pixelColor
 * */
    public static synchronized void setRGB(int x, int y, Color pixelColor){
        image.setRGB(x, y, pixelColor.getRGB());
    }

}
