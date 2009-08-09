

package sintelo.glc;

import sintelo.glc.problemas.RecursaoEsquerdaException;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;
import sintelo.glc.conjuntos.Follow;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


public class NaoTerminal implements Simbolo{

    private String nome;
    private Set<Regra> regras;

    private First first;
    private Follow follow;

    private int gerador = -1;
    private boolean trava = false;
    private Set<Regra> recursivas = new HashSet<Regra>();

    public NaoTerminal(String nome) {
        this.nome = nome;
        this.regras = new LinkedHashSet<Regra>();

        follow = new Follow();
    }

    public void add(Regra r){
        regras.add(r);
    }

    public void remove(Regra r){
        regras.remove(r);
    }

    public Iterator<Regra> regraIterator(){
        return regras.iterator();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public First getFirst(){
        if(trava){
            RecursaoEsquerdaException e = new RecursaoEsquerdaException(this);
            throw e;
        }
        trava = true;
        if(first == null){
            calculaFirst();
        }
        trava = false;
        return first;
    }
    
   
    private void calculaFirst() {
        first = new First();
        try{
            for(Regra r : regras){
                first.add(r.getFirst());
            }
        }catch(RecursaoEsquerdaException e){
            if(!e.isPronta()){
                if(e.getNaoTerminal() == this){
                    e.setPronta(true); 
                }
            }
            throw e;
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
        final NaoTerminal other = (NaoTerminal) obj;
        if (this.nome != other.nome && (this.nome == null || !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "<" + nome + ">";
    }


    public boolean adicionaFollow(Terminal t){
        return follow.add(t);
    }

    public boolean adicionaFollow(First f){
        return follow.add(f);
    }

    public boolean adicionaFollow(Follow f) {
        return follow.add(f);
    }

    public Follow getFollow() {
        return follow;
    }

    private void verificaGerador(){
        for(Regra r : regras){
            if(!recursivas.contains(r)){
                recursivas.add(r);
                boolean b = r.isGerador();
                recursivas.remove(r);
                if(b){
                    gerador = 1;
                    break;
                }                
            }
        }
        if(gerador != 1){
            gerador = 0;
        }
    }

    public boolean isGerador() {
        if(gerador == -1){
            verificaGerador();
        }
        return gerador == 1;
    }

    public Set<Regra> getRegras() {
        return Collections.unmodifiableSet(regras);
    }

    public void calculaAlcancaveis(Alcancaveis a) {
        if(!trava){
            trava = true;
            a.add(this);
            for(Regra r : regras){
                r.calculaAlcancaveis(a);
            }
            trava = false;
        }
    }

}
