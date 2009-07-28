/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.metaparsers.lexico;

import sintelo.lexico.Automato;
import sintelo.lexico.fabrica.FabricaAutomato;
import sintelo.reconhecedor.Semantico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class SemanticoLexico implements Semantico{
    
    private FabricaAutomato fabrica;

    public SemanticoLexico() {
        fabrica = new FabricaAutomato();
    }

    public FabricaAutomato getFabrica() {
        return fabrica;
    }

    public Automato getAutomato(){
        return fabrica.getAutomato();
    }

    public void executa(int acao, Token token) {
        switch(acao){
            case 0:{
                fabrica.novoIgnorado();
                break;
            }
            case 1:{
                String s = extraiId(token.getTexto());
                fabrica.novoReconhecedor(s);
                break;
            }
            case 2:{
                fabrica.novaAlternativa();
                break;
            }
            case 3:{
                char c = token.getTexto().charAt(0);
                fabrica.novoCaracter(c);
                break;
            }
            case 4:{
                String s = processaString(token.getTexto());
                fabrica.abreGrupo();
                for(int i = 0; i < s.length(); ++i){
                    char c = s.charAt(i);
                    fabrica.novoCaracter(c);
                }
                fabrica.fechaGrupo();
                break;
            }
            case 5:{
                char c = getEscape(token.getTexto());
                fabrica.novoCaracter(c);
                break;
            }
            case 6:{
                fabrica.zeroMais();
                break;
            }
            case 7:{
                fabrica.umMais();
                break;
            }
            case 8:{
                fabrica.umZero();
                break;
            }
            case 9:{
                fabrica.abreGrupo();
                break;
            }
            case 10:{
                fabrica.fechaGrupo();
                break;
            }
            case 11:{
                String s = processaString(token.getTexto());
                fabrica.novoReconhecedor(token.getTexto());
                for(int i = 0; i < s.length(); ++i){
                    char c = s.charAt(i);
                    fabrica.novoCaracter(c);
                }
                break;
            }
            case 12:{
                fabrica.novoConjunto();
                break;
            }
            case 13:{
                fabrica.fechaConjunto();
                break;
            }
            case 14:{
                fabrica.negaConjunto();
                break;
            }
            case 15:{
                char c = token.getTexto().charAt(0);
                fabrica.setProximo(c);
                break;
            }
            case 16:{
                char c = getEscape(token.getTexto());
                fabrica.setProximo(c);
                break;
            }
            case 17:{
                fabrica.adicionaFaixa();
                break;
            }
            case 18:{
                fabrica.adicionaElemento();
                break;
            }
            case 19:{
                fabrica.fechaReconhecedor();
                break;
            }
            case 20:{
                fabrica.informaTokens();
                break;
            }

        }
    }
    
    private static String extraiId(String s){
        int pos = s.length() - 1;
        for(int i = 0; i < s.length(); ++i){
            char c = s.charAt(i);
            if(c == ' ' || c == '\t'){
                pos = i;
                break;
            }
        }
        return s.substring(0, pos);
    }

    private static String processaString(String s){
        s = s.substring(1, s.length()-1);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); ++i){
            char c = s.charAt(i);
            if(c == '\\'){
                ++i;
                SubStr sub = new SubStr(s, i);
                c = getEscape(sub);
                i = sub.fim - 1;                
            }
            sb.append(c);
        }

        return sb.toString();
    }

    private static char getEscape(String s){
        SubStr sub = new SubStr(s, 1);
        return getEscape(sub);
    }

    private static class SubStr{
        String s;
        int inicio;
        int fim;

        public SubStr(String s, int inicio) {
            this.s = s;
            this.inicio = inicio;
        }


    }
    
    private static char getEscape(SubStr rc){
        char c;
        char ch = rc.s.charAt(rc.inicio);
        rc.fim = rc.inicio + 1;
        switch(ch){
            case 'n':{
                c = '\n';
                break;
            }
            case 'r':{
                c = '\r';
                break;
            }
            case 's':{
                c = ' ';
                break;
            }
            case 't':{
                c = '\t';
                break;
            }
            default:{
                if(ch >= '0' && ch <= '9'){
                    rc.fim += 2;
                    int n = Integer.parseInt(rc.s.substring(rc.inicio, rc.fim));
                    c = (char)n;
                }
                else{
                    c = ch;
                }
            }

        }
        return c;
    }

}
