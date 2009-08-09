/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import sintelo.gerador.GeradorReconhecedores;
import sintelo.reconhecedor.sintatico.EspecificacaoSintatica;

/**
 *
 * @author phireis
 */
public class EspecificacoesSintaticas implements EspecificacaoSintatica{

    private GeradorReconhecedores gerador;

    public EspecificacoesSintaticas(GeradorReconhecedores gerador) {
        this.gerador = gerador;
    }

    public int getSimboloInicial() {
        return gerador.getTabelaParse().getInicial() + gerador.getPrimeiroNaoTerminal();
    }

    public int getPrimeiroNaoTerminal() {
        return gerador.getPrimeiroNaoTerminal();
    }

    public int getPrimeiraAcao() {
        return gerador.getPrimeiraAcao();
    }
    
    public int[] getProducao(int numero){
        return gerador.getProducoes()[numero];
    }

    public int getDerivacao(int simboloAtual, int token) {
        return gerador.getTabelaSintatico()[simboloAtual][token];
    }

}
