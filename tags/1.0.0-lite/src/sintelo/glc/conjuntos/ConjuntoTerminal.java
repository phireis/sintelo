

package sintelo.glc.conjuntos;

import sintelo.glc.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



public class ConjuntoTerminal implements Iterable<Terminal>{

    protected Set<Terminal> terminais;

    public ConjuntoTerminal() {
        terminais = new HashSet<Terminal>();
    }

    public boolean add(Terminal t){
        return terminais.add(t);
    }
    

    public boolean add(Iterable<Terminal> ts){
        boolean mudou = false;
        for(Terminal t : ts){
            if(add(t)){
                mudou = true;
            }
        }
        return mudou;
    }

    public Iterator<Terminal> iterator(){
        return terminais.iterator();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append('{');
        Iterator<Terminal> it = iterator();
        if(it.hasNext()){
            Terminal t = it.next();
            s.append(t.toString());
            while(it.hasNext()){
                s.append(',');
                t = it.next();
                s.append(t.toString());
            }
        }
        s.append('}');
        return s.toString();
    }




}
