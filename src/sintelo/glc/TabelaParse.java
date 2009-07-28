/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.glc;

import sintelo.glc.problemas.AmbiguidadeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sintelo.glc.conjuntos.First;
import sintelo.glc.conjuntos.Follow;

/**
 *
 * @author phireis
 */
public class TabelaParse {

    private int inicial;
    private int[][] tabela;
    private String[] linhas;
    private String[] colunas;
    private Map<Coordenada, Conflito> conflitos;


    private class Coordenada{
        int x;
        int y;

        public Coordenada(int x, int y) {
            this.x = x;
            this.y = y;
        }



        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Coordenada other = (Coordenada) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + this.x;
            hash = 37 * hash + this.y;
            return hash;
        }


    }
    public class Conflito{
        private int linha;
        private int coluna;
        private Map<Integer, Regra> valores = new HashMap<Integer, Regra>();

        public Conflito(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
        }
        
        public String getNaoTerminal(){
            return getLinhas()[linha];
        }
        public String getTerminal(){
            return getColunas()[coluna];
        }



        public void resolve(int val){
            tabela[linha][coluna] = val;
            conflitos.remove(new Coordenada(linha, coluna));
        }

        public int getColuna() {
            return coluna;
        }

        public void setColuna(int coluna) {
            this.coluna = coluna;
        }

        public int getLinha() {
            return linha;
        }

        public void setLinha(int linha) {
            this.linha = linha;
        }

        public Map<Integer, Regra> getValores() {
            return Collections.unmodifiableMap(valores);
        }

        public void adiciona(int val, Regra regra){
            valores.put(val, regra);
        }

    }


    private Map<NaoTerminal, Integer> mapaNaoTerminais = new HashMap<NaoTerminal, Integer>();
    private Map<String, Integer> mapaTerminais = new HashMap<String, Integer>();
    private Map<Regra, Integer> mapaRegras = new HashMap<Regra, Integer>();

    private Gramatica gramatica;


    public TabelaParse(Gramatica gramatica) {
        this.gramatica = gramatica;

        Set<NaoTerminal> naoTerminais = gramatica.getNaoTerminals();
        Set<Terminal> terminais = gramatica.getTerminais();
        Set<Regra> regras = gramatica.getRegras();
        
        linhas = new String[naoTerminais.size()];
        colunas = new String[terminais.size()];

        conflitos = new HashMap<Coordenada, Conflito>();

        tabela = new int[naoTerminais.size()][terminais.size()];

        for(int i = 0; i < tabela.length; ++i){
            for(int j = 0; j < terminais.size(); ++j){
                tabela[i][j] = -1;
            }
        }

        int pos = 0;
        for(NaoTerminal nt : naoTerminais){
            mapaNaoTerminais.put(nt, pos);
            linhas[pos++] = nt.toString();
        }

        inicial = mapaNaoTerminais.get(gramatica.getSimboloInicial());

        pos = 0;
        for(Terminal t : terminais){
            mapaTerminais.put(t.getNome(), pos);
            colunas[pos++] = t.getNome();
        }

        pos = 0;
        for(Regra r : regras){
            mapaRegras.put(r, pos++);
        }
        Regra[] vetorRegras = regras.toArray(new Regra[0]);

        for(Regra r : regras){
            First first = r.getFirst();
            int i = mapaNaoTerminais.get(r.getNaoTerminal());
            for(Terminal t : first){
                int j = mapaTerminais.get(t.getNome());
                int val = mapaRegras.get(r);
                if(tabela[i][j] != -1){
                    if(tabela[i][j] != val){
                        if(!t.equals(Terminal.getEpsilon())){
                            Coordenada coord = new Coordenada(i, j);
                            Conflito conflito = conflitos.get(coord);
                            if(conflito == null){
                                conflito = new Conflito(i, j);
                                int ant = tabela[i][j];
                                conflito.adiciona(ant, vetorRegras[ant]);
                                conflitos.put(coord, conflito);
                            }
                            conflito.adiciona(val, r);                                
                            //throw new AmbiguidadeException(r.getNaoTerminal(), t);
                        }
                    }
                }
                tabela[i][j] = val;
            }
            if(first.temCadeiaVazia()){
                Follow follow = r.getNaoTerminal().getFollow();
                for(Terminal t : follow){
                    int j = mapaTerminais.get(t.getNome());
                    int val = mapaRegras.get(r);
                    if(tabela[i][j] != -1){
                        if(tabela[i][j] != val){
                            if(!t.equals(Terminal.getEpsilon())){
                                Coordenada coord = new Coordenada(i, j);
                                Conflito conflito = conflitos.get(coord);
                                if(conflito == null){
                                    conflito = new Conflito(i, j);
                                    int ant = tabela[i][j];
                                    conflito.adiciona(ant, vetorRegras[ant]);
                                    conflitos.put(coord, conflito);
                                }
                                conflito.adiciona(val, r);
                                //throw new AmbiguidadeException(r.getNaoTerminal(), t);
                            }
                        }
                    }
                    tabela[i][j] = val;
                }
            }
        }

    }

    public String getNaoTerminal(int n){
        return linhas[n];
    }

    public int getNumeroRegra(Regra regra){
        Integer i = mapaRegras.get(regra);
        if(i == null){
            return -1;
        }
        return i;
    }

    public Gramatica getGramatica() {
        return gramatica;
    }

    

    public int getLinha(NaoTerminal nt){
        Integer i = mapaNaoTerminais.get(nt);
        if(i == null){
            return -1;
        }
        return i;
    }
    
    public int getColuna(String token){
        Integer i = mapaTerminais.get(token);
        if(i == null){
            return -1;
        }
        return i;
    }
    
    public int getQtdLinhas(){
        return linhas.length;
    }
    
    public int getQtdColunas(){
        return colunas.length;
    }

    public String[] getColunas() {
        return colunas;
    }

    public int getInicial() {
        return inicial;
    }

    public String[] getLinhas() {
        return linhas;
    }

    public int getValor(int i, int j){
        return tabela[i][j];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NT:\t");

        for(int i = 0; i < getQtdColunas(); ++i){
            sb.append(colunas[i]).append('\t');
        }
        sb.append('\n');
        for(int i = 0; i < getQtdLinhas(); ++i){
            sb.append(linhas[i]).append('\t');
            for(int j = 0; j < getQtdColunas(); ++j){
                sb.append(tabela[i][j]).append('\t');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public Collection<Conflito> getConflitos() {
        return Collections.unmodifiableCollection(conflitos.values());
    }


    public Conflito getConflito(int i, int j){
        return conflitos.get(new Coordenada(i, j));
    }


}
