/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador.java;

import sintelo.gerador.GeradorException;

/**
 *
 * @author phireis
 */
public class ClasseJava {

    private String pacote;
    private String nome;
    private String caminho;

    public ClasseJava(String pacote, String nome) {
        this.pacote = pacote;
        this.nome = nome;
        
        if(!validaPacote(pacote)){
            throw new GeradorException("Pacote invalido!");
        }
        
        caminho = pacote.replace('.', '/') + '/' + nome + ".java";
    }

    public static boolean validaPacote(String pacote){
        pacote = pacote.trim();
        if(pacote.length() == 0){
            return true;
        }
        char inicio = pacote.charAt(0);
        char fim = pacote.charAt(pacote.length() - 1);

        if(!Character.isLetter(inicio) && inicio != '_'){
            return false;
        }
        for(int i = 1; i < pacote.length(); ++i){
            char c = pacote.charAt(i);
            if(!Character.isLetterOrDigit(c) && c != '_' && c != '.'){
                return false;
            }
        }
        if(fim == '.'){
            return false;
        }
        return true;

    }

    public String getCaminho() {
        return caminho;
    }

    public String getNome() {
        return nome;
    }

    public String getPacote() {
        return pacote;
    }
    
    


}
