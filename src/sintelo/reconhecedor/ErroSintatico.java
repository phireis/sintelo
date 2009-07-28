/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor;

/**
 *
 * @author phireis
 */
public class ErroSintatico extends ErroAnalise{

    public ErroSintatico(int posicao, String msg) {
        super(posicao, "Erro sintático: " + msg + " na posição " + posicao);
    }
    
    

}
