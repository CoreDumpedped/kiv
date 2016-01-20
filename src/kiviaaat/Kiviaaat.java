/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

import java.awt.Graphics;
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
            
    public Kiviaaat(){ 
        this(new DefaultTableModel(),new ArrayList<>());
    }
    
    
    public Kiviaaat(TableModel t,ArrayList<AxeComponent> l){
        this.model=t;
        this.listAxe=l;
    }
   
    
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
    }

}
