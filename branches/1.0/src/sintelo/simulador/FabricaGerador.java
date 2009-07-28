/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.simulador;

import sintelo.gerador.GeradorException;
import sintelo.gerador.GeradorReconhecedores;
import sintelo.glc.Gramatica;
import sintelo.glc.TabelaParse;
import sintelo.lexico.Automato;
import sintelo.lexico.TabelaTransicao;
import sintelo.metaparsers.lexico.CompiladorLexico;
import sintelo.metaparsers.sintatico.CompiladorSintatico;

/**
 *
 * @author phireis
 */
public class FabricaGerador {
    
    public static GeradorReconhecedores criaGerador(String elexica, String esintatica){
        return new GeradorReconhecedores(criaTabelaTransicao(elexica), criaTabelaParse(esintatica));
    }
    
    public static GeradorReconhecedores criaGerador(String elexica){
        return new GeradorReconhecedores(criaTabelaTransicao(elexica), null);
    }
    
    public static TabelaParse criaTabelaParse(String esintatica){
        CompiladorSintatico sintatico = new CompiladorSintatico();
                        
        Gramatica gramatica;
        
        try{
            gramatica = sintatico.compila(esintatica);
        }catch(Exception e){
            throw new GeradorException("Erro na gramática.\n" + e.getMessage(), e);
        }
        
        gramatica.calculaFollow();
                
        TabelaParse tabelaParse = new TabelaParse(gramatica);

        return tabelaParse;

    }
    
    public static TabelaTransicao criaTabelaTransicao(String elexica){
        CompiladorLexico lexico = new CompiladorLexico();                        
        Automato automato;
        
        try{
            automato = lexico.compila(elexica);
        }catch(Exception e){
            throw new GeradorException("Erro nas definições dos tokens.\n" + e.getMessage(), e);
        }
        
        automato.minimiza();
                
        TabelaTransicao tabelaTransicao = new TabelaTransicao(automato);

        return tabelaTransicao;
    }

}
