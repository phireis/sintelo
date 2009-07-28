/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sintelo.glc.AcaoSemantica;
import sintelo.glc.NaoTerminal;
import sintelo.glc.Regra;
import sintelo.glc.TabelaParse;
import sintelo.glc.Terminal;
import sintelo.lexico.TabelaTransicao;

/**
 *
 * @author phireis
 */
public class GeradorReconhecedores {
    
    private TabelaTransicao tabelaTransicao;
    private TabelaParse tabelaParse;

    private int[][] tabelaLexico;
    private int[][] tabelaSintatico;
    private int[][] producoes;

    private String[] regras;

    private ArrayList<String> tokensNaoUtilizados = new ArrayList<String>();
    
    private int primeiroNaoTerminal;
    private int primeiraAcao;
    
    public GeradorReconhecedores(TabelaTransicao tabelaTransicao, TabelaParse tabelaParse) {
        this.tabelaTransicao = tabelaTransicao;
        this.tabelaParse = tabelaParse;

        if(tabelaTransicao == null){
            throw new GeradorException("Tabela de transição nula");
        }

        inicializaLexico();

        if(tabelaParse != null){
            inicializaSintatico();
            inicializaProducoes();
        }
    }
        

    private void inicializaLexico(){
        int[][] tabela = tabelaTransicao.getTabela();

        tabelaLexico = new int[tabela.length][256];

        for(int i = 0; i < 256; ++i){
            char c = (char)i;
            int col = tabelaTransicao.getColuna(c);
            if(col != -1){
                for(int j = 0; j < tabela.length; ++j){
                    tabelaLexico[j][i] = tabela[j][col];
                }
            }
            else{
                for(int j = 0; j < tabela.length; ++j){
                    tabelaLexico[j][i] = -1;
                }
            }
        }
    }

    private void inicializaSintatico(){
        Map<String, Integer> tokens = tabelaTransicao.getMapaTokens();
        tabelaSintatico = new int[tabelaParse.getQtdLinhas()][tokens.size()];
        int i = 0;
        for(String token : tokens.keySet()){
            int pos = tabelaParse.getColuna(token);
            if(pos == -1){ //token não utilizado na gramatica
                if(token.length() > 0){
                    tokensNaoUtilizados.add(token);
                }
                for(int j = 0; j < tabelaSintatico.length; ++j){
                    tabelaSintatico[j][i] = -1;
                }
            }
            else{
                for(int j = 0; j < tabelaSintatico.length; ++j){
                    tabelaSintatico[j][i] = tabelaParse.getValor(j, pos);
                }
            }
            ++i;
        }
    }

    public void inicializaProducoes(){
        primeiroNaoTerminal = tabelaTransicao.getMapaTokens().size();
        primeiraAcao = primeiroNaoTerminal + tabelaParse.getQtdLinhas();

        Set<Regra> regras = tabelaParse.getGramatica().getRegras();

        this.regras = new String[regras.size()];
        producoes = new int[regras.size()][];
        int i = 0;
        for(Regra r : regras){
            List lista = r.toList();
            int[] producao = new int[lista.size()];
            int j = 0;
            for(Object o : lista){
                if(o instanceof Terminal){
                    Terminal t = (Terminal) o;
                    Integer n = tabelaTransicao.getMapaTokens().get(t.getNome());
                    if(n == null){
                        throw new GeradorException("Token não declarado: " + t.getNome());
                    }
                    producao[j++] = n;
                }
                else if(o instanceof NaoTerminal){
                    NaoTerminal nt = (NaoTerminal) o;
                    int n = primeiroNaoTerminal + tabelaParse.getLinha(nt);
                    producao[j++] = n;
                }
                else if(o instanceof AcaoSemantica){
                    AcaoSemantica a = (AcaoSemantica) o;
                    int n = primeiraAcao + a.getNumero();
                    producao[j++] = n;
                }
            }
            this.regras[i] = r.toString();
            producoes[i++] = producao;
        }
    }

    public int getTokenIgnorado(){
        return tabelaTransicao.getAutomato().getTokenIgnorado();
    }

    public int[][] getProducoes() {
        return producoes;
    }

    public TabelaParse getTabelaParse() {
        return tabelaParse;
    }

    public int[][] getTabelaSintatico() {
        return tabelaSintatico;
    }

    public TabelaTransicao getTabelaTransicao() {
        return tabelaTransicao;
    }

    public List<String> getTokensNaoUtilizados() {
        return Collections.unmodifiableList(tokensNaoUtilizados);
    }

    public int[][] getTabelaLexico() {
        return tabelaLexico;
    }

    public int getPrimeiraAcao() {
        return primeiraAcao;
    }

    public int getPrimeiroNaoTerminal() {
        return primeiroNaoTerminal;
    }

    
    public String getToken(int n){
        return tabelaTransicao.getTokens()[n];
    }

    public String getNaoTerminal(int n){
        return tabelaParse.getNaoTerminal(n);
    }

    public String getRegra(int n){
        return regras[n];
    }
    

}
