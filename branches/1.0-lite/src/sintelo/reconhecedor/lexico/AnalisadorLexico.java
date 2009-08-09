/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.reconhecedor.lexico;

import sintelo.reconhecedor.ErroLexico;
import sintelo.reconhecedor.Lexico;
import sintelo.reconhecedor.Token;

/**
 *
 * @author phireis
 */
public class AnalisadorLexico implements Lexico{

    public static final int TOKEN_NAO_RECONHECIDO = Integer.MIN_VALUE;
    public static final int DOLLAR = 0;

    private EspecificacaoLexica especificacao;

    private String entrada;
    private int posicao;

    private Token ultimo;

    public AnalisadorLexico(EspecificacaoLexica especificacao) {
        this.especificacao = especificacao;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
        this.posicao = 0;
    }

    public EspecificacaoLexica getEspecificacao() {
        return especificacao;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao){ 
        this.posicao = posicao;
    }


    private Token avanca(){
        int fim = entrada.length();

        if(posicao >= fim){
            return null;
        }

        int inicio = posicao;
        
        int atual = especificacao.getEstadoInicial();

        int ultimoEstado = -1;
        int ultimaPosicao = -1;

        while(posicao != fim){
            char c = entrada.charAt(posicao++);
            if(c > 255){
                throw new ErroLexico(posicao, "Caractere inválido: " + c);
            }
            atual = especificacao.getDestino(atual, c);

            if(atual < 0){
                break;
            }
            else{
                int token = especificacao.getTokenReconhecido(atual);
                if(token != TOKEN_NAO_RECONHECIDO){
                    ultimoEstado = atual;
                    ultimaPosicao = posicao;
                }
            }
        }
        if(ultimoEstado == -1){
            String s = entrada.substring(inicio, posicao);
            throw new ErroLexico(inicio, "'" + s + "' inválido");
        }
        posicao = ultimaPosicao;


        int numero = especificacao.getTokenReconhecido(ultimoEstado);
        String texto = entrada.substring(inicio, ultimaPosicao);
        return new Token(numero, texto, inicio);

    }

    public synchronized boolean temProximo() {
        if(ultimo != null){
            return true;
        }
        Token token;
        do{
            token = avanca();
            if(token == null){
                return false;
            }
        }while(token.getNumero() == especificacao.getTokenIgnorado());
        ultimo = token;
        return true;
    }

    public Token proximo() {
        if(temProximo()){
            Token token = ultimo;
            ultimo = null;
            return token;
        }
        return new Token(DOLLAR, "", posicao);
    }

}
