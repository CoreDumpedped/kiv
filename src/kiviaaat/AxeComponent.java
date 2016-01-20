/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.Point;
import javax.swing.JComponent;

/**
 *
 * @author deslanbe
 */
public class AxeComponent extends JComponent{
    
    //Longueur du trait
    private Integer longueur;
    //Distance entre le centre du Kiviatt et le début du trait
    private Integer distToCenter;
    //Orientation du trait en degrés
    private Integer orientation;
    
    //Valeurs contenues dans le modèle
    private String titre;
    private Integer vMax;
    private Integer vMin;
    private Integer value;
    
    //Coordonnées du centre du Kiviatt
    private Point centre;
    
    public AxeComponent(Point centre, Integer longueur, Integer distToCenter, Integer orientation, Object[] line){
        this.centre = centre;
        this.longueur = longueur;
        this.distToCenter = distToCenter;
        this.orientation = orientation;
        this.titre = (String) line[0];
        this.value = (Integer) line[1];
        this.vMin = (Integer) line[2];
        this.vMax = (Integer) line[3];
    }
}
