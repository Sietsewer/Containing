/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.awt.Dimension;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Wessel
 */
@XmlRootElement(name = "container")
@XmlSeeAlso({Vector3f.class, Date.class})
public class Container {
    //Properties

    private String id;                      //Container id
    private Date dateArrival;               //Date of arrival
    private Date dateDeparture;             //Date of scheduled departure
    private int transportTypeArrival;       //Type of container arrival
    private int transportTypeDeparture;     //Type of container departure
    private String cargoCompanyArrival;     //Company handling container arrival
    private String cargoCompanyDeparture;   //Company handling container departure
    private Vector3f position;              //Position on arrival transport
    private Vector3f bufferPosition;        //Position of container inside buffer
    private String owner;                   //Owner of this container
    private int containerNumber;            //Container number
    private String height;                 //Only length and width?
    private String width;
    private String lenght;
    private int weightEmpty;                //Container weight when empty
    private int weightLoaded;               //Container weight when loaded
    private String contents;                //Contents of this container
    private String contentType;             //Type of contents
    private String contentDanger;           //Danger of contents
    private String iso;                     //Container standard

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
            String height,
            String width,
            String lenght, int weightEmpty, int weightLoaded,
            String contents, String contentType, String contentDanger,
            String iso ) {
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

        this.weightEmpty = weightEmpty;
        this.weightLoaded = weightLoaded;
        this.contents = contents;
        this.contentType = contentType;
        this.contentDanger = contentDanger;
        this.iso = iso;
        this.height = height;                 //Only length and width?
        this.width = width;
        this.lenght = lenght;
    }
    public Container()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Date getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(Date dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public int getTransportTypeArrival() {
        return transportTypeArrival;
    }

    public void setTransportTypeArrival(int transportTypeArrival) {
        this.transportTypeArrival = transportTypeArrival;
    }

    public int getTransportTypeDeparture() {
        return transportTypeDeparture;
    }

    public void setTransportTypeDeparture(int transportTypeDeparture) {
        this.transportTypeDeparture = transportTypeDeparture;
    }

    public String getCargoCompanyArrival() {
        return cargoCompanyArrival;
    }

    public void setCargoCompanyArrival(String cargoCompanyArrival) {
        this.cargoCompanyArrival = cargoCompanyArrival;
    }

    public String getCargoCompanyDeparture() {
        return cargoCompanyDeparture;
    }

    public void setCargoCompanyDeparture(String cargoCompanyDeparture) {
        this.cargoCompanyDeparture = cargoCompanyDeparture;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getBufferPosition() {
        return bufferPosition;
    }

    public void setBufferPosition(Vector3f bufferPosition) {
        this.bufferPosition = bufferPosition;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(int containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public int getWeightEmpty() {
        return weightEmpty;
    }

    public void setWeightEmpty(int weightEmpty) {
        this.weightEmpty = weightEmpty;
    }

    public int getWeightLoaded() {
        return weightLoaded;
    }

    public void setWeightLoaded(int weightLoaded) {
        this.weightLoaded = weightLoaded;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentDanger() {
        return contentDanger;
    }

    public void setContentDanger(String contentDanger) {
        this.contentDanger = contentDanger;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}
