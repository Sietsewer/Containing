/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import java.util.Date;
import javax.xml.bind.annotation.XmlSeeAlso;

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
    Vector3f size;                  //Changed to Vector3f, Dimension halds only two values
    int weightEmpty;                //Container weight when empty
    int weightLoaded;               //Container weight when loaded
    String contents;                //Contents of this container
    String contentType;             //Type of contents
    String contentDanger;           //Danger of contents
    String iso;                     //Container standard
    
    //Constructor
    /**
     *
     * @param id
     * @param dateArrival
     * @param dateDeparture
     * @param transportTypeArrival
     * @param transportTypeDeparture
     * @param cargoCompanyArrival
     * @param cargoCompanyDeparture
     * @param position
     * @param owner
     * @param containerNumber
     * @param size
     * @param weightEmpty
     * @param weightLoaded
     * @param contents
     * @param contentType
     * @param contentDanger
     * @param iso
     */
    public Container(String id, Date dateArrival, Date dateDeparture,
            int transportTypeArrival, int transportTypeDeparture,
            String cargoCompanyArrival, String cargoCompanyDeparture,
            Vector3f position, String owner, int containerNumber,
            Vector3f size, int weightEmpty, int weightLoaded,
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
