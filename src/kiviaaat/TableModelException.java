/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiviaaat;

/**
 *
 * @author astierre
 */
public class TableModelException extends Exception {

    /**
     * Creates a new instance of <code>TableModelException</code> without detail
     * message.
     */
    public TableModelException() {
      messageGenerique();
        
    }

    
    public void messageGenerique(){
          System.err.println("TableModel format incorrect ");
        System.err.println("[nom][valeur][valeur min][valeur max]");
    }
    /**
     * Constructs an instance of <code>TableModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TableModelException(String msg) {   
        super(msg);
                  messageGenerique();
    }
}
