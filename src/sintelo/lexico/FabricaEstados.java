/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.lexico;

/**
 *
 * @author phireis
 */
public class FabricaEstados {

    private int seq = 0;

    public synchronized Estado novoEstado(){
        return new Estado(seq++);
    }

}
