/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.persistencia;

import java.io.Serializable;

/**
 *
 * @author phireis
 */
public class Especificacoes implements Serializable{

    private String especificacaoLexica;
    private String especificacaoSintatica;

    public Especificacoes(String especificacaoLexica, String especificacaoSintatica) {
        this.especificacaoLexica = especificacaoLexica;
        this.especificacaoSintatica = especificacaoSintatica;
    }

    public String getEspecificacaoLexica() {
        return especificacaoLexica;
    }

    public void setEspecificacaoLexica(String especificacaoLexica) {
        this.especificacaoLexica = especificacaoLexica;
    }

    public String getEspecificacaoSintatica() {
        return especificacaoSintatica;
    }

    public void setEspecificacaoSintatica(String especificacaoSintatica) {
        this.especificacaoSintatica = especificacaoSintatica;
    }

    

}
