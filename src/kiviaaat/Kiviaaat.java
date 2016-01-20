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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author deslanbe
 */
public class Kiviaaat extends JComponent{
 
    private TableModel model;
    private ArrayList<AxeComponent> listAxe;
    private static int DIST_CENTRE=5; 
            
    public Kiviaaat(){ 
        this(new DefaultTableModel(),new ArrayList<>());
    }
    
    
    public Kiviaaat(TableModel t,ArrayList<AxeComponent> l){
        this.model=t;
        this.listAxe=l;
    }
   
    public Kiviaaat(TableModel t){
        this(t,new ArrayList<AxeComponent>());
    }
    
    /**
     * crée les différents axes en fonction du model de données
     */
    public void createAxes(){
       int i;
       int orientation=0;       
       int angle=360/model.getRowCount();
      
       for(i=0;i<model.getRowCount();i++){
           Point p=new Point();
           int l=this.calculeLongueur();
           p.x=this.getSize().height/2;
           p.y=this.getSize().width/2;
           Object[] line=RowToObject(i);    
           AxeComponent a=new AxeComponent(p,l,DIST_CENTRE, orientation, line);
           listAxe.add(a);
           orientation+=angle;        
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

}
