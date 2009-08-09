
package sintelo.glc;

import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;


public interface Simbolo {

    public First getFirst();
    public boolean isGerador();
    public void calculaAlcancaveis(Alcancaveis a);

}
