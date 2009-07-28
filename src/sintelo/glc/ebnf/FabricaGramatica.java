

package sintelo.glc.ebnf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sintelo.glc.Gramatica;
import sintelo.glc.NaoTerminal;
import sintelo.glc.Regra;
import sintelo.glc.Simbolo;
import sintelo.glc.Terminal;



public class FabricaGramatica {

    private Map<String, NaoTerminal> naoTerminais;
    private Map<String, Terminal> terminais;
    private List<Regra> regras;

    private Gramatica gramatica;

    private Stack<Regra> pilha;

    private Map<NaoTerminal, Integer> filhos;

    public FabricaGramatica() {
        naoTerminais = new HashMap<String, NaoTerminal>();
        terminais = new HashMap<String, Terminal>();
        regras = new ArrayList<Regra>();

        gramatica = new Gramatica();

        pilha = new Stack<Regra>();

        filhos = new HashMap<NaoTerminal, Integer>();
    }

    public Gramatica getGramatica() {
        for(NaoTerminal nt : naoTerminais.values()){
            int p = nt.getNome().indexOf('.');
            if(p != -1){
                String nome = nt.getNome().replace('.', '_');
                while(naoTerminais.containsKey(nome)){
                    nome += "_";
                }
                nt.setNome(nome);
            }
        }
        return gramatica;
    }


    protected NaoTerminal getNaoTerminal(String nome){
        NaoTerminal nt = naoTerminais.get(nome);

        if(nt == null){
            nt = gramatica.novoNaoTerminal(nome);
            naoTerminais.put(nome, nt);
        }

        return nt;
    }

    protected Terminal getTerminal(String nome){
        Terminal t = terminais.get(nome);

        if(t == null){
            t = gramatica.novoTerminal(nome);
            terminais.put(nome, t);
        }

        return t;
    }

    public Regra novaRegra(String nome){
        NaoTerminal nt = getNaoTerminal(nome);
        Regra r = gramatica.novaRegra(nt);
        pilha.push(r);
        return r;
    }


    public void fechaRegra(){
        pilha.pop();
    }

    public Regra novaAlternativa(){
        Regra r = pilha.pop();
        NaoTerminal nt = r.getNaoTerminal();
        r = gramatica.novaRegra(nt);
        pilha.push(r);
        return r;
    }

    public void addNaoTerminal(String nome){
        NaoTerminal nt = getNaoTerminal(nome);
        Regra r = pilha.peek();
        r.add(nt);
    }

    public void addTerminal(String nome){
        Terminal t = getTerminal(nome);
        Regra r = pilha.peek();
        r.add(t);
    }

    public void novaAcao(int numero){
        Regra r = pilha.peek();
        r.addAcao(numero);
    }

    private NaoTerminal novoFilho(NaoTerminal nt){
        Integer i = filhos.get(nt);
        if(i == null){
            i = 0;
        }
        String nome = nt.getNome() + "." + i;
        NaoTerminal filho = getNaoTerminal(nome);

        i = i.intValue() + 1;
        filhos.put(nt, i);

        return filho;
    }

    public void elevaZeroMais(){
        Regra r = pilha.peek();
        Simbolo s = r.removeUltimo();

        NaoTerminal filho = novoFilho(r.getNaoTerminal());

        Regra rep = gramatica.novaRegra(filho);
        rep.add(s);
        rep.add(filho);

        Regra vazia = gramatica.novaRegra(filho);

        r.add(filho);
    }

    public void elevaUmMais(){
        Regra r = pilha.peek();
        Simbolo s = r.getUltimo();

        NaoTerminal filho = novoFilho(r.getNaoTerminal());

        Regra rep = gramatica.novaRegra(filho);
        rep.add(s);
        rep.add(filho);

        Regra vazia = gramatica.novaRegra(filho);

        r.add(filho);
    }

    public void empilha(){
        Regra r = pilha.peek();

        NaoTerminal filho = novoFilho(r.getNaoTerminal());

        Regra nova = gramatica.novaRegra(filho);

        pilha.push(nova);
    }

    public void novoGrupo(){
        Regra r = pilha.pop();
        NaoTerminal nt = r.getNaoTerminal();
        pilha.peek().add(nt);
    }

    public void novoOpcional(){
        Regra r = pilha.pop();

        NaoTerminal nt = r.getNaoTerminal();
        Regra nova = gramatica.novaRegra(nt);

        pilha.peek().add(nt);
    }





}
