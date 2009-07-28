/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor;

/**
 *
 * @author phireis
 */
public class Token {

    private int numero;
    private String texto;
    private int posicao;

    public Token(int numero, String texto, int posicao) {
        this.numero = numero;
        this.texto = texto;
        this.posicao = posicao;
    }

    public Token() {
    }
    
    

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return numero + ": " + texto + " (" + posicao + ")";
    }




}
