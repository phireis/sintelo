/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.lexico;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author phireis
 */
public class ClasseEquivalencia implements Iterable<Estado>{

    private Set<Estado> estados;

    public ClasseEquivalencia() {
        estados = new LinkedHashSet<Estado>();
    }

    public Set<Estado> getEstados() {
        return Collections.unmodifiableSet(estados);
    }

    public boolean add(Estado e){
        return estados.add(e);
    }

    public Iterator<Estado> iterator() {
        return getEstados().iterator();
    }





}
