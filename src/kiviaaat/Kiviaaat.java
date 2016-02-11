package kiviaaat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class Kiviaaat extends JLayeredPane implements AxeListener{
 
    private TableModel model;
    private ArrayList<AxeComponent> listAxe;
    private static int DIST_CENTRE=20; 
    
    ///////////////////
    // Constructeurs //
    ///////////////////
    
    public Kiviaaat(){
        listAxe =new ArrayList<AxeComponent>();
        model=null;
    }   
    public Kiviaaat(TableModel t){
        this.setModel(t);
    }
    
    ///////////////////////
    // Gestion du modèle //
    ///////////////////////
    
    /**
     * Initialise le Kiviatt avec un nouveau modèle
     * @param t, le nouveau modèle 
     */
    public void setModel(TableModel t) {   
        try {
            testTableModel(t);
        } catch (TableModelException ex) {
            Logger.getLogger(Kiviaaat.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
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
    
    /**
     * Teste si le tableModel est conforme, renvoie des exceptions sinon.
     * @param e 
     */   
    private void testTableModel(TableModel t) throws TableModelException {
        int i;
        System.out.println("test table");
        String valeurAxe[];
        if (t.getColumnCount() != 4) {
            throw new TableModelException("Nombre de colonnes incorrect. Il doit y en avoir 4 : Nom, valeur, valeurMin et valeurMax.");
        }
        try {
            for (i = 0; i < t.getRowCount(); i++) {
                Object[] line = RowToObject(i, t);
                if((int)line[2]>(int)line[3]){
                     throw new TableModelException("[LIGNE " + (i+1) + "]" + "Les valeurs de la 3e colonne doivent être inferieures a celles de la 4e");
                }
                if((int)line[1]<(int)line[2] || (int)line[1]>(int)line[3]){
                     throw new TableModelException("[LIGNE " + (i+1) + "]" + "Les valeurs de la 2e colonne doivent être situées entre celles de la 3e et 4e colonne");
                }
                
            }
          if(verifNomVariable(t)){
              throw new TableModelException("[ERREUR] Plusieur variable on le même nom"); 
          }
        } catch (NumberFormatException e) {
            throw new TableModelException("Valeurs de la 2e colonne incorrectes");
        }
        
    }
    
    private boolean verifNomVariable(TableModel t){
        System.out.println("verif nom");
        int i,j;
        for (i = 0; i < t.getRowCount(); i++) {
            for (j = i+1; j < t.getRowCount(); j++) {
                 Object[] line1 = RowToObject(i, t);
                 Object[] line2 = RowToObject(j, t);
                 if(line1[0].equals(line2[0])){
                     return true;
                 }
            }
        }
        return false;
    }

    /**
     * Handler pour l'évènement TableModelEvent
     * @param e 
     */
    private void update(TableModelEvent e){
        switch(e.getType()){
            case UPDATE:
                updateAxe();           
                break;
            default:
        {
            try {
                testTableModel(model);
                majTab();  
            } catch (TableModelException ex) {
                Logger.getLogger(Kiviaaat.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
                
                break;          
        }
    }   
    
    /////////////////
    // Utilitaires //
    /////////////////
    
    /**
     * Transforme une ligne du tableModel en object[]
     * @param indice de la ligne
     * @return tableau d'objet
     */
    private Object[] RowToObject(int indice){
           Object[] line=new Object[4];
           line[0]=model.getValueAt(indice,0);
           line[2]=model.getValueAt(indice,2);
           line[3]=model.getValueAt(indice,3);
           if(model.getValueAt(indice,1) instanceof String){
                line[1]=Integer.parseInt((String) model.getValueAt(indice,1));
           }
           else{
                line[1]=model.getValueAt(indice,1);
           }
           return line;
    }
    
     /**
     * Transforme une ligne du tableModel en object[]
     * @param indice de la ligne
     * @param t, la table à exploiter
     * @return un tableau d'objet
     */
    private Object[] RowToObject(int indice,TableModel t){
           Object[] line=new Object[4];
           line[0]=t.getValueAt(indice,0);
           line[2]=t.getValueAt(indice,2);
           line[3]=t.getValueAt(indice,3);
          if(t.getValueAt(indice,1) instanceof String){
                   line[1]=Integer.parseInt((String) t.getValueAt(indice,1));
           }else{
                 line[1]=t.getValueAt(indice,1);
           }
           return line;
    }
     
    /**
     * Calcule la longueur des axes en fonction de la taille de la fenetre
     * @return la longueur
     */
    private int calculeLongueur(){
        int min=this.getSize().height;
        if(min>this.getSize().width){
            min=this.getSize().width;
        }
        return (min/2)-2*DIST_CENTRE;
    }
     
    ////////////////////////////////
    // Gestion des composants Axe //
    ////////////////////////////////
    
    /**
     * Supprime tous les axes du layout
     */
    private void removeAxe(){
        for(AxeComponent a:listAxe){
            this.remove(a);
        }
    }
    
    /**
     * Mets à jour tous les axes
     */
    private void updateAxe(){
        int i=0;
        for(AxeComponent a: listAxe){     
            Object[] line = RowToObject(i);    
            a.update(line);
            i++;
        }
        this.repaint();
    }
    
    /**
     * Crée les différents axes en fonction du model de données
     */
    private void createAxes(){
        int orientation=0;       
        int angle=360/model.getRowCount();
        listAxe =new ArrayList<AxeComponent>();

        Point centre = new Point();
        int l=this.calculeLongueur();
        centre.x=this.getSize().width/2;
        centre.y=this.getSize().height/2;
       
       for(int i=0;i<model.getRowCount();i++){          
           Object[] line = RowToObject(i);    
           AxeComponent a=new AxeComponent(centre, l, DIST_CENTRE, orientation, line);
           this.add(a);
           listAxe.add(a);
           orientation+=angle; 
           a.addListener(this);
       }
    }
    
    /**
     * Réinitialise les axes et les recrée en fonction du model 
     */
    private void majTab(){
        this.removeAxe();
        createAxes();
        this.revalidate();
    }
    
    ////////////////////////
    // Méthodes de dessin //   
    ////////////////////////
    
    /**
     * Dessine le fond en polygones
     * @param g 
     */
    private void drawDecoPolygone(Graphics g){       
        int orientation=0;       
        double angle=360/model.getRowCount();
        Point centre = new Point();
        int l=this.calculeLongueur();
        centre.x=this.getSize().width/2;
        centre.y=this.getSize().height/2;
        for(double j=0;j<11;j++){ 
            orientation = 0;
            Polygon p = new Polygon();            
            for(int i=0;i<model.getRowCount()+1;i++){
                double x = (centre.x + (DIST_CENTRE+(j/10)*l)*Math.cos(Math.toRadians(orientation)));
                double y = (centre.y + (DIST_CENTRE+(j/10)*l)*Math.sin(Math.toRadians(orientation)));
                
                p.addPoint((int)x, (int)y);              
                orientation+=angle; 
           }             
                g.setColor(Color.gray);
                g.drawPolygon(p);
        }
    }
    
    /**
     * Dessine le fond en cercles
     * @param g 
     */
    private void drawDecoCercle(Graphics g){       
        int orientation=0;       
        double angle=360/model.getRowCount();
        Point centre = new Point();
        int l=this.calculeLongueur();
        centre.x=this.getSize().width/2;
        centre.y=this.getSize().height/2;
        for(double j=0;j<11;j++){                         
            g.setColor(Color.gray);
            g.drawOval((int) (centre.x - ((DIST_CENTRE + (j/10)*l))), (int) (centre.y - ((DIST_CENTRE + (j/10)*l))), (int) (2*(DIST_CENTRE + (j/10)*l)), (int) (2*(DIST_CENTRE + (j/10)*l)));
        }
    }
    
    /**
     * Dessine le polygone qui relie les curseurs entre eux
     * @param g 
     */
    private void drawPolygon(Graphics g){
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
        g.setColor(new Color((float) 0.7, 0, (float) 0.4, (float) 0.3));
        g.fillPolygon(xPoints, yPoints, listAxe.size());     
    }    
    
    //////////////////////////
    // Méthodes surchargées //
    //////////////////////////
    
    @Override
    public void paint(Graphics g) {
        this.drawPolygon(g);  
        this.drawDecoPolygone(g);
        //this.drawDecoCercle(g);
        super.paint(g);           
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        for (AxeComponent c : listAxe) {
            c.setBounds(0, 0, w, h);
        }
    }

    /**
     * Change la valeur du model lorsque celui-ci est notifié par l'axe
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
