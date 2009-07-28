/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.sintatico;

import java.util.Stack;
import sintelo.reconhecedor.ErroSintatico;
import sintelo.reconhecedor.Lexico;
import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class AnaliseSintatica {

    private EspecificacaoSintatica especificacao;
    private Lexico lexico;
    private Semantico semantico;
    private Stack<Integer> pilha;
    private Token token;
    private Token ultimo;

    public AnaliseSintatica(EspecificacaoSintatica especificacao, Lexico lexico, Semantico semantico) {
        this.especificacao = especificacao;
        this.lexico = lexico;
        this.semantico = semantico;
        
        inicia();
    }

    public EspecificacaoSintatica getEspecificacao() {
        return especificacao;
    }

    public Lexico getLexico() {
        return lexico;
    }

    public Stack<Integer> getPilha() {
        return pilha;
    }

    public Semantico getSemantico() {
        return semantico;
    }

    public Token getToken() {
        return token;
    }

    public Token getUltimo() {
        return ultimo;
    }
   

    
    private void inicia(){
        pilha = new Stack<Integer>();
        pilha.push(especificacao.getSimboloInicial());
        token = lexico.proximo();
        ultimo = null;
    }
    
    private static String getDescricaoToken(Token token){
        if(token.getNumero() == 0){
            return "fim da cadeia";
        }
        else{
            return "'" + token.getTexto() + "'";
        }
    }

    public boolean avanca(){
        Integer topo = pilha.pop();

        if(topo < especificacao.getPrimeiroNaoTerminal()){
            //terminal

            if(token.getNumero() != topo){
                String s = getDescricaoToken(token);
                throw new ErroSintatico(token.getPosicao(), "Não esperava " + s);
            }
            ultimo = token;
            token = lexico.proximo();
        }
        else if(topo < especificacao.getPrimeiraAcao()){
            //nao terminal
            topo -= especificacao.getPrimeiroNaoTerminal();
            int regra = especificacao.getDerivacao(topo, token.getNumero());
            if(regra == -1){
                String s = getDescricaoToken(token);
                throw new ErroSintatico(token.getPosicao(), "Sem regra para " + s);
            }
            int[] producao = especificacao.getProducao(regra);
            for(int i = producao.length; i > 0;){
                pilha.push(producao[--i]);
            }
        }
        else{
            //acao semantica
            topo -= especificacao.getPrimeiraAcao();
            semantico.executa(topo, ultimo);
        }

        if(pilha.size() == 0){
            if(token.getNumero() != 0){
                throw new ErroSintatico(token.getPosicao(), "Esperava fim de cadeia no lugar de '" + token.getTexto() + "'");
            }
            return false;
        }

        return true;

    }

}
