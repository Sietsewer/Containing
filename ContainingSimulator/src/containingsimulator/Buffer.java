/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Wessel
 */
public class Buffer extends Node{
    Vector3f bufferSize;                //size of this buffer
    Container[][][] bufferArray;     	//spatial array off SimContainers
    Node bufferNode;                    //node to attach all containers to
    ParkingSpot[] pSpots;               //array of parking spots
    
    /**
     * Constructor, initializes full buffer for stress testing
     * @param bufferSize size of this buffer
     */
    public Buffer(Vector3f bufferSize){
        this.bufferSize = bufferSize;
        
        Vector3f csize = new Vector3f(1.22f, 1.22f, 6.705f); //container size in m divided by 2 because box size grows both ways
        bufferArray = new Container[(int)bufferSize.x][(int)bufferSize.y][(int)bufferSize.z];
        
        bufferNode = new Node();
        
        for(int i = 0; i < bufferSize.x; i++){
            for(int j = 0; j < bufferSize.y; j++){
                for (int k = 0; k < bufferSize.z; k++){
                    bufferArray[i][j][k] = new Container("asdf",
                            new Vector3f(i * 2 * csize.x, j * 2 * csize.y, k * 2 * csize.z),
                            csize);
                    bufferArray[i][j][k].setIndexPosition(new Vector3f(i, j, k));
                    bufferNode.attachChild(bufferArray[i][j][k]);
                }
            }
        }
        
        setRendering();
        this.attachChild(bufferNode);
    }
    
    /**
     * Empty constructor, initalizes empty buffer
     */
    public Buffer(){
        this.bufferSize = new Vector3f(6, 6, 26);
        bufferArray = new Container[(int)bufferSize.x][(int)bufferSize.y][(int)bufferSize.z];
        bufferNode = new Node();
        this.attachChild(bufferNode);
    }

    /**
     * Attach parking spots to both ends of the buffer
     */
    public void addParkingSpots(Vector3f loc){
        pSpots = new ParkingSpot[12];
        for(int i = 0; i < pSpots.length; i++){
            if(i < 6){  //spots at north end of the buffer
                pSpots[i] = new ParkingSpot(new Vector3f(loc.x + 6.1f, 10, loc.z + 7.925f - (i * 2.44f)), (float)Math.PI * 0.5f);
            }else{      //spots at opposite side
                pSpots[i] = new ParkingSpot(new Vector3f(loc.x + 6.1f, 10, loc.z + 356.582f + (i * 2.44f)), (float)Math.PI * 0.5f);
            }
        }
    }
    
    /**
     * Set which geometries have to be drawn and which not
     */
    private void setRendering(){
        //cull walled-in containers
        for (int i = 0; i < bufferSize.x; i++){
            for (int j = 0; j < bufferSize.y; j++){
                for (int k = 0; k < bufferSize.z; k++){
                    if(bufferArray[i][j][k] != null){
                        bufferArray[i][j][k].updateRendering(bufferArray);
                    }
                }
            }
        }
    }
    
    /**
     * Fetch a container from the buffer
     * @param index buffer of container to fetch
     * @return 
     */
    public Container removeContainer(Vector3f index){
        Container c1 = bufferArray[(int)index.x][(int)index.y][(int)index.z];
        Container c2 = (Container)c1.clone();
        bufferNode.detachChild(c1);
        bufferArray[(int)index.x][(int)index.y][(int)index.z] = null;
        setRendering();
        return c2;
    }
    
    /**
     * Add a container to the buffer
     * @param index index where you want to add the container
     * @param sc1 the container you want to add to the buffer
     */
    public void addContainer(Vector3f index, Container c1){
        Container c2 = (Container)c1.clone();
        bufferArray[(int)index.x][(int)index.y][(int)index.z] = c2;
        c2.setIndexPosition(index);
        c2.setLocalTranslation(new Vector3f(index.x * 2 * c2.size.x,
                index.y * 2 * c2.size.y, index.z * 2 * c2.size.z)); //fit container into buffer
        bufferNode.attachChild(c2);
        setRendering();
    }
    
    public Container getContainerByID(String id){
        Container tempCont = null;
        for(int i = 0; i < bufferArray.length; i++){
            for(int j = 0; j < bufferArray[0].length; j++){
                for(int k = 0; k < bufferArray[0][0].length; k++){
                    if(bufferArray[i][j][k] != null && bufferArray[i][j][k].id.equalsIgnoreCase(id)){
                        tempCont = bufferArray[i][j][k];
                        return tempCont;
                    }
                }
            }
        }
        return tempCont;
    }
}
