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
import java.util.ArrayList;
import java.util.List;
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

    private String titre;
    private Integer vMax;
    private Integer vMin;
    private double value;
    
    private List<AxeListener> listeListener=new ArrayList<>();
    
    //Le centre du curseur
    private Point2D.Double centreCurseur;

    //Coordonnées du centre du Kiviatt
    private Point centre;
    
    //Le rayon du curseur
    private Integer rayonCurseur = 5;
    
    private boolean dragged = true;
    
    public AxeComponent(){
        longueur = 200;
        distToCenter = 20;
        orientation = 45;
        titre = "test";
        vMax = 100;
        vMin = 0;
        value = 75;
        centre = new Point(250, 250);
        repaint();
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
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        repaint();
    }
    
    /**
     * update les valeur de l'axe uniquement
     * @param line 
     */
    public void update(Object[] line){
        if(!dragged){
             this.titre = (String) line[0];
            this.value = (Integer)line[1];
            this.vMin = (Integer) line[2];
            this.vMax = (Integer) line[3];
            repaint();      
        }
    
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
    
        //Valeurs contenues dans le modèle
    public Integer getValue() {
        return Math.round((float)value);
    }
    private double getAngle(){
        return Math.toRadians(orientation);
    }
         
    public String getTitre() {
        return titre;
    }
    
    public Point2D.Double getCentreCurseur() {
        int xCentreCurseur = (int) (centre.x + (distToCenter+(value-vMin)/getEchelle())*Math.cos(getAngle()));
        int yCentreCurseur = (int) (centre.y + (distToCenter+(value-vMin)/getEchelle())*Math.sin(getAngle()));
        return new Point2D.Double(xCentreCurseur, yCentreCurseur);       
    }

    public double getValueProjection(int x, int y){

        double dist = centre.distance(x, y);
        double cosa = (float) (x - centre.x) / dist;
        double sina = (float) (y - centre.y) / dist;

        double alpha = (double) Math.acos(cosa);
        if (sina <= 0) {
            alpha *= -1;
        }
        double l = dist * Math.cos(alpha - getAngle());

        double newValue = (l-distToCenter)*getEchelle();
        newValue = (newValue < vMin) ? vMin : newValue;
        newValue = (newValue > vMax) ? vMax : newValue;
        return newValue;
    }
    
    @Override
    public boolean contains(int x, int y) {        
        Point2D.Double souris = new Point2D.Double(x, y);    
        return getCentreCurseur().distance(souris) < rayonCurseur;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
     //   System.out.println(titre + " : Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
     //   System.out.println(titre + " : Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(dragged){
           value = getValueProjection(e.getX(), e.getY());
            firePropertyNotify();
           repaint();
           dragged=false;
        }      
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      //  System.out.println(titre + " : Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
     //   System.out.println(titre + " : Exited");
    }

    @Override
    public void mouseDragged(MouseEvent e) {      
        value = getValueProjection(e.getX(), e.getY());
        dragged = true;
        firePropertyNotify();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(titre + " : Moved");
    }
    
    
    public void addListener(AxeListener s){
         listeListener.add(s);
    }
       /**
        * notify des changement les observeur
        */
    public void firePropertyNotify(){
        System.out.println("notification de changement");
       AxeEvent e= new AxeEvent(this);
            for(AxeListener s: listeListener){
                s.datachange(e);
            } 
        }

    }
    
    

