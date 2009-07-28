/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author phireis
 */
public abstract class Mensagens {
    
    public static void informa(Component parent, String msg){
        JOptionPane.showMessageDialog(parent, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void alerta(Component parent, String msg, String titulo){
        JOptionPane.showMessageDialog(parent, msg, titulo, JOptionPane.WARNING_MESSAGE);
    }

}
