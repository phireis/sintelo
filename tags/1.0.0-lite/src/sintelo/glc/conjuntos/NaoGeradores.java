

package sintelo.glc.conjuntos;

import sintelo.glc.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sintelo.glc.NaoTerminal;
import sintelo.glc.Regra;



public class NaoGeradores {

    private Set<NaoTerminal> naoTerminais;
    private Set<Regra> regras;

    public NaoGeradores() {
        naoTerminais = new HashSet<NaoTerminal>();
        regras = new HashSet<Regra>();
    }

    public Set<NaoTerminal> getNaoTerminais() {
        return Collections.unmodifiableSet(naoTerminais);
    }

    public Set<Regra> getRegras() {
        return Collections.unmodifiableSet(regras);
    }

    public void add(NaoTerminal nt){
        naoTerminais.add(nt);
    }

    public void add(Regra r){
        regras.add(r);
    }


}
