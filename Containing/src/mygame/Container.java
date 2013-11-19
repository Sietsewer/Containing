/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import java.awt.Dimension;
import java.util.Date;

/**
 *
 * @author Wessel
 */
public class Container {
    //Properties
    String id;                      //Container id
    Date dateArrival;               //Date of arrival
    Date dateDeparture;             //Date of scheduled departure
    int transportTypeArrival;       //Type of container arrival
    int transportTypeDeparture;     //Type of container departure
    String cargoCompanyArrival;     //Company handling container arrival
    String cargoCompanyDeparture;   //Company handling container departure
    Vector3f position;              //Position on arrival transport
    Vector3f bufferPosition;        //Position of container inside buffer
    String owner;                   //Owner of this container
    int containerNumber;            //Container number
    Dimension size;                 //Only length and width?
    int weightEmpty;                //Container weight when empty
    int weightLoaded;               //Container weight when loaded
    String contents;                //Contents of this container
    String contentType;             //Type of contents
    String contentDanger;           //Danger of contents
    String iso;                     //Container standard
    
    //Constructor
    public Container(String id, Date dateArrival, Date dateDeparture,
            int transportTypeArrival, int transportTypeDeparture,
            String cargoCompanyArrival, String cargoCompanyDeparture,
            Vector3f position, String owner, int containerNumber,
            Dimension size, int weightEmpty, int weightLoaded,
            String contents, String contentType, String contentDanger,
            String iso){
        this.id = id;
        this.dateArrival = dateArrival;
        this.dateDeparture = dateDeparture;
        this.transportTypeArrival = transportTypeArrival;
        this.transportTypeDeparture = transportTypeDeparture;
        this.cargoCompanyArrival = cargoCompanyArrival;
        this.cargoCompanyDeparture = cargoCompanyDeparture;
        this.position = position;
        //NOTE: bufferPosition remains null until container is assigned to a buffer!
        this.owner = owner;
        this.containerNumber = containerNumber;
        this.size = size;
        this.weightEmpty = weightEmpty;
        this.weightLoaded = weightLoaded;
        this.contents = contents;
        this.contentType = contentType;
        this.contentDanger = contentDanger;
        this.iso = iso;
    }
}
