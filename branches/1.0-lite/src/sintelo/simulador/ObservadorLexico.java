/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public interface ObservadorLexico {

    void tokenReconhecido(Token t);

}
