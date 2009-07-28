/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import java.util.ArrayList;
import java.util.Stack;
import sintelo.gerador.GeradorReconhecedores;
import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.lexico.AnalisadorLexico;
import sintelo.reconhecedor.sintatico.AnaliseSintatica;

/**
 *
 * @author phireis
 */
public class Simulador {

    private GeradorReconhecedores gerador;
    private AnaliseSintatica sintatico;
    private LexicoObservavel lexico;
    private Semantico semantico;
    private boolean terminou;

    public Simulador(GeradorReconhecedores gerador, Semantico semantico) {
        this.gerador = gerador;
        this.semantico = semantico;

        lexico = new LexicoObservavel();
    }
    
    public Simulador(GeradorReconhecedores gerador, Semantico semantico, ObservadorLexico obs) {
        this(gerador, semantico);
        lexico.addObservador(obs);
    }
    

    public GeradorReconhecedores getGerador() {
        return gerador;
    }

    public LexicoObservavel getLexico() {
        return lexico;
    }

    public Semantico getSemantico() {
        return semantico;
    }

    public AnaliseSintatica getSintatico() {
        return sintatico;
    }

    public boolean avanca(){
        if(terminou){
            return true;
        }
        return terminou = !sintatico.avanca();
    }

    public boolean analiseTerminou() {
        return terminou;
    }


    public void novaSimulacao(String entrada){
        EspecificacoesLexicas especificacoesLexicas = new EspecificacoesLexicas(gerador);
        EspecificacoesSintaticas especificacoesSintaticas = new EspecificacoesSintaticas(gerador);

        AnalisadorLexico analisadorLexico = new AnalisadorLexico(especificacoesLexicas);
        analisadorLexico.setEntrada(entrada);

        lexico.setLexico(analisadorLexico);
        sintatico = new AnaliseSintatica(especificacoesSintaticas, lexico, semantico);

        terminou = false;
    }

    public String getGramatica(){
        return gerador.getTabelaParse().getGramatica().toString();
    }

    public String toString(int simbolo){
        if(simbolo < gerador.getPrimeiroNaoTerminal()){
            return gerador.getToken(simbolo);
        }
        if(simbolo < gerador.getPrimeiraAcao()){
            return gerador.getNaoTerminal(simbolo - gerador.getPrimeiroNaoTerminal());
        }
        return "#" + (simbolo - gerador.getPrimeiraAcao());
    }

    public String getPilha(){
        Stack<Integer> pilha = sintatico.getPilha();
        ArrayList<String> itens = new ArrayList<String>();

        for(Integer i : pilha){
            itens.add(toString(i));
        }

        StringBuilder sb = new StringBuilder();

        int pos = itens.size() - 1;
        for(int i = pos; i >= 0; --i){
            String s = itens.get(i);
            sb.append(s);
            sb.append('\n');
        }

        return sb.toString();
    }



}
