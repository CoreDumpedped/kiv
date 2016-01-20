/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.Color;
import java.awt.Graphics;
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
    
    //Le rayon du curseur
    private Integer rayonCurseur = 3;
    
    public AxeComponent(){
        this.centre = new Point(50, 50);
        this.longueur = 100;
        this.distToCenter = 5;
        this.orientation = 5;
        this.titre = "titre";
        this.value = 5;
        this.vMin = 1;
        this.vMax = 10;
        
    }
    
    public AxeComponent(Point centre, Integer longueur, Integer distToCenter, Integer orientation, Object[] line){
        this.centre = centre;
        this.longueur = longueur;
        this.distToCenter = distToCenter;
        this.orientation = orientation;
        this.titre = (String) line[0];
        this.value = (Integer) line[1];
        this.vMin = (Integer) line[2];
        this.vMax = (Integer) line[3];
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(Color.black);       
                
        //La valeur de l'échelle en valeur/unité de longueur
        double echelle = (vMax-vMin)/longueur;
        
        //L'angle du trait en radians
        double angle = Math.toRadians(orientation);
        
        //Positions des extrémités du trait        
        int xDepart = (int) (centre.x + distToCenter*Math.cos(angle));
        int yDepart = (int) (centre.x + distToCenter*Math.sin(angle));
        int xFin = (int) (centre.x + (distToCenter+longueur)*Math.cos(angle));
        int yFin = (int) (centre.x + (distToCenter+longueur)*Math.cos(angle));
        
        //Position du curseur
        int xCurseur = (int) (centre.x - rayonCurseur + (distToCenter+(value-vMin)/echelle)*Math.cos(angle));
        int yCurseur = (int) (centre.x - rayonCurseur + (distToCenter+(value-vMin)/echelle)*Math.sin(angle));
        
        //On trace le trait
        g.drawLine(xDepart, yDepart, xFin, yFin);
        //On trace le curseur
        g.fillOval(xCurseur, yCurseur, 2*rayonCurseur, 2*rayonCurseur);
        
        
        
    }
}
