/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.metaparsers.sintatico;

import sintelo.glc.Gramatica;
import sintelo.reconhecedor.lexico.AnalisadorLexico;
import sintelo.reconhecedor.sintatico.AnalisadorSintatico;

/**
 *
 * @author phireis
 */
public class CompiladorSintatico {
    
    private AnalisadorLexico lexico;
    private AnalisadorSintatico sintatico;
    private SemanticoSintatico semantico;

    public CompiladorSintatico() {
        lexico = new AnalisadorLexico(new DadosLexico());
        sintatico = new AnalisadorSintatico(new DadosSintatico());
        semantico = new SemanticoSintatico();
    }

    public Gramatica compila(String entrada){
        lexico.setEntrada(entrada);
        sintatico.reconhece(lexico, semantico);
        return semantico.getGramatica();
    }

    public AnalisadorLexico getLexico() {
        return lexico;
    }

    public SemanticoSintatico getSemantico() {
        return semantico;
    }

    public AnalisadorSintatico getSintatico() {
        return sintatico;
    }

    

}
