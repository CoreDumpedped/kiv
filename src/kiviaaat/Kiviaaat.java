/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author deslanbe
 */
public class Kiviaaat extends JLayeredPane{
 
    private TableModel model;
    private ArrayList<AxeComponent> listAxe;
    private static int DIST_CENTRE=5; 
            

    public Kiviaaat(){
        listAxe =new ArrayList<AxeComponent>();
        model=null;
    }
    
    public Kiviaaat(TableModel t){
        this.model=t;
        this.listAxe=createAxes();
    }
   
    public void setModel(TableModel t){
        this.model=t;
        this.listAxe=createAxes();
        this.repaint();
    }
    /**
     * crée les différents axes en fonction du model de données
     * return une liste axes en fonction du model
     */
    public ArrayList<AxeComponent> createAxes(){
       int i;
       int orientation=0;       
       int angle=360/model.getRowCount();
      ArrayList<AxeComponent> liste =new ArrayList<AxeComponent>();
       for(i=0;i<model.getRowCount();i++){
           Point p=new Point();
           int l=this.calculeLongueur();
           p.x=this.getSize().height/2;
           p.y=this.getSize().width/2;
           Object[] line=RowToObject(i);    
           AxeComponent a=new AxeComponent(p,l,DIST_CENTRE, orientation, line);
           this.add(a);
           liste.add(a);
           orientation+=angle;        
       }
       return liste;
    }
    

   
    /**
     * transforme une ligne du tableModel en object[]
     * @param indice de la ligne
     * @return tableau d'objet
     */
    public Object[] RowToObject(int indice){
           Object[] line=new Object[4];
           line[0]=model.getValueAt(indice,0);
           line[1]=model.getValueAt(indice,1);
           line[2]=model.getValueAt(indice,2);
           line[3]=model.getValueAt(indice,3);
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
        return (min/2)-DIST_CENTRE;
    }
    

    
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        
        for (AxeComponent c : listAxe) {
            c.setBounds(0, 0, w, h);
        }
    }

}
