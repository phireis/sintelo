

package sintelo.glc.conjuntos;

import sintelo.glc.*;



public class First extends ConjuntoTerminal{

    public First() {
    }


    public boolean temCadeiaVazia(){
        return terminais.contains(Terminal.getEpsilon());
    }

    @Override
    public First clone() throws CloneNotSupportedException {
        First f = new First();
        f.add(this);
        return f;
    }

    public First cloneSemVazio(){
        First f = new First();
        f.add(this);
        f.terminais.remove(Terminal.getEpsilon());
        return f;
    }




}
