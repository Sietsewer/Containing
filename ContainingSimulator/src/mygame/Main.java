package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    ServerListener listener;
    Spatial sky_geo;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Main app = new Main();


        app.start();
    }

    /**
     *
     */
    @Override
    public void simpleInitApp() {
        
        loadAssets();
        flyCam.setMoveSpeed(100f);

    }

    /**
     *
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        sky_geo.setLocalTranslation(cam.getLocation());
    }

    /**
     *
     * @param rm
     */
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    void loadAssets(){
        sky_geo = assetManager.loadModel("Models/SkyBox/SkyBox.j3o");
        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture sky_text = assetManager.loadTexture("Textures/SkyBox_1.jpg");
        skyMat.setTexture("ColorMap", sky_text);
        sky_geo.setMaterial(skyMat);
        sky_geo.setQueueBucket(RenderQueue.Bucket.Sky);//Makes it so that sky is always rendered last, and in the background.
        sky_geo.scale(1000f);
        rootNode.attachChild(sky_geo);
        Spatial dock = assetManager.loadModel("Models/dockBase/dockBase.j3o");
        
        Quad waterQuad = new Quad(1550f,600f);
        Geometry waterGeo = new Geometry("Quad", waterQuad);
        waterGeo.rotate(-(float)Math.PI/2,0f,0f);

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        sun.setDirection(new Vector3f(.5f, -.5f, -.5f).normalizeLocal());
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);
        rootNode.addLight(sun);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(26f/255, 126f/255, 168f/255, 1f));
        waterGeo.setMaterial(mat);
        rootNode.attachChild(dock);
        rootNode.attachChild(waterGeo);
        listener = new ServerListener(this);
    }

    void sendMessage(Message Message) {
        listener.sendMessage(Message);
    }

    void messageRecieved(Message decodedMessage) {
    }
}
