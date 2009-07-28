/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sintelo.reconhecedor.Lexico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author Projetos
 */
public class LexicoLista implements Lexico{

    private Lexico lexico;
    private ArrayList<Token> tokens;

    public LexicoLista(Lexico lexico) {
        this.lexico = lexico;
        this.tokens = new ArrayList<Token>();
    }

    public boolean temProximo() {
        return lexico.temProximo();
    }

    public Token proximo() {
        Token t = lexico.proximo();
        tokens.add(t);
        return t;
    }

    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public void percorreEntrada(){
        while(temProximo()){
            proximo();
        }
        proximo();
    }

}
