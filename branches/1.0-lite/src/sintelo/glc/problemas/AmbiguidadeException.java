/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.glc.problemas;

import sintelo.glc.*;

/**
 *
 * @author phireis
 */
public class AmbiguidadeException extends RuntimeException{

    private NaoTerminal naoTerminal;
    private Terminal terminal;

    public AmbiguidadeException(NaoTerminal naoTerminal, Terminal terminal) {
        this.naoTerminal = naoTerminal;
        this.terminal = terminal;
    }


    public NaoTerminal getNaoTerminal() {
        return naoTerminal;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    @Override
    public String getMessage() {
        return "Ambiguidade na gramatica (" + naoTerminal + "; " + terminal + ").";
    }



    

}
