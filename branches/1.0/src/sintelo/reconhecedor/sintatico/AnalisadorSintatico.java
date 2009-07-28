/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.sintatico;

import sintelo.reconhecedor.Lexico;
import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.semantico.SemanticoNulo;

/**
 *
 * @author phireis
 */
public class AnalisadorSintatico {

    private EspecificacaoSintatica especificacao;

    public AnalisadorSintatico(EspecificacaoSintatica especificacao) {
        this.especificacao = especificacao;

         
    }
    
    public void reconhece(Lexico lexico, Semantico semantico) {
        if(semantico == null){
            semantico = new SemanticoNulo();
        }
        AnaliseSintatica analise = new AnaliseSintatica(especificacao, lexico, semantico);
        while(analise.avanca()){

        }
    }

}
