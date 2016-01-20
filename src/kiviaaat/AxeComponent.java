/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

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
    
    //Valeurs contenues dans le modèle
    private String titre;
    private Integer vMax;
    private Integer vMin;
    private Integer value;
    
    //Coordonnées du centre du Kiviatt
    private Integer x0;
    private Integer y0;
    
    public AxeComponent(Integer x0, Integer y0, Integer longueur, Integer distToCenter, Object[] line){
        this.x0 = x0;
        this.y0 = y0;
        this.longueur = longueur;
        this.distToCenter = distToCenter;
        this.titre = (String) line[0];
        this.value = (Integer) line[1];
        this.vMin = (Integer) line[2];
        this.vMax = (Integer) line[3];
    }
}
