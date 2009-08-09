

package sintelo.glc;

import sintelo.glc.problemas.RecursaoEsquerdaException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;



public class Cadeia {

    private ArrayList<Simbolo> simbolos;
    private ArrayList<AcaoSemantica> acoes;

    private First first;
    private Boolean gerador;


    public Cadeia() {
        simbolos = new ArrayList<Simbolo>();
        acoes = new ArrayList<AcaoSemantica>();
    }

    public void add(Simbolo s){
        simbolos.add(s);
    }

    public void addAcao(int numero){
        int pos = simbolos.size();
        acoes.add(new AcaoSemantica(numero, pos));
    }

    public Simbolo getUltimo(){
        if(simbolos.isEmpty()){
            return null;
        }
        return simbolos.get(simbolos.size() - 1);
    }

    public Simbolo removeUltimo(){
        if(simbolos.isEmpty()){
            return null;
        }
        return simbolos.remove(simbolos.size() - 1);
    }

    public List<Simbolo> getSimbolos() {
        return Collections.unmodifiableList(simbolos);
    }

    public List<AcaoSemantica> getAcoes() {
        return Collections.unmodifiableList(acoes);
    }






    public First getFirst(){
        if(first == null){
            calculaFirst();
        }
        return first;
    }

    private void calculaFirst(){
        if(simbolos.size() == 0){
            first = new First();
            first.add(Terminal.getEpsilon());
        }
        else{
            try{
                first = calculaFirst(simbolos.iterator());
            }catch(RecursaoEsquerdaException e){
                throw e;
            }
        }
    }

    public static First calculaFirst(Iterator<Simbolo> simbolos) {
        First first = new First();

        while(simbolos.hasNext()){
            Simbolo s = simbolos.next();
            First fs = s.getFirst();
            if(fs.temCadeiaVazia()){
                if(simbolos.hasNext()){
                    fs = fs.cloneSemVazio();
                }
                first.add(fs);
            }
            else{
                first.add(fs);
                break;
            }
        }

        return first;
    }

    private class IteradorAcoes{

        private List<AcaoSemantica> acoes;
        private int pos;
        private int size;

        public IteradorAcoes(){
            this.acoes = getAcoes();
            this.size = this.acoes.size();
            this.pos = 0;
        }

        public void get(List lista, int i){
            for(; pos < size; ++pos){
                AcaoSemantica a = acoes.get(pos);
                if(a.getPosicao() == i){
                    lista.add(a);
                }
                else{
                    break;
                }
            }
        }
    }
    
    public List toList(){
        int n = simbolos.size();
        IteradorAcoes ac = new IteradorAcoes();
        List lista = new ArrayList();
        for(int i = 0; i < n; ++i){
            ac.get(lista, i);
            lista.add(simbolos.get(i));
        }
        ac.get(lista, n);
        return lista;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        List lista = toList();

        if(lista.size() == 0){
            s.append(" î");
        }
        else{
            for(Object o : lista){
                s.append(' ');
                s.append(o.toString());
            }
        }
        return s.toString();
    }

    public void verificaGerador(){
        for(Simbolo s : simbolos){
            if(!s.isGerador()){
                gerador = false;
                return;
            }
        }
        gerador = true;
    }

    public boolean isGerador() {
        if(gerador == null){
            verificaGerador();
        }
        return gerador;
    }

    public void calculaAlcancaveis(Alcancaveis a){
        for(Simbolo s : simbolos){
            s.calculaAlcancaveis(a);
        }
    }

}
