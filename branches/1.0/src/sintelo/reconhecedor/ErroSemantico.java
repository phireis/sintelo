

package sintelo.reconhecedor;

/**
 *
 * @author phireis
 */
public class ErroSemantico extends ErroAnalise{

    public ErroSemantico(String msg){
        this(-1, msg);
    }

    public ErroSemantico(int posicao, String msg) {
        super(posicao, "Erro semântico: " + msg);
    }

}
