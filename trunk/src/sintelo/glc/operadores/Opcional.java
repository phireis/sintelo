

package sintelo.glc.operadores;

import java.util.logging.Level;
import java.util.logging.Logger;
import sintelo.glc.*;
import sintelo.glc.conjuntos.First;



public class Opcional extends Cadeia implements Simbolo{

    private First first = null;

    @Override
    public String toString() {
        return '[' + super.toString() + ']';
    }

    @Override
    public First getFirst() {
        if(first == null){
            First f = super.getFirst();
            if(f.temCadeiaVazia()){
                try {
                    first = f.clone();
                    first.add(Terminal.getEpsilon());
                } catch (CloneNotSupportedException ex) {
                    //nada
                }
            }
            else{
                first = f;
            }
        }
        return first;
    }




}
