/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.metaparsers.lexico;

import sintelo.lexico.Automato;
import sintelo.reconhecedor.lexico.AnalisadorLexico;
import sintelo.reconhecedor.sintatico.AnalisadorSintatico;

/**
 *
 * @author phireis
 */
public class CompiladorLexico {

    private AnalisadorLexico lexico;
    private AnalisadorSintatico sintatico;
    private SemanticoLexico semantico;

    public CompiladorLexico() {
        lexico = new AnalisadorLexico(new DadosLexico());
        sintatico = new AnalisadorSintatico(new DadosSintatico());
        semantico = new SemanticoLexico();

    }
    
    public Automato compila(String entrada){
        lexico.setEntrada(entrada);
        sintatico.reconhece(lexico, semantico);
        return semantico.getAutomato();
    }

    public AnalisadorLexico getLexico() {
        return lexico;
    }

    public SemanticoLexico getSemantico() {
        return semantico;
    }

    public AnalisadorSintatico getSintatico() {
        return sintatico;
    }

    

}
