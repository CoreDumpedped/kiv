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


public class AxeComponent extends JComponent implements MouseListener, MouseMotionListener{
    
    //Longueur du trait
    private Integer longueur;
    //Distance entre le centre du Kiviatt et le début du trait
    private Integer distToCenter;
    //Orientation du trait en degrés
    private Integer orientation;
    
    //Les valeurs de la ligne
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
    
    //Variable utile pour le Drag'n'Drop
    private boolean dragged = true;
    
    ///////////////////
    // Constructeurs //
    ///////////////////
    
    public AxeComponent(){
        longueur = 200;
        distToCenter = 20;
        orientation = 0;
        titre = "default";
        vMax = 100;
        vMin = 0;
        value = 50;
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
    
    /////////////////////////////////
    // Interactions avec le modèle //
    /////////////////////////////////
    
    /**
     * Mets à jour les valeur de l'axe
     * @param line, les nouvelles données 
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

    /**
     * Ajoute un listener aux listeners de cet axe
     * @param s, le listener à ajouter
     */
    public void addListener(AxeListener s){
         listeListener.add(s);
    }
    
    /**
     * Notifie les observeurs qu'une donnée a changé
     */
    public void firePropertyNotify(){
        AxeEvent e= new AxeEvent(this);
            for(AxeListener s: listeListener){
                s.datachange(e);
            } 
        }
    
    /////////////////
    // Utilitaires //
    /////////////////
    
    /**
     * @return l'échelle de cette axe en unité de valeur par pixel
     */
    private double getEchelle(){
        return ((double)vMax-(double)vMin)/(double)longueur;
    }

    /**
     * @return la valeur de l'axe
     */
    public Integer getValue() {
        return Math.round((float)value);
    }
    
    /**
     * @return l'angle de cet axe en radians
     */
    private double getAngle(){
        return Math.toRadians(orientation);
    } 
    
    /**
     * @return le titre de cet axe
     */
    public String getTitre() {
        return titre;
    }
    
    /**
     * @return le point correspondant au centre du curseur de cet axe
     */
    public Point2D.Double getCentreCurseur() {
        double xCentreCurseur = (centre.x + (distToCenter+(value-vMin)/getEchelle())*Math.cos(getAngle()));
        double yCentreCurseur = (centre.y + (distToCenter+(value-vMin)/getEchelle())*Math.sin(getAngle()));
        return new Point2D.Double(xCentreCurseur, yCentreCurseur);       
    }  
    
    /**
     * @param x la coordonées des abcisses de la souris
     * @param y la coordonées des ordonnées de la souris
     * @return la valeur correspondant à la projection orthogonale de la souris sur l'axe
     */
    private double getValueProjection(int x, int y){
        double dist = centre.distance(x, y);
        double cosa = (float) (x - centre.x) / dist;
        double sina = (float) (y - centre.y) / dist;

        double alpha = (double) Math.acos(cosa);
        if (sina <= 0) {
            alpha *= -1;
        }
        double l = dist * Math.cos(alpha - getAngle());
        double newValue = (l-distToCenter)*getEchelle();
        
        //Si on est sorti des bornes on prend une valeur extrême
        newValue = (newValue < vMin) ? vMin : newValue;
        newValue = (newValue > vMax) ? vMax : newValue;
        
        return newValue;
    }
   
    //////////////////////////
    // Méthodes surchargées //
    //////////////////////////
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);       
        Graphics2D g2 = (Graphics2D) g;
                                   
        //Coordonnées des extrémités du trait        
        int xDepart = (int) (centre.x + distToCenter*Math.cos(getAngle()));
        int yDepart = (int) (centre.y + distToCenter*Math.sin(getAngle()));
        int xFin = (int) (centre.x + (distToCenter+longueur)*Math.cos(getAngle()));
        int yFin = (int) (centre.y + (distToCenter+longueur)*Math.sin(getAngle()));
        
        //Coordonnées du titre
        int xTexte = (int) (centre.x + (1.5*distToCenter+longueur)*Math.cos(getAngle())) - 5;
        int yTexte = (int) (centre.y + (1.5*distToCenter+longueur)*Math.sin(getAngle())) + 5;              
          
        //On trace le trait
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(xDepart, yDepart, xFin, yFin);
        
        //On trace le curseur
        g2.setColor(Color.red);      
        Point2D.Double centreC = getCentreCurseur();
        g2.fillOval((int) Math.round(centreC.x - rayonCurseur), (int) Math.round(centreC.y - rayonCurseur), 2*rayonCurseur, 2*rayonCurseur);
        g2.setColor(Color.black);       
        g2.drawOval((int) Math.round(centreC.x - rayonCurseur), (int) Math.round(centreC.y - rayonCurseur), 2*rayonCurseur, 2*rayonCurseur);
        
        //On trace le titre
        g2.drawString(titre, xTexte , yTexte);
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
        //firePropertyNotify();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println(titre + " : Moved");
    }   
}
    
    

