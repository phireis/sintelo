/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.lexico;

/**
 *
 * @author phireis
 */
public interface EspecificacaoLexica {

    public int getDestino(int estado, char c);
    public int getTokenReconhecido(int estado);
    public int getEstadoInicial();
    public int getTokenIgnorado();

}
