/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import java.util.LinkedHashSet;
import java.util.Set;
import sintelo.reconhecedor.Lexico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class LexicoObservavel implements Lexico{

    private Lexico lexico;
    private Set<ObservadorLexico> observadores;

    public LexicoObservavel() {
        this(null);
    }



    public LexicoObservavel(Lexico lexico) {
        this.lexico = lexico;
        observadores = new LinkedHashSet<ObservadorLexico>();
    }

    public synchronized void addObservador(ObservadorLexico obs){
        observadores.add(obs);
    }

    public synchronized void removeObservador(ObservadorLexico obs){
        observadores.remove(obs);
    }

    public synchronized void notifica(Token t){
        for(ObservadorLexico obs : observadores){
            obs.tokenReconhecido(t);
        }
    }


    public boolean temProximo() {
        return lexico.temProximo();
    }

    public Token proximo() {
        Token t = lexico.proximo();
        notifica(t);
        return t;
    }

    public Lexico getLexico() {
        return lexico;
    }

    public void setLexico(Lexico lexico) {
        this.lexico = lexico;
    }
   


}
