package containingsimulator;

import containing.xml.SimContainer;
import containing.xml.Message;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     Traincrane spatials
     */
    Spatial tcModel;
    Spatial tcSModel;
    Spatial tcHModel;
    /*
     crane 
     */
    Crane[] seaCranes = new Crane[10];
    Crane[] bufCranes = new Crane[63];
    Crane[] lorCranes = new Crane[20];
    Crane[] trainCranes = new Crane[4];
    Crane[] barCranes = new Crane[8];
    ArrayList<Transporter> transporters;
    ArrayList<AGV> agvs;
    Buffer[] buffers;
    private static Main app = null;
    public static float globalSpeed = 1f;
    private Collection<Message> messagesTodo;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
        app = new Main();
        
        app.setDisplayFps(true);
        app.setDisplayStatView(true);
        
        AppSettings settings = new AppSettings(true);
        settings.put("Title", "Project Containing - by Sjaal");
         //Anti-Aliasing
        settings.put("Samples", 0);
        app.setSettings(settings);  
        
        app.start();
    }
    

    /**
     * Simulator initialization
     */
    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
        loadAssets();
        init_Input();
        init_CrossHairs();
        flyCam.setMoveSpeed(400f);
        cam.setFrustumFar(5000f);
        listener = new ServerListener(this);
        messagesTodo = new ArrayList<Message>();
    }

    /**
     * Update function
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        sky_geo.setLocalTranslation(cam.getLocation());
        for (Crane c : seaCranes) {
            c.update(tpf);
        }
        for (Crane c : lorCranes) {
            c.update(tpf);
        }
        for (Crane c : bufCranes) {
            c.update(tpf);
        }
        for (Crane c : barCranes) {
            c.update(tpf);
        }
        for (Crane c : trainCranes) {
            c.update(tpf);
        }
        Collection<Message> temp = Collections.synchronizedCollection(new ArrayList(messagesTodo));
        for (Message m : temp) {
            this.messageRecieved(m);
            messagesTodo.remove(m);
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

    /**
     * Load all used assets and initialize simulator objects
     */
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

        //Init trainCrane
        tcModel = assetManager.loadModel("Models/traincrane/traincrane.j3o");
        tcSModel = assetManager.loadModel("Models/traincrane/traincrane_slider.j3o");
        tcHModel = assetManager.loadModel("Models/traincrane/traincrane_slider_hook.j3o");
        Material tcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        tcMat.setColor("Color", ColorRGBA.Yellow);
        tcModel.setMaterial(tcMat);
        tcSModel.setMaterial(tcMat);
        tcHModel.setMaterial(tcMat);

        //Init empty buffers
        buffers = new Buffer[63];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer();
            rootNode.attachChild(buffers[i]);
            //some magic number abuse here, snap buffers to proper location on map:
            buffers[i].setLocalTranslation(111.76f + (i * 22.035f), 11, 115);
            buffers[i].addParkingSpots(buffers[i].getLocalTranslation().clone());
        }

        init_SeaCranes();
        init_BargeCranes();
        init_BufferCranes();
        init_LorryCranes();
        init_TrainCranes();


        //Init Transporters
        Transporter.makeGeometry(assetManager);
        transporters = new ArrayList<Transporter>();



        //Init AGVs
        agvs = new ArrayList<AGV>();
        init_AGVs();

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

    /**
     * Send a message back to the controller
     * @param Message the message which to send
     */
    static void sendMessage(Message Message) {
        listener.sendMessage(Message);
    }

    /**
     * Adds incoming message to todo list
     * @param decodedMessage the message which to add
     */
    public void messageReceivedEvent(Message decodedMessage) {
        this.messagesTodo.add(decodedMessage);
    }

    /**
     * Process the incoming message
     * @param decodedMessage the decoded message which to process
     */
    private void messageRecieved(Message decodedMessage) {
        Object[] params = decodedMessage.getParameters();
        Container cont = null;
        Crane crane;

        String transporterID;
        AGV agv;
        switch (decodedMessage.getCommand()) {
            
            case Commands.SHUTDOWN:
                System.exit(1);
                break;
            case Commands.MOVE:
                agv = getAGVbyID((String) params[0]);
                Crane c = null;
                String[] pathIDs1 = new String[params.length - 1];
                for (int i = 1; i <= pathIDs1.length; i++) {
                    pathIDs1[i - 1] = (String) params[i];

                }
                c = getCraneByID(pathIDs1[pathIDs1.length - 1]);
                agv.addWaypoints(pathIDs1, c);
                break;
            case Commands.PICKUP_CONTAINER:
                crane = getCraneByID((String) params[0]);
                Transporter trans = getTransporterByID((String) params[1]);
                cont = getContainerByID((String) params[2]);
                if (crane != null && trans != null && cont != null) {
                    if (crane instanceof BufferCrane){
                        //pak container van buffer ipv transport
                    } else {
                        crane.pickupContainer(cont, trans);
                    }
                } else {
                    System.err.println("Error: No crane/container/transporter with this ID");
                }
                break;
            case Commands.GIVE_CONTAINER:
                crane = getCraneByID((String) params[0]);
                agv = getAGVbyID((String) params[1]);
                if(agv!=null)
                {
                crane.loadContainer(agv);
                }
                else
                {
                 System.err.println("Error: agv is null");
                }

                //TODO: put container on Transporter/Buffer
                break;
            case Commands.PUT_CONTAINER:
                crane = getCraneByID((String) params[0]);
                Vector3f indexPosition = new Vector3f((Float) params[1], (Float) params[2], (Float) params[3]);
                Vector3f realPosition = null;
                if(crane != null && crane instanceof BufferCrane)
                {
                    realPosition = ((BufferCrane)crane).getBuffer().getRealContainerPosition(indexPosition);
                    crane.putContainer(realPosition,indexPosition);
                }
                else
                if (crane != null) {
                    {
                      realPosition = crane.transporter.getRealContainerPosition(indexPosition);
                      crane.putContainer(realPosition,indexPosition);
                    }
                } 
                else {
                    System.err.println("Error: No crane/container with this ID");
                }
                break;
            case Commands.GET_CONTAINER:
                agv = getAGVbyID((String) params[0]);
                crane = getCraneByID((String) params[1]);
                if (crane != null ) {
                        crane.getContainer(agv);
                } else {
                    System.err.println("Error: No crane/container/transporter with this ID");
                }
                //TODO: take container from AGV
                break;
            case Commands.CREATE_TRANSPORTER:
                transporterID = (String) params[0];
                int transporterType = (Integer) params[1];
                Vector3f dockingPoint = getCraneByID((String) params[2]).getPos();
                ArrayList<SimContainer> simContainers = new ArrayList<SimContainer>();
                for (int i = 3; i < params.length; i++) {
                    simContainers.add((SimContainer) params[i]);
                }
                Transporter t = new Transporter(transporterID, simContainers, dockingPoint, transporterType);
                transporters.add(t);
                rootNode.attachChild(t);
                break;
            case Commands.REMOVE_TRANSPORTER:
                transporterID = (String) params[0];
                
                Transporter temp = null;
                for (Transporter transp : transporters) {
                    if (transp.id.equalsIgnoreCase(transporterID)) {
                        temp = transp;
                        break;
                    }
                }
                switch(temp.type)
                {
                    case TransportTypes.BARGE:
                        for(Crane cr : barCranes)
                        {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.LORRY:
                        for(Crane cr : lorCranes)
                        {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.SEASHIP:
                        for(Crane cr : seaCranes)
                        {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.TRAIN:
                        for(Crane cr : trainCranes)
                        {
                            cr.moveToHome();
                        }
                        break;
                }

                rootNode.detachChild(temp);
                transporters.remove(temp);
                break;
            case Commands.CHANGE_SPEED:
                float newspeed = (Integer) params[0];
                changeGlobalSpeed(newspeed);
                break;
            default:
                System.err.println("Error: Invalid command for simulator.");
        }
    }

    /**
     * Finds and returns a transporter by container ID
     *
     * @param id the id to search for
     * @return reference to a transporter that matches the ID
     */
    private Transporter getTransporterByID(String id) {
        for (Transporter trans : this.transporters) {
            if (trans.id.equalsIgnoreCase(id)) {
                return trans;
            }
        }
        return null;
    }

    /**
     * Finds and returns a crane by crane ID
     *
     * @param id the id to search for
     * @return reference to a crane that matches the ID
     */
    private Crane getCraneByID(String id) {

        String crane = id.substring(0, 3);
        if (crane.equalsIgnoreCase(Path.getSeaID())) {
            for (int i = 0; i < seaCranes.length; i++) {
                if (seaCranes[i].getID().equalsIgnoreCase(id)) {
                    return seaCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getBufferAID()) || crane.equalsIgnoreCase(Path.getBufferBID())) {
            for (int i = 0; i < bufCranes.length; i++) {
                if (bufCranes[i].getId().equalsIgnoreCase(id) || bufCranes[i].getId().equalsIgnoreCase(Path.getBufferAID() + id.substring(3))) {
                    return bufCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getLorryID())) {
            for (int i = 0; i < lorCranes.length; i++) {
                if (lorCranes[i].getID().equalsIgnoreCase(id)) {
                    return lorCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getTrainID())) {
            for (int i = 0; i < trainCranes.length; i++) {
                if (trainCranes[i].getID().equalsIgnoreCase(id)) {
                    return trainCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getBargeID())) {
            for (int i = 0; i < barCranes.length; i++) {
                if (barCranes[i].getID().equalsIgnoreCase(id)) {
                    return barCranes[i];
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns a container by container ID
     *
     * @param id the id to search for
     * @return reference to a container that matches the ID
     */
    private Container getContainerByID(String id) {
        Container c = null;
        for (int i = 0; i < buffers.length; i++) {
            c = buffers[i].getContainerByID(id);
            if (c != null) {
                return c;
            }
        }

        if (c == null) {
            for (Transporter t : transporters) {
                c = t.getContainerByID(id);
                if (c != null) {
                    return c;
                }
            }
        }
        return c;
    }

    /**
     * Finds and returns an AGV by container ID
     *
     * @param id the id to search for
     * @return reference to an AGV that matches the ID
     */
    private AGV getAGVbyID(String id) {

        for (AGV a : agvs) {
            if (a.id.equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Initialize SeaCranes
     */
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

    /**
     * Initialize BufferCranes
     */
    private void init_BufferCranes() {
        String cID = Path.getBufferAID();

        for (int i = 1; i <= 63; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BufferCrane(id, Path.getVector(id).add(0,0,200), bcModel, bcSModel, bcHModel, buffers[i - 1]);
            bufCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id).add(0,0,200));
        }
    }

    /**
     * Initialize LorryCranes
     */
    private void init_LorryCranes() {
        String cID = Path.getLorryID();

        for (int i = 1; i <= 20; i++) {
            String id = cID + String.format("%03d", i);
            Vector3f pos = Path.getVector(id).add(new Vector3f(0, 0, 40f));
            LorryCrane c = new LorryCrane(id, pos, lcModel, scSModel, scHModel.clone().scale(0.4f).rotate(0, 90 * FastMath.DEG_TO_RAD, 0));
            lorCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(pos);
        }
    }

    /**
     * Initialize BargeCranes
     */
    private void init_BargeCranes() {
        String cID = Path.getBargeID();
        for (int i = 1; i <= 8; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BargeCrane(id, Path.getVector(id), scModel, scSModel, scHModel);
            barCranes[i - 1] = c;

            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     * Initialize TrainCranes
     */
    private void init_TrainCranes() {
        String cID = Path.getTrainID();
        for (int i = 1; i <= 4; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new TrainCrane(id, Path.getVector(id), tcModel, tcSModel, tcHModel);
            trainCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     * Initialize AGVs
     */
    private void init_AGVs() {
        for (int i = 0; i < buffers.length * 4; i++) {
            String id = "AGV" + String.format("%03d", i + 1);
            AGV agv = new AGV(id, agvModel.clone());
            agvs.add(agv);
            rootNode.attachChild(agv);
        }

        int j = 0;
        for (int i = 0; i < buffers.length * 4; i += 4) {
            if (j < 5){
                agvs.get(i).jumpToPark(buffers[j].pSpots[3]);
                agvs.get(i + 1).jumpToPark(buffers[j].pSpots[2]);
                agvs.get(i + 2).jumpToPark(buffers[j].pSpots[1]);
                agvs.get(i + 3).jumpToPark(buffers[j].pSpots[0]);
                agvs.get(i).up = true;
                agvs.get(i + 1).up = true;
                agvs.get(i + 2).up = true;
                agvs.get(i + 3).up = true;
            } else {
                agvs.get(i).jumpToPark(buffers[j].pSpots[1]);
                agvs.get(i + 1).jumpToPark(buffers[j].pSpots[0]);
                agvs.get(i + 2).jumpToPark(buffers[j].pSpots[6]);
                agvs.get(i + 3).jumpToPark(buffers[j].pSpots[7]);
                agvs.get(i).up = true;
                agvs.get(i + 1).up = true;
                agvs.get(i + 2).up = false;
                agvs.get(i + 3).up = false;
            }
            j++;
        }
    }

    /**
     * Send a READY message back to the controller
     * @param id the ID of the object.
     */
    public static void sendReady(String id) {
        try {
            Object[] objectArray = new Object[1];
            objectArray[0] = id;
            Message message = new Message(0, objectArray);
            sendMessage(message);
        } catch (Exception ex) {
            System.out.println("Connection problems");
        }
    }

    //FOR TESTING///////FOR TESTING///////FOR TESTING////CLICK TO TEST SOMETHING! 
    public void init_Input() {
        inputManager.addMapping("left-click",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("right-click",
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
          inputManager.addMapping("esc-button",
                new KeyTrigger(KeyInput.KEY_ESCAPE));
          
        inputManager.addListener(actionListener, "left-click");
        inputManager.addListener(actionListener, "right-click");
        inputManager.addListener(actionListener, "esc-button");
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("esc-button"))
            {
                System.exit(1);
            }
            if (name.equals("right-click")) {
                changeGlobalSpeed(globalSpeed*1.5f); //fasten up
            }
            if (name.equals("left-click") && !keyPressed) {
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection().normalize());
                rootNode.collideWith(ray, results);
                //check if point of click was on the iceberg (only penguins on iceberg allowed!)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    Vector3f hitPoint = closest.getContactPoint(); //where uve shot 
                   barCranes[0].moveToHome();
                }

            }
        }
    };
    private void changeGlobalSpeed(float acceleration)
    {
        
        globalSpeed = acceleration;
        
        for(AGV agv: agvs)
        {
            agv.globalSpeedChanged();
        }
        
    }

    protected void init_CrossHairs() {
        setDisplayStatView(false);
        BitmapText ch = new BitmapText(guiFont, false);

        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChildAt(ch, 0);
    }
}
