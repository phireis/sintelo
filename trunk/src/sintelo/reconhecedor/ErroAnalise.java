/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor;

/**
 *
 * @author phireis
 */
public class ErroAnalise extends RuntimeException{

    private int posicao;

    public ErroAnalise(int posicao, String msg){
        super(msg);
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }

    


}
