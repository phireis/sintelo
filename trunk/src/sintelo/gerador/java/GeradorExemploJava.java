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
public class GeradorExemploJava {


    public GeradorExemploJava() {
    }

    public void gera(String caminho, String pacote){
        
        GravadorCodigo out = null;
        try{
            ClasseJava classe = new ClasseJava(pacote, "Exemplo");
            out = new GravadorCodigo(caminho + "/" +  classe.getCaminho());
            
            out.novaLinha();
            if(classe.getPacote().length() > 0){
                out.escreve("package " + classe.getPacote() + ";");
            }
            out.novaLinha();
            out.novaLinha();
            out.escreve("import sintelo.reconhecedor.*;");
            out.escreve("import sintelo.reconhecedor.lexico.*;");
            out.escreve("import sintelo.reconhecedor.sintatico.*;");
            out.novaLinha();
            out.novaLinha();
            out.escreve("public class " + classe.getNome() + "{ ");
            out.aumentaNivel();
            out.novaLinha();
            out.novaLinha();
            
            out.escreve("public static void main(String[] args) { ");
            out.aumentaNivel();

            out.escreve("AnalisadorLexico lexico = new AnalisadorLexico(new DadosLexico());");
            out.escreve("AnalisadorSintatico sintatico = new AnalisadorSintatico(new DadosSintatico());");
            out.escreve("Semantico semantico = null;");
            
            out.escreve("try { ");
            out.aumentaNivel();

            out.escreve("String entrada = args[0];");
            out.escreve("lexico.setEntrada(entrada);");
            out.escreve("sintatico.reconhece(lexico, semantico);");
            out.escreve("System.out.println(\"Análise realizada com sucesso!\");");
            
            out.diminuiNivel();
            out.escreve("} catch(ErroLexico el) { ");
            out.aumentaNivel();
            out.escreve("System.err.println(el.getMessage());");
            out.diminuiNivel();
            out.escreve("} catch(ErroSintatico es) { ");
            out.aumentaNivel();
            out.escreve("System.err.println(es.getMessage());");
            out.diminuiNivel();
            out.escreve("} catch(ErroSemantico es) { ");
            out.aumentaNivel();
            out.escreve("System.err.println(es.getMessage());");

            out.diminuiNivel();
            out.escreve("}");
            
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
