/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.lexico;

/**
 *
 * @author phireis
 */
public class AutomatoException extends RuntimeException{

    public AutomatoException(Throwable arg0) {
        super(arg0);
    }

    public AutomatoException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AutomatoException(String arg0) {
        super(arg0);
    }

    public AutomatoException() {
    }

    

}
