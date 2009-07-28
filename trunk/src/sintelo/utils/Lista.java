/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.utils;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author phireis
 */
public class Lista<T> implements Iterable<T>{

    private class No{
        public No anterior;
        public T elemento;
        public No proximo;
    }

    public class Iterador implements Iterator<T>{

        private No atual;

        public Iterador(No atual) {
            this.atual = atual;
        }

        public boolean hasNext() {
            return atual.proximo != ultimo;
        }

        public T next() {
            atual = atual.proximo;
            return atual.elemento;
        }

        public void remove() {
            atual.anterior.proximo = atual.proximo;
            atual.proximo.anterior = atual.anterior;
            atual = atual.anterior;
            --sz;
        }

    }

    private No primeiro;
    private No ultimo;
    private int sz;

    public Lista() {
        primeiro = new No();
        ultimo = new No();
        sz = 0;

        primeiro.proximo = ultimo;
        ultimo.anterior = primeiro;
    }

    public Lista(Iterable<T> col){
        this();
        for(T t : col){
            add(t);
        }
    }

    public void add(T elemento){
        No novo = new No();
        novo.anterior = ultimo.anterior;
        novo.elemento = elemento;
        novo.proximo = ultimo;
        novo.anterior.proximo = novo;
        novo.proximo.anterior = novo;
        ++sz;
    }

    public int size(){
        return sz;
    }

    
    public Iterator<T> iterator() {
        return new Iterador(primeiro);
    }

}
