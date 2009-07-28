

package sintelo.glc.operadores;

import sintelo.glc.*;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;



public class PotenciaZeroMais implements Simbolo{

    private Simbolo simbolo;
    private First first = null;

    public PotenciaZeroMais(Simbolo simbolo) {
        this.simbolo = simbolo;
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }



    public First getFirst() {
        if(first == null){
            try {
                First f = simbolo.getFirst();
                if(f.temCadeiaVazia()){
                    first = f;
                }
                else{
                    first = f.clone();
                    first.add(Terminal.getEpsilon());
                }
            } catch (CloneNotSupportedException ex) {
                //...
            }
        }
        return first;
    }

    public boolean isGerador() {
        return simbolo.isGerador();
    }

    public void calculaAlcancaveis(Alcancaveis a) {
        simbolo.calculaAlcancaveis(a);
    }

    @Override
    public String toString() {
        return simbolo + "*";
    }

}
