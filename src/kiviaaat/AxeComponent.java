/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    
    private   int xCurseur=0;
    private    int yCurseur=0;

    public int getxCurseur() {
        return xCurseur + rayonCurseur;
    }

    public void setxCurseur(int xCurseur) {
        this.xCurseur = xCurseur;
    }

    public int getyCurseur() {
        return yCurseur + rayonCurseur;
    }

    public void setyCurseur(int yCurseur) {
        this.yCurseur = yCurseur;
    }
    
    //Coordonnées du centre du Kiviatt
    private Point centre;
    
    //Le rayon du curseur
    private Integer rayonCurseur = 5;
    
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
        this.value = (Integer)line[1];
        this.vMin = (Integer) line[2];
        this.vMax = (Integer) line[3];
        repaint();
    }
    
    

    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D) g;
               
                
        //La valeur de l'échelle en valeur/unité de longueur
        double echelle = ((double)vMax-(double)vMin)/(double)longueur;
   //     System.out.println("vMax : " + vMax + ",vMin : " + vMin + ",l : " + longueur);
   //     System.out.println("centre X : " + centre.x + "centre Y : " + centre.y);
        //L'angle du trait en radians
        double angle = Math.toRadians(orientation);
        
        //Positions des extrémités du trait        
        int xDepart = (int) (centre.x + distToCenter*Math.cos(angle));
        int yDepart = (int) (centre.y + distToCenter*Math.sin(angle));
        int xFin = (int) (centre.x + (distToCenter+longueur)*Math.cos(angle));
        int yFin = (int) (centre.y + (distToCenter+longueur)*Math.sin(angle));
        int xTexte = (int) (centre.x + (1.5*distToCenter+longueur)*Math.cos(angle)) - 5;
        int yTexte = (int) (centre.y + (1.5*distToCenter+longueur)*Math.sin(angle)) + 5;
        
        //Position du curseur
         xCurseur = (int) (centre.x + (distToCenter+(value-vMin)/echelle)*Math.cos(angle) - rayonCurseur);
         yCurseur = (int) (centre.y + (distToCenter+(value-vMin)/echelle)*Math.sin(angle) - rayonCurseur);
        
   //     System.out.println("Trait orientation : " + orientation + ", echelle : " + echelle + ", xDepart=" + xDepart + ", yDepart=" + yDepart + ", xCurseur=" + xCurseur + ", yCurseur=" + yCurseur);
          
        //On trace le trait
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(xDepart, yDepart, xFin, yFin);
        
        //On trace le curseur
        g2.setColor(Color.red);       
        g2.fillOval(xCurseur, yCurseur, 2*rayonCurseur, 2*rayonCurseur);
        g2.setColor(Color.black);       
        g2.drawOval(xCurseur, yCurseur, 2*rayonCurseur, 2*rayonCurseur);
        
        g2.drawString(titre, xTexte , yTexte);
    }
}
