/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author deslanbe
 */
public class Kiviaaat extends JLayeredPane implements AxeListener{
 
    private TableModel model;
    private ArrayList<AxeComponent> listAxe;
    private static int DIST_CENTRE=20; 
            

    public Kiviaaat(){
        listAxe =new ArrayList<AxeComponent>();
        model=null;
    }
    
    public Kiviaaat(TableModel t){
        this.model=t;
         this.model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
              update(e);
            }
        });   
        createAxes();
    }
   
    public void setModel(TableModel t){
        this.model=t;
        this.model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
               update(e);
            }
        });   
        createAxes();
        this.repaint();
    }
    
    
    public void update(TableModelEvent e){
        switch(e.getType()){
            case UPDATE:
                updateAxe();           
             break;
            default:
                majTab();   
           break;     
        }
    }
    
    
    
    /**
     * réinitialise les axes et les recrée en fonction du model 
     * @param e 
     */
    public void majTab(){
        System.out.println("MAJ ");
        this.removeAxe();
        createAxes();
        this.revalidate();
        System.out.println(listAxe.isEmpty());
    }
    
    public void removeAxe(){
        for(AxeComponent a:listAxe){
            this.remove(a);
        }
    }
    
    /**
     * Demande une mise a jours de tout les axes
     * en fonction du model
     */
    public void updateAxe(){
        System.out.println("update");
        int i=0;
        for(AxeComponent a: listAxe){     
          Object[] line = RowToObject(i);    
            a.update(line);
            i++;
        }
        this.repaint();
    }
    
    
    
    /**
     * crée les différents axes en fonction du model de données
     * 
     */
    public void createAxes(){
        System.out.println("CreateAxes");
       int i;
       int orientation=0;       
       int angle=360/model.getRowCount();
       listAxe =new ArrayList<AxeComponent>();

       for(i=0;i<model.getRowCount();i++){
           Point centre = new Point();
           int l=this.calculeLongueur();
           centre.x=this.getSize().width/2;
           centre.y=this.getSize().height/2;
           Object[] line = RowToObject(i);    
           AxeComponent a=new AxeComponent(centre, l, DIST_CENTRE, orientation, line);
           this.add(a);
           listAxe.add(a);
           orientation+=angle; 
           a.addListener(this);
       }
    }
    
   

    
    
    /**
     * transforme une ligne du tableModel en object[]
     * @param indice de la ligne
     * @return tableau d'objet
     */
    public Object[] RowToObject(int indice){
           Object[] line=new Object[4];
           line[0]=model.getValueAt(indice,0);
           line[2]=model.getValueAt(indice,2);
           line[3]=model.getValueAt(indice,3);
                      if(model.getValueAt(indice,1) instanceof String){
                   line[1]=Integer.parseInt((String) model.getValueAt(indice,1));
           }else{
                 line[1]=model.getValueAt(indice,1);
           }
           
           
           
           
           return line;
    }
    
    /**
     * calcule la longueur d'un trait 
     * en fonction de la taille de la fenetre
     * @return la longueur
     */
    public int calculeLongueur(){
        int min=this.getSize().height;
        if(min>this.getSize().width){
            min=this.getSize().width;
        }
        return (min/2)-2*DIST_CENTRE;
    }
       
    @Override
    public void paint(Graphics g) {
        this.drawPolyagon(g);
        super.paint(g);           
    }
    
    
    /**
     * calcule et dessine le polygone
     * qui relie les curseur entre eux
     * @param g 
     */
    public void drawPolyagon(Graphics g){
        int i=0;
        int[] xPoints=new int[listAxe.size()];
        int[] yPoints=new int[listAxe.size()];

        for(AxeComponent a:listAxe){
             xPoints[i]= (int)a.getCentreCurseur().x;
             yPoints[i]= (int)a.getCentreCurseur().y;
             i++;
        }
        g.setColor(Color.black);
        g.drawPolygon(xPoints, yPoints, listAxe.size());          
        g.setColor(new Color((float) 0.5, 0, (float) 0.2, (float) 0.1));
        g.fillPolygon(xPoints, yPoints, listAxe.size());     
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        for (AxeComponent c : listAxe) {
            c.setBounds(0, 0, w, h);
        }
    }

    /**
     * change la valeur du model lorsque celui-ci est notifyer par l'axe
     * @param e 
     */
    @Override
    public void datachange(AxeEvent e) {
      AxeComponent axe= (AxeComponent) e.getSource();
      int i;
      for(i=0;i<model.getRowCount();i++){
          if(model.getValueAt(i,0).equals(axe.getTitre())){
                model.setValueAt(axe.getValue(), i,1);
                break;
          }
      }
    
    }

}
