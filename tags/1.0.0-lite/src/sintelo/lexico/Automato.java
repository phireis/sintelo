

package sintelo.lexico;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sintelo.lexico.Estado;
import sintelo.utils.Lista;



public class Automato {

    private Set<Estado> estados;
    private Set<Character> caracteres;
    private FabricaEstados fabrica;
    private Estado estadoInicial;
    private Map<String, Integer> tokens;

    private int tokenIgnorado = -1;

    public Automato(FabricaEstados fabrica) {
        estados = new LinkedHashSet<Estado>();
        caracteres = new LinkedHashSet<Character>();
        this.fabrica = fabrica;
    }

    public Automato(){
        this(new FabricaEstados());
    }

    public int getTokenIgnorado() {
        return tokenIgnorado;
    }

    public void setTokenIgnorado(int tokenIgnorado) {
        this.tokenIgnorado = tokenIgnorado;
    }

    

    public Map<String, Integer> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, Integer> tokens) {
        this.tokens = tokens;
    }



    public Set<Character> getCaracteres() {
        return Collections.unmodifiableSet(caracteres);
    }

    public Set<Estado> getEstados() {
        return Collections.unmodifiableSet(estados);
    }

    public FabricaEstados getFabrica() {
        return fabrica;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }




    public synchronized Estado novoEstado(){
        Estado novo = fabrica.novoEstado();
        estados.add(novo);
        return novo;
    }

    public void novaTransicao(Estado origem, char condicao, Estado destino){
        caracteres.add(condicao);
        origem.novaTransicao(condicao, destino);
    }

    public void novaTransicaoVazia(Estado origem, Estado destino){
        origem.novaTransicaoVazia(destino);
    }
    
    public void novasTransicoes(Estado origem, char primeiro, char ultimo, Estado destino){
        if(primeiro > ultimo){
            throw new AutomatoException("Primeiro caractere maior que o último.");
        }
        for(char c = primeiro; c <= ultimo; ++c){
            novaTransicao(origem, c, destino);
        }
    }

    public void remove(Estado e){
        e.exclui();
        estados.remove(e);
    }

    public void minimiza(){
        eliminaTransicoesVazias();
        tornaDeterministico();
        eliminaInalcancaveis();
        eliminaMortos();
        eliminaEquivalencias();
    }

    public void eliminaTransicoesVazias(){
        for(Estado est : estados){
            est.eliminaTransicoesVazias();
        }
    }

    public void eliminaInalcancaveis(){
        if(estadoInicial == null){
            throw new AutomatoException("Autômato sem estado inicial!");
        }
        Set<Estado> alcancaveis = new LinkedHashSet<Estado>();
        estadoInicial.procuraAlcancaveis(alcancaveis);

        Iterator<Estado> it = estados.iterator();
        while(it.hasNext()){
            Estado e = it.next();
            if(!alcancaveis.contains(e)){
                e.exclui();
                it.remove();
            }
        }
    }

    public void eliminaMortos(){
        Set<Estado> vivos = new LinkedHashSet<Estado>();

        for(Estado e : estados){
            if(e.isReconhecedor()){
                e.procuraVivos(vivos);
            }
        }

        if(!vivos.contains(estadoInicial)){
            throw new AutomatoException("Estado inicial morto!");
        }

        Iterator<Estado> it = estados.iterator();
        while(it.hasNext()){
            Estado e = it.next();
            if(!vivos.contains(e)){
                e.exclui();
                it.remove();
            }
        }
    }

    public void tornaDeterministico(){
        Map<Set<Estado>, Estado> novos = new HashMap<Set<Estado>, Estado>();

        Lista<Estado> lista = new Lista<Estado>(estados);        
        for(Estado estado : lista){
            for(Character condicao : estado.getCondicoes()){
                Transicao t = estado.getTransicoes(condicao);
                Set<Estado> destinos = new LinkedHashSet<Estado>(t.getDestinos());
                if(destinos.size() > 1){
                    Estado novo = novos.get(destinos);
                    if(novo == null){
                        novo = this.novoEstado();
                        lista.add(novo);
                        novos.put(destinos, novo);
                        for(Estado est : destinos){
                            novo.setReconhecedor(est);
                            for(Character ch : est.getCondicoes()){
                                for(Estado destino : est.getTransicoes(ch).getDestinos()){
                                    novo.novaTransicao(ch, destino);
                                }
                            }
                        }
                    }                    
                    estado.remove(t);
                    estado.novaTransicao(t.getCondicao(), novo);
                }
            }
        }
    }

    public void eliminaEquivalencias(){
        Equivalencia eq = new Equivalencia(this);
        eq = eq.classifica();

        for(ClasseEquivalencia cl : eq.getClasses()){
            Iterator<Estado> it = cl.iterator();
            if(it.hasNext()){
                Estado e = it.next();
                while(it.hasNext()){
                    Estado es = it.next();
                    es.substitui(e);
                    remove(es);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Estado e : estados){
            sb.append(e)
                    .append('(')
                    .append(e.getReconhecedor())
                    .append(')')
                    .append(":\n");
            for(Estado es : e.getTransicoesVazias()){
                sb.append('(').append(es).append(")\n");
            }
            for(Transicao t : e.getTransicoes()){
                sb.append(t).append('\n');
            }
            sb.append('\n');
        }

        return sb.toString();
    }


    
}
