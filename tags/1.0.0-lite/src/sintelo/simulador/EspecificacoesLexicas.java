/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import sintelo.gerador.GeradorReconhecedores;
import sintelo.reconhecedor.lexico.EspecificacaoLexica;

/**
 *
 * @author phireis
 */
public class EspecificacoesLexicas implements EspecificacaoLexica{

    private GeradorReconhecedores gerador;

    public EspecificacoesLexicas(GeradorReconhecedores gerador) {
        this.gerador = gerador;
    }


    public int getDestino(int estado, char c) {
        try{
            return gerador.getTabelaLexico()[estado][c];
        }catch(Exception e){
            return -1;
        }
    }

    public int getTokenReconhecido(int estado) {
        return gerador.getTabelaTransicao().getReconhecedores()[estado];
    }

    public int getEstadoInicial() {
        return gerador.getTabelaTransicao().getInicial();
    }

    public int getTokenIgnorado() {
        return gerador.getTokenIgnorado();
    }

}
