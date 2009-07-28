/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor;

/**
 *
 * @author phireis
 */
public class ErroLexico extends ErroAnalise{

    public ErroLexico(int posicao, String msg) {
        super(posicao, "Erro léxico: " + msg + " na posição " + posicao);
    }


}
