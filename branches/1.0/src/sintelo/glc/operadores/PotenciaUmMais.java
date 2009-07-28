

package sintelo.glc.operadores;

import sintelo.glc.*;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;



public class PotenciaUmMais implements Simbolo{

    private Simbolo simbolo;

    public PotenciaUmMais(Simbolo simbolo) {
        this.simbolo = simbolo;
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }



    public First getFirst() {
        return simbolo.getFirst();
    }

    public boolean isGerador() {
        return simbolo.isGerador();
    }

    public void calculaAlcancaveis(Alcancaveis a) {
        simbolo.calculaAlcancaveis(a);
    }

    @Override
    public String toString() {
        return simbolo + "+";
    }

}
