/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.sintatico;

/**
 *
 * @author phireis
 */
public interface EspecificacaoSintatica {

    public int getSimboloInicial();
    public int getPrimeiroNaoTerminal();
    public int getPrimeiraAcao();
    public int getDerivacao(int simboloAtual, int token);
    public int[] getProducao(int numero);

}
