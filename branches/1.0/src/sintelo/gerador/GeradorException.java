/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador;

/**
 *
 * @author phireis
 */
public class GeradorException extends RuntimeException{

    public GeradorException(Throwable arg0) {
        super(arg0);
    }

    public GeradorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public GeradorException(String arg0) {
        super(arg0);
    }

    public GeradorException() {
    }

    

}
