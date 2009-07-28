

package sintelo.lexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class TabelaTransicao {

    private int[][] tabela;
    private String[] colunas;
    private int[] reconhecedores;
    private Map<String, Integer> mapaTokens;
    private int inicial;

    private String[] tokens;
    
    private Map<Estado, Integer> posicoes = new LinkedHashMap<Estado, Integer>();
    private Map<Character, Integer> indiceColunas = new HashMap<Character, Integer>();

    private Automato automato;

    public TabelaTransicao(Automato automato) {
        this.automato = automato;
        Set<Character> caracteres = automato.getCaracteres();
        colunas = new String[caracteres.size()];

        int i = 0;
        for(Character c : caracteres){
            indiceColunas.put(c, i);
            colunas[i++] = c.toString();
        }

        
        int pos = 0;
        tabela = new int[automato.getEstados().size()][];
        for(Estado estado : automato.getEstados()){
            posicoes.put(estado, pos++);
        }

        reconhecedores = new int[automato.getEstados().size()];
        pos = 0;
        for(Estado estado : automato.getEstados()){
            if(estado == automato.getEstadoInicial()){
                inicial = pos;
            }
            int[] linha = new int[colunas.length];
            i = 0;
            for(Character c : caracteres){
                Estado destino = estado.getDestino(c);
                linha[i++] = destino == null ? -1 : posicoes.get(destino);
            }
            reconhecedores[pos] = estado.getReconhecedor();
            tabela[pos++] = linha;
        }

        mapaTokens = automato.getTokens();
        tokens = new String[mapaTokens.size()];
        for(Map.Entry<String, Integer> e : mapaTokens.entrySet()){
            tokens[e.getValue()] = e.getKey();
        }


    }

    public Automato getAutomato() {
        return automato;
    }

    

    public int getColuna(char c){
        Integer i = indiceColunas.get(c);
        if(i == null){
            return -1;
        }
        return i;
    }


    public Map<String, Integer> getMapaTokens() {
        return Collections.unmodifiableMap(mapaTokens);
    }

    public String[] getTokens() {
        return tokens;
    }

    



    

    public int getQtdColunas() {
        return colunas.length;
    }

    public int getQtdLinhas() {
        return tabela.length;
    }



    public String[] getColunas() {
        return colunas;
    }

    public int[][] getTabela() {
        return tabela;
    }

    public int getInicial() {
        return inicial;
    }

    public int[] getReconhecedores() {
        return reconhecedores;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("E:\t");
        for(int i = 0; i < colunas.length; ++i){
            sb.append(colunas[i]);
            sb.append('\t');
        }
        
        for(int i = 0; i < tabela.length; ++i){
            sb.append('\n');
            if(i == inicial){
                sb.append('>');
            }
            sb.append(i);
            if(reconhecedores[i] != Estado.NAO_RECONHECEDOR){
                sb.append('(');
                sb.append(reconhecedores[i]);
                sb.append(')');
            }
            sb.append('\t');
            for(int j = 0; j < colunas.length; ++j){
                sb.append(tabela[i][j]);
                sb.append('\t');
            }
        }
        
        sb.append('\n');

        return sb.toString();
    }


    


}
