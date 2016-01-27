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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 *
 * @author deslanbe
 */
public class AxeComponent extends JComponent implements MouseListener, MouseMotionListener{
    
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
    
    //Le centre du curseur
    private Point2D.Double centreCurseur;

    //Coordonnées du centre du Kiviatt
    private Point centre;
    
    //Le rayon du curseur
    private Integer rayonCurseur = 5;
    
    public AxeComponent(){   
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
                                   
        //Positions des extrémités du trait        
        int xDepart = (int) (centre.x + distToCenter*Math.cos(getAngle()));
        int yDepart = (int) (centre.y + distToCenter*Math.sin(getAngle()));
        int xFin = (int) (centre.x + (distToCenter+longueur)*Math.cos(getAngle()));
        int yFin = (int) (centre.y + (distToCenter+longueur)*Math.sin(getAngle()));
        int xTexte = (int) (centre.x + (1.5*distToCenter+longueur)*Math.cos(getAngle())) - 5;
        int yTexte = (int) (centre.y + (1.5*distToCenter+longueur)*Math.sin(getAngle())) + 5;              
          
        //On trace le trait
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(xDepart, yDepart, xFin, yFin);
        
        //On trace le curseur
        g2.setColor(Color.red);      
        Point2D.Double centreC = getCentreCurseur();
        g2.fillOval((int)centreC.x - rayonCurseur, (int)centreC.y - rayonCurseur, 2*rayonCurseur, 2*rayonCurseur);
        g2.setColor(Color.black);       
        g2.drawOval((int)centreC.x - rayonCurseur, (int)centreC.y - rayonCurseur, 2*rayonCurseur, 2*rayonCurseur);
        
        g2.drawString(titre, xTexte , yTexte);
    }

    private double getEchelle(){
        return ((double)vMax-(double)vMin)/(double)longueur;
    }
    
    private double getAngle(){
        return Math.toRadians(orientation);
    }
    
    public Point2D.Double getCentreCurseur() {
        int xCentreCurseur = (int) (centre.x + (distToCenter+(value-vMin)/getEchelle())*Math.cos(getAngle()));
        int yCentreCurseur = (int) (centre.y + (distToCenter+(value-vMin)/getEchelle())*Math.sin(getAngle()));
        return new Point2D.Double(xCentreCurseur, yCentreCurseur);       
    }

    
    @Override
    public boolean contains(int x, int y) {        
        Point2D.Double souris = new Point2D.Double(x, y);              
        return getCentreCurseur().distance(souris) < rayonCurseur;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(titre + " : Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(titre + " : Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println(titre + " : Released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println(titre + " : Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println(titre + " : Exited");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println(titre + " : Dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(titre + " : Moved");
    }
    
}
