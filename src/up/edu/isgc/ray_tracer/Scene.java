package up.edu.isgc.ray_tracer;

import up.edu.isgc.ray_tracer.lights.Light;
import up.edu.isgc.ray_tracer.objects.Camera;
import up.edu.isgc.ray_tracer.objects.Object3D;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Arreola and Jafet
 * scene that holds camera, objects and lights
 * */
public class Scene {
    private Camera camera;
    private List<Object3D> objects;
    private List<Light> lights;

    public Scene() {
        setObjects(new ArrayList<>());
        setLights(new ArrayList<>());
    }
    /**
     * @return camera
     * */
    public Camera getCamera() {
        return camera;
    }
    /**
     * @param camera camera
     * */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @param object Object3D
     * */
    public void addObject(Object3D object){
        getObjects().add(object);
    }
    /**
     * @return Object3D objects
     * */
    public List<Object3D> getObjects() {
        if(objects == null){
            objects = new ArrayList<>();
        }
        return objects;
    }
    /**
     * @param objects List Object3D
     * */
    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    /**
     * @param lights List Light
     * */
    public void setLights(List<Light> lights){this.lights = lights;}

    /**
     * @param light Light
     * */
    public void addLight(Light light){
        getLights().add(light);
    }
    /**
     * @return lights List Light
     * */
    public List<Light> getLights() {
        if(lights == null){
            lights = new ArrayList<>();
        }
        return lights;
    }
}
