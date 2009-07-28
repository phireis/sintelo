/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.lexico.fabrica;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import sintelo.lexico.Automato;
import sintelo.lexico.AutomatoException;
import sintelo.lexico.Estado;

/**
 *
 * @author phireis
 */
public class FabricaAutomato {

    private Automato automato;
    private LinkedHashMap<String, Integer> mapaTokens;
    private Stack<Escopo> pilha;
    private Conjunto conjunto;

    private int tokenIgnorado;
    
    private int seqToken;

    public Automato getAutomato() {
        return automato;
    }

    private class Escopo{
        Estado inicial;
        Estado anterior;
        Estado atual;
        Estado efinal;

        void avanca(Estado proximo){
            anterior = atual;
            atual = proximo;
        }

        void fecha(){
            atual.novaTransicaoVazia(efinal);
        }

        void zeroMais(){
            atual.novaTransicaoVazia(anterior);
            anterior.novaTransicaoVazia(atual);
        }

        void umMais(){
            atual.novaTransicaoVazia(anterior);
        }

        void umZero(){
            anterior.novaTransicaoVazia(atual);
        }
    }

    private class Conjunto implements Iterable<Character>{
        Set<Character> conjunto;
        boolean negativo;
        Character ultimo;
        Character proximo;

        Conjunto() {
            conjunto = new LinkedHashSet<Character>();
            negativo = false;
        }

        boolean add(Character c){
            ultimo = c;
            return conjunto.add(c);
        }

        void addFaixa(Character ch){
            for(char c = (char)(ultimo + 1); c <= ch; ++c){
                conjunto.add(c);
            }
            ultimo = null;
        }

        boolean tem(char c){
            return conjunto.contains(c);
        }

        public Iterator<Character> iterator() {
            return conjunto.iterator();
        }

    }


    public FabricaAutomato() {
        automato = new Automato();
        mapaTokens = new LinkedHashMap<String, Integer>();
        pilha = new Stack<Escopo>();
        seqToken = 0;

        mapaTokens.put("$", seqToken++);

        Escopo raiz = new Escopo();
        raiz.inicial = raiz.atual = automato.novoEstado();
        automato.setEstadoInicial(raiz.inicial);
        pilha.push(raiz);

        tokenIgnorado = -2;
        
    }

    private Escopo novoEscopo(){
        Escopo esc = pilha.peek();
        Escopo novo = new Escopo();

        novo.atual = novo.inicial = esc.atual;
        novo.efinal = automato.novoEstado();
        pilha.push(novo);

        novaCadeia();

        return novo;
    }
    
    private void novaCadeia(){
        Escopo esc = pilha.peek();

        Estado novo = automato.novoEstado();
        automato.novaTransicaoVazia(esc.inicial, novo);
        esc.avanca(novo);
    }
    

    public void novoReconhecedor(String nome){
        Integer numero = mapaTokens.get(nome);
        if(numero != null){
            throw new AutomatoException("Definição duplicada de token:" + nome);
        }        
        Escopo esc = novoEscopo();
        numero = seqToken++;
        esc.efinal.setReconhecedor(numero);
        mapaTokens.put(nome, numero);
    }

    public void fechaReconhecedor(){
        Escopo esc = pilha.pop();
        esc.fecha();
    }

    public void novoIgnorado(){
        if(tokenIgnorado == -2){
            tokenIgnorado = seqToken++;
            automato.setTokenIgnorado(tokenIgnorado);
            mapaTokens.put("", tokenIgnorado);
        }        
        Escopo esc = novoEscopo();
        esc.efinal.setReconhecedor(tokenIgnorado);
    }
    
    public void novoCaracter(char c){
        Escopo esc = pilha.peek();
        Estado novo = automato.novoEstado();
        automato.novaTransicao(esc.atual, c, novo);
        esc.avanca(novo);
    }
    

    public void novaAlternativa(){
        Escopo grupo = pilha.peek();

        grupo.fecha();
        novaCadeia();
    }

    public void abreGrupo(){
        novoEscopo();
    }
    
    public void fechaGrupo(){
        Escopo grupo = pilha.pop();
        Escopo esc = pilha.peek();
        grupo.fecha();
        esc.avanca(grupo.efinal);
    }

    public void novoConjunto(){
        conjunto = new Conjunto();
    }

    public void negaConjunto(){
        conjunto.negativo = true;
    }

    public void setProximo(char c){
        conjunto.proximo = c;
    }

    public void adicionaElemento(){
        conjunto.add(conjunto.proximo);
    }

    public void adicionaFaixa(){
        conjunto.addFaixa(conjunto.proximo);
    }

    public void fechaConjunto(){
        Escopo esc = pilha.peek();
        Estado novo = automato.novoEstado();
        if(conjunto.negativo){
            for(int i = 0; i < 255; ++i){
                char c = (char)i;
                if(!conjunto.tem(c)){
                    automato.novaTransicao(esc.atual, c, novo);
                }
            }
        }
        else{
            for(Character c : conjunto){
                automato.novaTransicao(esc.atual, c, novo);
            }
        }
        esc.avanca(novo);
    }
    
    public void zeroMais(){
        Escopo esc = pilha.peek();
        esc.zeroMais();
    }
    
    public void umMais(){
        Escopo esc = pilha.peek();
        esc.umMais();
    }
    
    public void umZero(){
        Escopo esc = pilha.peek();
        esc.umZero();
    }

    public void informaTokens(){
        automato.setTokens(mapaTokens);
    }



}
