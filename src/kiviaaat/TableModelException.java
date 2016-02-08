package kiviaaat;

public class TableModelException extends Exception {

    public TableModelException(){
      messageGenerique(); 
    }
    
    /**
     * @param msg le message à transmettre
     */
    public TableModelException(String msg) {   
        super(msg);
        messageGenerique();
    }
    
    public void messageGenerique(){
        System.err.println("TableModel format incorrect ");
        System.err.println("[nom][valeur][valeur min][valeur max]");
    }
    
}
