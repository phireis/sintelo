

package sintelo.lexico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;



public class Estado {

    public static final int NAO_RECONHECEDOR = Integer.MIN_VALUE;

    private int numero;
    private Map<Character, Transicao> destinos;
    private Origens origens;
    private Set<Estado> transicoesVazias;
    private int reconhecedor;

    private static class Origens{

        private Map<Estado, Set<Character>> origens;

        public Origens() {
            origens = new LinkedHashMap<Estado, Set<Character>>();
        }

        public void adiciona(Estado origem, char c){
            Set<Character> ch = origens.get(origem);

            if(ch == null){
                ch = new LinkedHashSet<Character>();
                origens.put(origem, ch);
            }

            ch.add(c);
        }
        
        public void remove(Estado origem, char c){
            Set<Character> ch = origens.get(origem);
            if(ch != null){
                ch.remove(c);
            }
        }

        public Set<Estado> getOrigens(){
            return Collections.unmodifiableSet(origens.keySet());
        }
        
        public Set<Character> getOrigens(Estado e){
            return Collections.unmodifiableSet(origens.get(e));
        }

    }

    public Estado(int numero) {
        this.numero = numero;
        this.destinos = new LinkedHashMap<Character, Transicao>();
        this.origens = new Origens();
        this.transicoesVazias = new LinkedHashSet<Estado>();
        this.reconhecedor = NAO_RECONHECEDOR;
    }

    public int getNumero() {
        return numero;
    }

    

    private Transicao getDestinos(char c){
        Transicao dest = destinos.get(c);
        if(dest == null){
            dest = new Transicao(this, c);
            destinos.put(c, dest);
        }
        return dest;
    }

    public boolean isReconhecedor(){
        return reconhecedor != NAO_RECONHECEDOR;
    }

    public int getReconhecedor() {
        return reconhecedor;
    }

    public void setReconhecedor(int reconhecedor) {
        this.reconhecedor = reconhecedor;
    }    


    public Estado getDestino(char c){
        Transicao t = destinos.get(c);
        if(t == null){
            return null;
        }
        return t.getDestino();
    }

    public void novaTransicaoVazia(Estado destino){
        transicoesVazias.add(destino);
    }

    public Set<Estado> getTransicoesVazias(){
        return Collections.unmodifiableSet(transicoesVazias);
    }

    public Transicao novaTransicao(char c, Estado destino){
        Transicao t = destinos.get(c);
        if(t == null){
            t = new Transicao(this, c);
            destinos.put(c, t);
        }
        t.novoDestino(destino);
        destino.origens.adiciona(this, c);
        return t;
    }

    public void remove(Transicao t){
        ArrayList<Estado> lista = new ArrayList<Estado>(t.getDestinos());
        for(Estado e : lista){
            removeTransicao(t.getCondicao(), e);
        }
    }

    public void removeTransicao(char c, Estado destino){
        Transicao t = destinos.get(c);
        if(t != null){
            t.remove(destino);
        }
        destino.origens.remove(this, c);
    }

    public Set<Character> getCondicoes(){
        return Collections.unmodifiableSet(destinos.keySet());
    }

    public Transicao getTransicoes(char c){
        return destinos.get(c);
    }

    public Collection<Transicao> getTransicoes(){
        return Collections.unmodifiableCollection(destinos.values());
    }

    public void exclui(){
        ArrayList<Transicao> transicoes = new ArrayList<Transicao>(destinos.values());
        for(Transicao t : transicoes){
            for(Estado e : t.getDestinos()){
                e.origens.remove(this, t.getCondicao());
            }
        }
        for(Estado orig : origens.getOrigens()){
            ArrayList<Character> chars = new ArrayList<Character>(origens.getOrigens(orig));
            for(Character c : chars){
                orig.removeTransicao(c, this);
            }
        }
    }
    
    public void substitui(Estado estado){
        ArrayList<Estado> origs = new ArrayList<Estado>(origens.getOrigens());
        for(Estado orig : origs){
            ArrayList<Character> chars = new ArrayList<Character>(origens.getOrigens(orig));
            for(Character c : chars){
                orig.removeTransicao(c, this);
                orig.novaTransicao(c, estado);
            }
        }
    }

    public void eliminaTransicoesVazias(){
        Set<Estado> tv = transicoesVazias;
        transicoesVazias = new LinkedHashSet<Estado>();
        for(Estado est : tv){
            if(est.transicoesVazias.size() > 0){
                est.eliminaTransicoesVazias();
            }
            for(Character ch : est.getCondicoes()){
                Transicao t = est.getDestinos(ch);
                for(Estado e : t.getDestinos()){
                    this.novaTransicao(ch, e);
                }
            }
            setReconhecedor(est);
        }
    }

    public void setReconhecedor(Estado estado){
        if(estado.reconhecedor > reconhecedor){
            reconhecedor = estado.reconhecedor;
        }
    }

    public void procuraAlcancaveis(Set<Estado> alcancaveis){
        if(alcancaveis.add(this)){
            for(Transicao t : destinos.values()){
                for(Estado dest : t.getDestinos()){
                    dest.procuraAlcancaveis(alcancaveis);
                }
            }
        }
    }

    public void procuraVivos(Set<Estado> vivos){
        if(vivos.add(this)){
            for(Estado e : origens.getOrigens()){
                e.procuraVivos(vivos);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estado other = (Estado) obj;
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return numero;
    }

    @Override
    public String toString() {
        return "q" + numero;
    }




}
