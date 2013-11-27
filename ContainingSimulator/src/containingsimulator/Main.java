package containingsimulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    static ServerListener listener;
    Spatial sky_geo;
    Spatial agvModel;
    /*
     Seacrane spatials
     */
    Spatial scModel;
    Spatial scSModel;
    Spatial scHModel;
    /*
     Buffercrane spatials
     */
    Spatial bcModel;
    Spatial bcSModel;
    Spatial bcHModel;
    /*
     Lorrycrane spatials
     */
    Spatial lcModel;
    Spatial lcHModel;
    /*
     crane 
     */
    Crane[] seaCranes = new Crane[10];
    Crane[] bufCranes = new Crane[63];
    Crane[] lorCranes = new Crane[20];
    Crane[] trainCranes = new Crane[4];
    Crane[] barCranes = new Crane[8];
    
    ArrayList<Transporter> transporters;
    
    Buffer[] buffers;
    public static float globalSpeed = 1f;

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
        flyCam.setMoveSpeed(400f);
        cam.setFrustumFar(5000f);
        listener = new ServerListener(this);
        new Thread(new Runnable() {
            public void run() {
                listener.run();
            }
        }).start();
    }

    /**
     *
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        sky_geo.setLocalTranslation(cam.getLocation());
        for(Crane c : seaCranes)
        {
            c.update(tpf);
        }
        for(Crane c : lorCranes)
        {
            c.update(tpf);
        }
        for(Crane c : bufCranes)
        {
            c.update(tpf);
        }
        for(Crane c : barCranes)
        {
            c.update(tpf);
        }
        for(Crane c : trainCranes)
        {
            c.update(tpf);
        }
    }

    /**
     *
     * @param rm
     */
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    void loadAssets() {

        Path.createPath();
        //Init of the AGV viewmodel.
        agvModel = assetManager.loadModel("Models/AGV/AGV.j3o");
        Material avgMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        avgMat.setColor("Color", ColorRGBA.Orange);
        agvModel.setMaterial(avgMat);

        //Init of skybox geometry, material, and texture.
        sky_geo = assetManager.loadModel("Models/SkyBox/SkyBox.j3o");
        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture sky_text = assetManager.loadTexture("Textures/SkyBox_1.jpg");
        skyMat.setTexture("ColorMap", sky_text);
        sky_geo.setMaterial(skyMat);
        sky_geo.setQueueBucket(RenderQueue.Bucket.Sky);
        sky_geo.scale(1000f);
        rootNode.attachChild(sky_geo);

        //Init Container
        Container.makeGeometry(assetManager);

        //Init of the SeaCrane viewmodel
        scModel = assetManager.loadModel("Models/seacrane/seacrane.j3o");
        scSModel = assetManager.loadModel("Models/seacrane/seacrane_slider.j3o");
        scHModel = assetManager.loadModel("Models/seacrane/seacrane_slider_hook.j3o");
        Material scMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        scMat.setColor("Color", ColorRGBA.Yellow);
        scModel.setMaterial(scMat);
        scSModel.setMaterial(scMat);
        scHModel.setMaterial(scMat);

        //Init of the BufferCrane viewmodel
        bcModel = assetManager.loadModel("Models/buffercrane/buffercrane.j3o");
        bcSModel = assetManager.loadModel("Models/buffercrane/buffercrane_slider.j3o");
        bcHModel = assetManager.loadModel("Models/buffercrane/buffercrane_slider_hook.j3o");
        Material bcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        bcMat.setColor("Color", ColorRGBA.Yellow);
        bcModel.setMaterial(bcMat);
        bcSModel.setMaterial(bcMat);
        bcHModel.setMaterial(bcMat);
        
        //Init lorryCrane
        lcModel = assetManager.loadModel("Models/lorrycrane/lorrycrane.j3o");
        Material lcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lcMat.setColor("Color", ColorRGBA.Yellow);
        lcModel.setMaterial(lcMat);
        init_SeaCranes();
        init_BargeCranes();
        init_BufferCranes();
        init_LorryCranes();
        init_TrainCranes();

        //Init Transporters
        Transporter.makeGeometry(assetManager);
        transporters = new ArrayList<Transporter>();

        //Init empty buffers
        buffers = new Buffer[63];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer();
            rootNode.attachChild(buffers[i]);
            //some magic number abuse here, snap buffers to proper location on map:
            buffers[i].setLocalTranslation(111.76f + (i * 22.035f), 11, 115);
            buffers[i].addParkingSpots(buffers[i].getLocalTranslation());
        }

        //Init of the small blue plane, representing water.
        Quad waterQuad = new Quad(1550f, 600f);
        Geometry waterGeo = new Geometry("Quad", waterQuad);
        waterGeo.rotate(-(float) Math.PI / 2, 0f, 0f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(26f / 255, 126f / 255, 168f / 255, 1f));
        waterGeo.setMaterial(mat);
        rootNode.attachChild(waterGeo);
        waterGeo.setLocalTranslation(0f, 0f, 600f);

        //Init of lightsources of the project.
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        sun.setDirection(new Vector3f(.5f, -.5f, -.5f).normalizeLocal());
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);
        rootNode.addLight(sun);

        //Init of the dock spatial, the base of the simulator.
        Spatial dock = assetManager.loadModel("Models/dockBase/dockBase.j3o");
        rootNode.attachChild(dock);
        dock.setLocalTranslation(0f, 0f, 600f);
    }

    static void sendMessage(Message Message) {
        listener.sendMessage(Message);
    }

    void messageRecieved(Message decodedMessage) {
        Object[] params = decodedMessage.getParameters();
        switch (decodedMessage.getCommand()) {
            case 1: //MOVE
                String agvID1 = (String) params[0];
                String[] pathIDs1 = new String[params.length - 1];
                for (int i = 0; i < pathIDs1.length; i++) {
                    pathIDs1[i] = (String) params[i + 1];
                }
                //TODO: get AGV from ID, get waypoints from IDs
                break;
            case 2: //PICKUP_CONTAINER
                Crane crane1 = getCraneByID((String) params[0]);
                Container cont1 = getContainerByID((String) params[1]);
                //param[2],[3] and [4] are for x, y and z of indexposition
                //Little start here, I'll process this and the rest of the cases
                //more once we get the required objects inside Main
                if (crane1 != null) {
                    //do stuff, needs containers
                } else {
                    System.err.println("Error: No crane with this ID.");
                }
                break;
            case 3: //GIVE_CONTAINER
                Container cont2 = getContainerByID((String) params[0]);
                Crane crane2 = getCraneByID((String) params[1]);
                //TODO: make this work when we have AGVs in Main
                break;
            case 4: //PUT_CONTAINER
                Crane crane3 = getCraneByID((String) params[0]);
                Container cont3 = getContainerByID((String) params[1]);
                Vector3f cposition = new Vector3f((Float) params[2], (Float) params[3], (Float) params[4]);
                //TODO: stuff
                break;
            case 5: //GET_CONTAINER
                //String agvID3 = (String) params[0];
                Crane crane4 = getCraneByID((String) params[1]);
                //TODO: you know the drill by now
                break;
            case 6: //CREATE_TRANSPORTER
                String transporterID1 = (String) params[0];
                int transporterType = (Integer) params[1];
                Vector3f dockingPoint = getCraneByID((String) params[2]).position;
                ArrayList<SimContainer> simContainers = new ArrayList<SimContainer>();
                for (int i = 3; i < params.length - 3; i++) {
                    simContainers.add((SimContainer) params[i]);
                }
                Transporter t = new Transporter(transporterID1, simContainers, dockingPoint, transporterType);
                transporters.add(t);
                rootNode.attachChild(t);
                break;
            case 7: //REMOVE_TRANSPORTER
                String transporterID2 = (String) params[0];
                for(Transporter trans : transporters){
                    if(trans.id.equals(transporterID2)){
                        transporters.remove(trans);
                        rootNode.detachChild(trans);
                    }
                }
                break;
            default:
                System.err.println("Error: Invalid command for simulator.");
        }
    }
    
    /**
     * Finds and returns a crane by crane ID
     * @param id the id to search for
     * @return reference to a crane that matches the ID
     */
    private Crane getCraneByID(String id){
        for(int i = 0; i < seaCranes.length; i++){
            if(seaCranes[i].id.equals(id))
                return seaCranes[i];
        }
        for(int i = 0; i < bufCranes.length; i++){
            if(bufCranes[i].id.equals(id))
                return bufCranes[i];
        }
        for(int i = 0; i < lorCranes.length; i++){
            if(lorCranes[i].id.equals(id))
                return lorCranes[i];
        }
        for(int i = 0; i < trainCranes.length; i++){
            if(trainCranes[i].id.equals(id))
                return trainCranes[i];
        }
        for(int i = 0; i < barCranes.length; i++){
            if(barCranes[i].id.equals(id))
                return barCranes[i];
        }
        return null;
    }
    
    /**
     * Finds and returns a container by container ID
     * @param id the id to search for
     * @return reference to a container that matches the ID
     */
    private Container getContainerByID(String id){
        Container c = null;
        for(int i = 0; i < buffers.length; i++){
            for(int j = 0; j < buffers[i].bufferArray.length; j++){
               for(int k = 0; k < buffers[i].bufferArray[0].length; k++){
                   for(int l = 0; l < buffers[i].bufferArray[0][0].length; l++){
                       if(buffers[i].bufferArray[j][k][l] != null && buffers[i].bufferArray[j][k][l].id.equals(id)){
                           return c;
                       }
                    }
                } 
            }
        }
        //TODO: add code to look into containers on transport
        return c;
    }

    private void init_SeaCranes() {
        String cID = Path.getSeaID();
        for (int i = 1; i <= 10; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new SeaCrane(id, Path.getVector(id), scModel, scSModel, scHModel);
            seaCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    private void init_BufferCranes() {
        String cID = Path.getBufferAID();

        for (int i = 1; i <= 63; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BufferCrane(id, Path.getVector(id), bcModel, bcSModel, bcHModel);
            bufCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    private void init_LorryCranes() {
        String cID = Path.getLorryID();

        for (int i = 1; i <= 20; i++) {
            String id = cID + String.format("%03d", i);
            LorryCrane c = new LorryCrane(id, Path.getVector(id), lcModel, scHModel.clone().rotate(0, 90*FastMath.DEG_TO_RAD, 0));
            lorCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }
    private void init_BargeCranes()
    {
        String cID = Path.getBargeID();
         for (int i = 1; i <= 8; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BargeCrane(id, Path.getVector(id),scModel, scSModel, scHModel);
            barCranes[i - 1] = c;
          
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }
     private void init_TrainCranes()
    {
        String cID = Path.getTrainID();
         for (int i = 1; i <= 4; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new TrainCrane(id, Path.getVector(id),bcModel, scSModel,scHModel);
            trainCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     *
     * @param id the ID of the object.
     */
    public static void sendReady(String id) {
        Object[] objectArray = new Object[1];
        objectArray[0] = id;
        Message message = new Message(0, objectArray);
        sendMessage(message);
    }
 
}
