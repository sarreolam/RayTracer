package up.edu.isgc.ray_tracer.tools;

import up.edu.isgc.ray_tracer.Vector3D;
import up.edu.isgc.ray_tracer.objects.Model3D;
import up.edu.isgc.ray_tracer.objects.Triangle;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static up.edu.isgc.ray_tracer.tools.Clamp.clamp;
/**
 * @author Arreola and Jafet
 *
 *
 * Diferent methods to manage diferent parameters, if not its set to default
 * */
public class ObjReader {
    public static Model3D getModel3D(String path, Vector3D origin, Color color) {
         return getModel3D( path,  origin,color, 0,0,0 , 1 );
    }
    public static Model3D getModel3D(String path, Vector3D origin, Color color, double scale) {
        return getModel3D( path,  origin,color, 0,0,0 , scale );
    }
    public static Model3D getModel3D(String path, Vector3D origin, Color color, double angRotX, double angRotY, double angRotZ) {
         return getModel3D( path,  origin,color, angRotX,angRotY,angRotZ , 1 );
    }
    public static Model3D getModel3D(String path, Vector3D origin, Color color, double angRotX, double angRotY, double angRotZ, double scale) {
        try {
            angRotX = Math.toRadians(angRotX);
            angRotY = Math.toRadians(angRotY);
            angRotZ = Math.toRadians(angRotZ);
//            scale = Math.max(scale, 0.0);

            Vector3D[] rotX = new Vector3D[]{ new Vector3D( 1,0,0),new Vector3D(0, Math.cos(angRotX), -Math.sin(angRotX)), new Vector3D(0, Math.sin(angRotX), Math.cos(angRotX))};
            Vector3D[] rotY = new Vector3D[]{ new Vector3D(Math.cos(angRotY), 0, Math.sin(angRotY)), new Vector3D( 0,1,0),new Vector3D(-Math.sin(angRotY), 0, Math.cos(angRotY))};
            Vector3D[] rotZ = new Vector3D[]{ new Vector3D(Math.cos(angRotZ),  -Math.sin(angRotZ), 0), new Vector3D(Math.sin(angRotZ), Math.cos(angRotZ), 0), new Vector3D( 0,0,1)};

            BufferedReader reader = new BufferedReader(new FileReader(path));

            List<Triangle> triangles = new ArrayList<>();
            List<Vector3D> vertices = new ArrayList<>();
            List<Vector3D> normals = new ArrayList<>();
            String line;
            int defaultSmoothingGroup = -1;
            int smoothingGroup = defaultSmoothingGroup;
            Map<Integer, List<Triangle>> smoothingMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ") || line.startsWith("vn ")) {
                    String[] vertexComponents = line.split("(\\s)+");
                    if (vertexComponents.length >= 4) {
                        double x = Double.parseDouble(vertexComponents[1]);
                        double y = Double.parseDouble(vertexComponents[2]);
                        double z = Double.parseDouble(vertexComponents[3]);
                        Vector3D vec = new Vector3D(x, y, z);
                        double[] result = new double[]{0,0,0};
                        for (int i = 0; i < 3; i++) {
                            result[i] = Vector3D.dotProduct(rotX[i], vec);
                        }
                        vec = new Vector3D(result[0], result[1], result[2]);
                        for (int i = 0; i < 3; i++) {
                            result[i] = Vector3D.dotProduct(rotY[i], vec);
                        }
                        vec = new Vector3D(result[0], result[1], result[2]);
                        for (int i = 0; i < 3; i++) {
                            result[i] = Vector3D.dotProduct(rotZ[i], vec);
                        }
                        Vector3D newVec = new Vector3D(result[0], result[1], result[2]);

                        if (line.startsWith("v ")) {
                            vertices.add(Vector3D.scalarMultiplication( newVec, scale));
                        } else {
                            normals.add(newVec);
                        }
                    }
                } else if (line.startsWith("f ")) {
                    String[] faceComponents = line.split("(\\s)+");
                    List<Integer> faceVertex = new ArrayList<>();
                    List<Integer> faceNormals = new ArrayList<>();

                    for (int i = 1; i < faceComponents.length; i++) {
                        String[] infoVertex = faceComponents[i].split("/");
                        if (infoVertex.length >= 1) {
                            int vertexIndex = Integer.parseInt(infoVertex[0]);
                            faceVertex.add(vertexIndex);
                        }
                        if (infoVertex.length >= 3) {
                            int normalIndex = Integer.parseInt(infoVertex[2]);
                            faceNormals.add(normalIndex);
                        }
                    }

                    if (faceVertex.size() >= 3) {
                        Vector3D[] triangleVertices = new Vector3D[faceVertex.size()];
                        Vector3D[] triangleNormals = new Vector3D[faceNormals.size()];

                        for (int i = 0; i < faceVertex.size(); i++) {
                            if (faceVertex.get(i)<0){
                                triangleVertices[i] = vertices.get(vertices.size()+faceVertex.get(i));
                            }else{
                                triangleVertices[i] = vertices.get(faceVertex.get(i) - 1);
                            }
                        }

                        Vector3D[] arrangedTriangleVertices = null;
                        Vector3D[] arrangedTriangleNormals = null;
                        if(normals.size() > 0 && !faceNormals.isEmpty()) {
                            for(int i = 0; i < faceNormals.size(); i++) {
                                if (faceNormals.get(i)<0){
                                    triangleNormals[i] = normals.get(normals.size() + faceNormals.get(i));
                                }else{
                                    triangleNormals[i] = normals.get(faceNormals.get(i) - 1);
                                }
                            }
                            arrangedTriangleNormals = new Vector3D[]{triangleNormals[1], triangleNormals[0], triangleNormals[2]};
                        }
                        arrangedTriangleVertices = new Vector3D[]{triangleVertices[1], triangleVertices[0], triangleVertices[2]};

                        Triangle tmpTriangle = new Triangle(arrangedTriangleVertices, arrangedTriangleNormals);
                        triangles.add(tmpTriangle);

                        List<Triangle> trianglesInMap = smoothingMap.get(smoothingGroup);
                        if(trianglesInMap == null) {
                            trianglesInMap = new ArrayList<>();
                        }
                        trianglesInMap.add(tmpTriangle);

                        if (faceVertex.size() == 4) {
                            arrangedTriangleVertices = new Vector3D[]{triangleVertices[2], triangleVertices[0], triangleVertices[3]};
                            if(arrangedTriangleNormals != null){
                                arrangedTriangleNormals = new Vector3D[]{triangleNormals[2], triangleNormals[0], triangleNormals[3]};
                            }
                            tmpTriangle = new Triangle(arrangedTriangleVertices, arrangedTriangleNormals);
                            triangles.add(tmpTriangle);
                            trianglesInMap.add(tmpTriangle);
                        }

                        if(smoothingGroup != defaultSmoothingGroup) {
                            smoothingMap.put(smoothingGroup, trianglesInMap);
                        }
                    }
                } else if(line.startsWith("s ")) {
                    String[] smoothingComponents = line.split("(\\s)+");
                    if(smoothingComponents.length > 1) {
                        if(smoothingComponents[1].equals("off")){
                            smoothingGroup = defaultSmoothingGroup;
                        } else {
                            try {
                                smoothingGroup = Integer.parseInt(smoothingComponents[1]);
                            } catch (NumberFormatException nfe){
                                smoothingGroup = defaultSmoothingGroup;
                            }
                        }
                    }
                }
            }
            reader.close();

