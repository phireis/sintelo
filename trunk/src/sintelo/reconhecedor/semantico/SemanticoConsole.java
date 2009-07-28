/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.semantico;

import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class SemanticoConsole implements Semantico{

    public void executa(int acao, Token token) {
        System.out.println("#" + acao + ": " + token.getTexto());
    }

}
