
package sintelo.glc;

import sintelo.glc.conjuntos.First;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.Inalcancaveis;
import sintelo.glc.conjuntos.NaoGeradores;


public class Gramatica {

    private Set<Regra> regras;
    private Set<NaoTerminal> naoTerminais;
    private Set<Terminal> terminais;

    private NaoTerminal simboloInicial;

    private int seqTerminal = 2;
    private int seqRegra = 0;

    public Gramatica() {
        regras = new LinkedHashSet<Regra>();
        naoTerminais = new LinkedHashSet<NaoTerminal>();
        terminais = new LinkedHashSet<Terminal>();

        terminais.add(Terminal.getEpsilon());
        terminais.add(Terminal.getDollar());
    }



    public Set<NaoTerminal> getNaoTerminals(){
        return Collections.unmodifiableSet(naoTerminais);
    }

    public Set<Regra> getRegras() {
        return Collections.unmodifiableSet(regras);
    }

    public Set<Terminal> getTerminais() {
        return Collections.unmodifiableSet(terminais);
    }


    public NaoTerminal getSimboloInicial() {
        return simboloInicial;
    }

    public void setSimboloInicial(NaoTerminal simboloInicial) {
        this.simboloInicial = simboloInicial;
    }



    public NaoTerminal novoNaoTerminal(String nome){
        NaoTerminal nt = new NaoTerminal(nome);
        if(naoTerminais.isEmpty()){
            setSimboloInicial(nt);
        }
        naoTerminais.add(nt);
        return nt;
    }

    public Regra novaRegra(NaoTerminal naoTerminal){
        Regra regra = new Regra(seqRegra++, naoTerminal);
        naoTerminal.add(regra);
        regras.add(regra);
        return regra;
    }

    public Terminal novoTerminal(String nome){
        Terminal t = new Terminal(seqTerminal++, nome);
        terminais.add(t);
        return t;
    }

    public Terminal novoTerminal(){
        return novoTerminal("");
    }

    public void calculaFirst(){
        if(simboloInicial == null){
            throw new GlcException("Gramática sem símbolo inicial.");
        }
        for(NaoTerminal nt : naoTerminais){
            nt.getFirst();
        }
        for(Regra r : regras){
            r.getFirst();
        }
    }

    public void calculaFollow(){
        calculaFirst();

        simboloInicial.adicionaFollow(Terminal.getDollar());

        boolean mudou;
        do{
            System.out.println("-------------");
            mudou = false;
            for(Regra r : regras){
                Iterator<Simbolo> it = r.getSimbolos().iterator();
                NaoTerminal ant = null;
                while(it.hasNext()){
                    Simbolo atual = it.next();

                    First first = atual.getFirst();
                    if(ant != null){
                        if(ant.adicionaFollow(first)){
                            mudou = true;
                        }
                        if(first.temCadeiaVazia()){
                            if(atual instanceof NaoTerminal){
                                NaoTerminal nt = (NaoTerminal)atual;
                                if(ant.adicionaFollow(nt.getFollow())){
                                    mudou = true;
                                }
                            }
                        }
                    }

                    if(atual instanceof NaoTerminal){
                        ant = (NaoTerminal)atual;
                    }
                    else{
                        ant = null;
                    }
                }
                if(ant != null){
                    if(ant.adicionaFollow(r.getNaoTerminal().getFollow())){
                        mudou = true;
                    }
                }
            }
        } while(mudou);
    }

    public Inalcancaveis encontraInalcancaveis(){

        Alcancaveis alc = new Alcancaveis();
        if(simboloInicial != null){
            simboloInicial.calculaAlcancaveis(alc);
        }
        

        alc.add(Terminal.getDollar());
        alc.add(Terminal.getEpsilon());

        Inalcancaveis inalc = new Inalcancaveis();
        for(Regra r : regras){
            if(!alc.getRegras().contains(r)){
                inalc.add(r);
            }
        }
        for(NaoTerminal nt : naoTerminais){
            if(!alc.getNaoTerminais().contains(nt)){
                inalc.add(nt);
            }
        }
        for(Terminal t : terminais){
            if(!alc.getTerminais().contains(t)){
                inalc.add(t);
            }
        }

        return inalc;
    }

    public NaoGeradores encontraNaoGeradores(){
        NaoGeradores naoGeradores = new NaoGeradores();

        for(NaoTerminal nt : naoTerminais){
            if(!nt.isGerador()){
                naoGeradores.add(nt);
            }
        }

        if(naoGeradores.getNaoTerminais().contains(simboloInicial)){
            throw new GlcException("O símbolo inicial da gramática não é produtivo!");
        }
        for(Regra r : regras){
            r.verificaGerador();
            if(!r.isGerador()){
                naoGeradores.add(r);
            }
        }
        return naoGeradores;
    }

    public void remove(NaoTerminal nt){
        for(Regra r : nt.getRegras()){
            regras.remove(r);
        }
        naoTerminais.remove(nt);
    }

    public void remove(Regra r){
        regras.remove(r);
        r.getNaoTerminal().remove(r);
    }

    public void remove(Terminal t){
        terminais.remove(t);
    }

    public void removeInuteis(){
        NaoGeradores ng = encontraNaoGeradores();
        for(NaoTerminal nt : ng.getNaoTerminais()){
            remove(nt);
        }
        for(Regra r : ng.getRegras()){
            remove(r);
        }

        Inalcancaveis inalc = encontraInalcancaveis();
        for(NaoTerminal nt : inalc.getNaoTerminais()){
            remove(nt);
        }
        for(Regra r : inalc.getRegras()){
            remove(r);
        }
        for(Terminal t : inalc.getTerminais()){
            remove(t);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for(Regra r : regras){
            s.append(r.toString());
            s.append('\n');
        }

        return s.toString();
    }



}