            class NormalPair{
                Vector3D normal;
                int count;

                public NormalPair() {
                    normal = Vector3D.ZERO();
                    count = 0;
                }
            }

            //Smooth vertices normals
            for (Integer key : smoothingMap.keySet()) {
                Map<Vector3D, NormalPair> vertexMap = new HashMap<>();
                List<Triangle> trianglesInMap = smoothingMap.get(key);
                for (Triangle triangle : trianglesInMap) {
                    Vector3D[] triangleVertices = triangle.getVertices();
                    Vector3D[] triangleNormals = triangle.getNormals();
                    for (int i = 0; i < triangleVertices.length; i++) {
                        NormalPair normalsVertex = vertexMap.get(triangleVertices[i]);
                        if (normalsVertex == null) {
                            normalsVertex = new NormalPair();
                        }
                        if (triangleNormals.length > 0 && i < triangleNormals.length) {
                            normalsVertex.normal = Vector3D.add(normalsVertex.normal, triangleNormals[i]);
                            normalsVertex.count++;
                        }
                        vertexMap.put(triangleVertices[i], normalsVertex);
                    }
                }
                for (Triangle triangle : trianglesInMap) {
                    Vector3D[] triangleVertices = triangle.getVertices();
                    Vector3D[] triangleNormals = triangle.getNormals();
                    for (int i = 0; i < triangleVertices.length; i++) {
                        NormalPair normalsVertex = vertexMap.get(triangleVertices[i]);
                        triangleNormals[i] = Vector3D.scalarMultiplication(normalsVertex.normal, 1.0 / (double) normalsVertex.count);
                    }
                    triangle.setNormals(
                           Vector3D.normalize(triangleNormals[0]),
                           Vector3D.normalize(triangleNormals[1]),
                           Vector3D.normalize(triangleNormals[2])
                    );
                }
            }

            return new Model3D(origin, triangles.toArray(new Triangle[triangles.size()]), color);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return null;
    }
}


