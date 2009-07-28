/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador;

import java.io.File;
import sintelo.gerador.java.GeradorExemploJava;
import sintelo.gerador.java.GeradorLexicoJava;
import sintelo.gerador.java.GeradorSintaticoJava;
import sintelo.persistencia.ArquivoEspecificacao;
import sintelo.persistencia.Especificacoes;
import sintelo.simulador.FabricaGerador;
import sintelo.utils.LeituraArquivo;

/**
 *
 * @author phireis
 */
public class GeradorSintelo {
    
    public static void geraMetaCompiladores(){
        //String elex1 = LeituraArquivo.leArquivo("doc/lexico.lex").trim();
        //String esin1 = LeituraArquivo.leArquivo("doc/lexico.sin");
        //String elex2 = LeituraArquivo.leArquivo("doc/gramatica.lex").trim();
        //String esin2 = LeituraArquivo.leArquivo("doc/gramatica.sin");

        Especificacoes espTokens = ArquivoEspecificacao.le("doc/sintelo-tokens.sintelo");
        Especificacoes espGramatica = ArquivoEspecificacao.le("doc/sintelo-gramatica.sintelo");
        
        String elex1 = espTokens.getEspecificacaoLexica();
        String esin1 = espTokens.getEspecificacaoSintatica();
        String elex2 = espGramatica.getEspecificacaoLexica();
        String esin2 = espGramatica.getEspecificacaoSintatica();
        
        GeradorReconhecedores gerador1 = FabricaGerador.criaGerador(elex1, esin1);
        GeradorReconhecedores gerador2 = FabricaGerador.criaGerador(elex2, esin2);
        
        GeradorLexicoJava glex1 = new GeradorLexicoJava(gerador1);
        GeradorSintaticoJava gsin1 = new GeradorSintaticoJava(gerador1);
        
        GeradorLexicoJava glex2 = new GeradorLexicoJava(gerador2);
        GeradorSintaticoJava gsin2 = new GeradorSintaticoJava(gerador2);
        
        String caminho = "src";
        String pacote1 = "sintelo.metaparsers.lexico";
        String pacote2 = "sintelo.metaparsers.sintatico";

        GeradorExemploJava gex = new GeradorExemploJava();
        
        glex1.gera(caminho, pacote1);
        gsin1.gera(caminho, pacote1);
        gex.gera(caminho, pacote1);

        glex2.gera(caminho, pacote2);
        gsin2.gera(caminho, pacote2);
    }

    public static void main(String[] args){
        geraMetaCompiladores();
    }

}
