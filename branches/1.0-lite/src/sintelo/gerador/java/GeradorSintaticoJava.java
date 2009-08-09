/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador.java;

import sintelo.gerador.GeradorException;
import sintelo.gerador.GeradorReconhecedores;
import sintelo.gerador.GravadorCodigo;

/**
 *
 * @author phireis
 */
public class GeradorSintaticoJava {

    private GeradorReconhecedores gerador;

    public GeradorSintaticoJava(GeradorReconhecedores gerador) {
        this.gerador = gerador;
    }

    public void gera(String caminho, String pacote){
        
        GravadorCodigo out = null;
        try{
            ClasseJava classe = new ClasseJava(pacote, "DadosSintatico");
            out = new GravadorCodigo(caminho + "/" +  classe.getCaminho());
            
            out.novaLinha();
            if(classe.getPacote().length() > 0){
                out.escreve("package " + classe.getPacote() + ";");
            }
            out.novaLinha();
            out.novaLinha();
            out.escreve("import sintelo.reconhecedor.sintatico.EspecificacaoSintatica;");
            out.novaLinha();
            out.novaLinha();
            out.escreve("public class " + classe.getNome() + " implements EspecificacaoSintatica {");
            out.aumentaNivel();
            out.novaLinha();
            out.novaLinha();
            
            int[][] tabela = gerador.getTabelaSintatico();            
            out.escreve("private static final int[][] TABELA_PARSE = {");
            out.aumentaNivel();            
            for(int i = 0; i < tabela.length; ++i){
                out.identa();
                out.insere("{");
                int[] linha = tabela[i];
                for(int j = 0; j < linha.length; ++j){
                    if(j != 0){
                        out.insere(", ");
                    }
                    out.insere(String.valueOf(tabela[i][j]));
                }
                out.insere("}");
                if(i != tabela.length - 1){
                    out.insere(",");
                }
                out.novaLinha();
            }
            out.diminuiNivel();
            out.escreve("};");
            
            out.novaLinha();
            out.novaLinha();

            int[] tokensReconhecidos = gerador.getTabelaTransicao().getReconhecedores();

            tabela = gerador.getProducoes();
            out.escreve("private static final int[][] PRODUCOES = {");
            out.aumentaNivel();            
            for(int i = 0; i < tabela.length; ++i){
                out.identa();
                out.insere("{");
                int[] linha = tabela[i];
                for(int j = 0; j < linha.length; ++j){
                    if(j != 0){
                        out.insere(", ");
                    }
                    out.insere(String.valueOf(tabela[i][j]));
                }
                out.insere("}");
                if(i != tabela.length - 1){
                    out.insere(",");
                }
                out.novaLinha();
            }
            out.diminuiNivel();
            out.escreve("};");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getDerivacao(int s, int t) {");
            out.aumentaNivel();
            out.escreve("return TABELA_PARSE[s][t];");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int[] getProducao(int n) {");
            out.aumentaNivel();
            out.escreve("return PRODUCOES[n];");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getPrimeiroNaoTerminal() {");
            out.aumentaNivel();
            int primeiroNt = gerador.getPrimeiroNaoTerminal();
            out.escreve("return " + String.valueOf(primeiroNt) + ";");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getPrimeiraAcao() {");
            out.aumentaNivel();
            int primeiraAcao = gerador.getPrimeiraAcao();
            out.escreve("return " + String.valueOf(primeiraAcao) + ";");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getSimboloInicial() {");
            out.aumentaNivel();
            int inicial = gerador.getTabelaParse().getInicial();
            out.escreve("return " + String.valueOf(inicial + primeiroNt) + ";");
            out.diminuiNivel();
            out.escreve("}");

            out.diminuiNivel();
            out.escreve("}");

            
        }catch(GeradorException e){
            throw e;
        }finally{
            if(out != null){
                out.fecha();
            }
        }
    }



}
