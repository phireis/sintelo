/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.metaparsers.sintatico;

import sintelo.glc.Gramatica;
import sintelo.glc.ebnf.FabricaGramatica;
import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class SemanticoSintatico implements Semantico{
    
    
    private FabricaGramatica fabrica;

    public SemanticoSintatico() {
        fabrica = new FabricaGramatica();
    }



    public void executa(int acao, Token token) {

        switch(acao){
            case 0:{
                String nome = getNaoTerminal(token.getTexto());
                fabrica.novaRegra(nome);
                break;
            }
            case 1:{
                fabrica.novaAlternativa();
                break;
            }
            case 2:{
                fabrica.fechaRegra();
                break;
            }
            case 3:{
                fabrica.addTerminal(token.getTexto());
                break;
            }
            case 4:{
                String nome = getNaoTerminal(token.getTexto());
                fabrica.addNaoTerminal(nome);
                break;
            }
            case 5:{
                int a = getAcaoSemantica(token.getTexto());
                fabrica.novaAcao(a);
                break;
            }
            case 6:{
                fabrica.empilha();
                break;
            }
            case 7:{
                fabrica.novoGrupo();
                break;
            }
            case 8:{
                fabrica.novoOpcional();
                break;
            }
            case 9:{
                fabrica.elevaZeroMais();
                break;
            }
            case 10:{
                fabrica.elevaUmMais();
                break;
            }
        }
    }
    
    public Gramatica getGramatica(){
        return fabrica.getGramatica();
    }



    private String getNaoTerminal(String nome){
        return nome.substring(1, nome.length()-1);
    }

    private int getAcaoSemantica(String id){
        return Integer.parseInt(id.substring(1));
    }

}
