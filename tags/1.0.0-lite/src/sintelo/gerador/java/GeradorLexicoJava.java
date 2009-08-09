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
public class GeradorLexicoJava {

    private GeradorReconhecedores gerador;

    public GeradorLexicoJava(GeradorReconhecedores gerador) {
        this.gerador = gerador;
    }

    public void gera(String caminho, String pacote){
        
        GravadorCodigo out = null;
        try{
            ClasseJava classe = new ClasseJava(pacote, "DadosLexico");
            out = new GravadorCodigo(caminho + "/" +  classe.getCaminho());
            
            out.novaLinha();
            if(classe.getPacote().length() > 0){
                out.escreve("package " + classe.getPacote() + ";");
            }
            out.novaLinha();
            out.novaLinha();
            out.escreve("import sintelo.reconhecedor.lexico.EspecificacaoLexica;");
            out.escreve("import java.util.Arrays;");
            out.novaLinha();
            out.novaLinha();
            out.escreve("public class " + classe.getNome() + " implements EspecificacaoLexica {");
            out.aumentaNivel();
            out.novaLinha();
            out.novaLinha();


            int[][] tabela = gerador.getTabelaLexico();

            out.escreve("private static int[][] TABELA_TRANSICAO = new int[" + tabela.length + "][256];");

            out.novaLinha();

            
            int atribuicoes = 0;
            int metodos = 0;

            for (int i = 0; i < tabela.length; ++i) {
                int[] linha = tabela[i];
                for (int j = 0; j < linha.length; ++j) {
                    int v = linha[j];
                    if (v != -1) {

                        if(atribuicoes == 0){
                            if(metodos > 0){
                                out.diminuiNivel();
                                out.escreve("}");
                                out.novaLinha();
                            }
                            out.escreve("private static void init" + metodos + "(){");
                            out.aumentaNivel();
                            metodos++;
                        }

                        out.escreve("TABELA_TRANSICAO[" + i + "][" + j + "] = " + v + ";");
                        atribuicoes++;
                        if (atribuicoes == 500) {
                            atribuicoes = 0;
                        }
                    }
                }
            }
            if(atribuicoes > 0){
                out.diminuiNivel();
                out.escreve("}");
                out.novaLinha();
            }


            out.escreve("static {");
            out.aumentaNivel();

            out.escreve("for(int i = 0; i < TABELA_TRANSICAO.length; ++i){");
            out.aumentaNivel();
            out.escreve("Arrays.fill(TABELA_TRANSICAO[i], -1);");
            out.diminuiNivel();
            out.escreve("}");

            for(int i = 0; i < metodos; ++i){
                out.escreve("init" + i + "();");
            }

            out.diminuiNivel();
            out.escreve("}");            
            out.novaLinha();
            out.novaLinha();


            int[] tokensReconhecidos = gerador.getTabelaTransicao().getReconhecedores();

            out.escreve("private static final int[] TOKENS_RECONHECIDOS = {");
            out.aumentaNivel();
            out.identa();
            for(int i = 0; i < tokensReconhecidos.length; ++i){
                if(i != 0){
                    out.insere(", ");
                }
                out.insere(String.valueOf(tokensReconhecidos[i]));
            }
            out.novaLinha();
            out.diminuiNivel();
            out.escreve("};");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getDestino(int e, char c) {");
            out.aumentaNivel();
            out.escreve("return TABELA_TRANSICAO[e][c];");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getTokenReconhecido(int e) {");
            out.aumentaNivel();
            out.escreve("return TOKENS_RECONHECIDOS[e];");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getEstadoInicial() {");
            out.aumentaNivel();
            int inicial = gerador.getTabelaTransicao().getInicial();
            out.escreve("return " + String.valueOf(inicial) + ";");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public int getTokenIgnorado() {");
            out.aumentaNivel();
            int tokenIgnorado = gerador.getTokenIgnorado();
            out.escreve("return " + String.valueOf(tokenIgnorado) + ";");
            out.diminuiNivel();
            out.escreve("}");
            
            out.novaLinha();
            out.novaLinha();

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
