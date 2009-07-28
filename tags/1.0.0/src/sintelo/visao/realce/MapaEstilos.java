/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.realce;

import java.util.HashMap;
import java.util.Map;
import javax.swing.text.SimpleAttributeSet;

/**
 *
 * @author phireis
 */
public class MapaEstilos {

    private Map<Integer, SimpleAttributeSet> estilos;
    private SimpleAttributeSet estiloPadrao;

    public MapaEstilos() {
        estilos = new HashMap<Integer, SimpleAttributeSet>();
        estiloPadrao = new SimpleAttributeSet();
    }

    public SimpleAttributeSet getEstiloPadrao() {
        return estiloPadrao;
    }

    public void setEstiloPadrao(SimpleAttributeSet estiloPadrao) {
        this.estiloPadrao = estiloPadrao;
    }

    public void registra(int n, SimpleAttributeSet estilo){
        estilos.put(n, estilo);
    }

    public void remove(int n){
        estilos.remove(n);
    }

    public SimpleAttributeSet getEstilo(int n){
        SimpleAttributeSet estilo = estilos.get(n);
        if(estilo == null){
            return estiloPadrao;
        }
        return estilo;        
    }

}
