

package sintelo.glc.conjuntos;

import sintelo.glc.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.print.attribute.HashAttributeSet;



public class ConjuntoElementos {

    private Set<NaoTerminal> naoTerminais;
    private Set<Terminal> terminais;
    private Set<Regra> regras;

    public ConjuntoElementos() {
        naoTerminais = new HashSet<NaoTerminal>();
        regras = new HashSet<Regra>();
        terminais = new HashSet<Terminal>();
    }

    public Set<Regra> getRegras() {
        return Collections.unmodifiableSet(regras);
    }

    public Set<NaoTerminal> getNaoTerminais() {
        return Collections.unmodifiableSet(naoTerminais);
    }

    public Set<Terminal> getTerminais(){
        return Collections.unmodifiableSet(terminais);
    }

    public void add(Regra r){
        regras.add(r);
    }

    public void add(NaoTerminal nt){
        naoTerminais.add(nt);
    }

    public void add(Terminal t){
        terminais.add(t);
    }

}
