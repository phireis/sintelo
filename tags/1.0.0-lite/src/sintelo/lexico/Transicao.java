

package sintelo.lexico;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;



public class Transicao {

    private Estado origem;
    private char condicao;
    private Set<Estado> destinos;

    public Transicao(Estado origem, char condicao) {
        this.origem = origem;
        this.condicao = condicao;
        this.destinos = new LinkedHashSet<Estado>();
    }

    public char getCondicao() {
        return condicao;
    }

    public Set<Estado> getDestinos() {
        return Collections.unmodifiableSet(destinos);
    }

    public Estado getOrigem() {
        return origem;
    }
    
    public Estado getDestino(){
        if(destinos.size() == 0){
            return null;
        }
        if(destinos.size() > 1){
            throw new AutomatoException("Autômato não determinístico!");
        }
        Iterator<Estado> it = destinos.iterator();
        it.hasNext();
        return it.next();
    }

    public void novoDestino(Estado destino){
        destinos.add(destino);
    }

    public void remove(Estado destino){
        destinos.remove(destino);
    }

    public void removeTodos(){
        destinos.clear();
    }

    @Override
    public String toString() {
        
        String ds;

        if(destinos.size() == 0){
            ds = "-";
        }
        else if(destinos.size() == 1){
            ds = destinos.iterator().next().toString();
        }
        else{
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            Iterator<Estado> it = destinos.iterator();
            it.hasNext();
            sb.append(it.next().toString());
            while(it.hasNext()){
                sb.append(", ");
                sb.append(it.next().toString());
            }
            sb.append('}');
            ds = sb.toString();
        }


        String s = "(" + condicao + "; " + ds + ")";

        return s.toString();
    }



}
