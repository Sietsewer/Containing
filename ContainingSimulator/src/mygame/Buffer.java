/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Wessel
 */
public class Buffer extends Node{
    Vector3f bufferSize;                //size of this buffer
    Container[][][] bufferArray;     //spatial array off SimContainers
    Node bufferNode;                    //node to attach all containers to
    
    /**
     * Constructor, initializes full buffer for stress testing
     * @param bufferSize size of this buffer
     */
    public Buffer(Vector3f bufferSize){
        this.bufferSize = bufferSize;
        
        Vector3f csize = new Vector3f(2.44f, 2.44f, 13.41f);
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
        Container sc1 = bufferArray[(int)index.x][(int)index.y][(int)index.z];
        Container sc2 = (Container)sc1.clone();
        bufferNode.detachChild(sc1);
        bufferArray[(int)index.x][(int)index.y][(int)index.z] = null;
        setRendering();
        return sc2;
    }
    
    /**
     * Add a container to the buffer
     * @param index index where you want to add the container
     * @param sc1 the container you want to add to the buffer
     */
    public void addContainer(Vector3f index, Container sc1){
        Container sc2 = (Container)sc1.clone();
        bufferArray[(int)index.x][(int)index.y][(int)index.z] = sc2;
        sc2.setIndexPosition(index);
        sc2.setLocalTranslation(new Vector3f(index.x * 2 * sc2.size.x, index.y * 2 * sc2.size.y, index.z * 2 * sc2.size.z));
        bufferNode.attachChild(sc2);
        setRendering();
    }
}
