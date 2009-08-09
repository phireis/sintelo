/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.glc.conjuntos;

import sintelo.glc.NaoTerminal;
import sintelo.glc.problemas.RecursaoEsquerdaException;

/**
 *
 * @author phireis
 */
public class EstrategiaFirstRecursao implements EstrategiaFirst{
    

    public boolean retornaComRecursao(NaoTerminal nt) {
        throw new RecursaoEsquerdaException(nt);
    }

}
