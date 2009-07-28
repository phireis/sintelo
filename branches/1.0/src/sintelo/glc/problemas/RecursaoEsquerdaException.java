

package sintelo.glc.problemas;

import sintelo.glc.*;
import java.util.Iterator;
import java.util.Stack;



public class RecursaoEsquerdaException extends GlcException{


    private Stack<Regra> pilha = new Stack<Regra>();
    private NaoTerminal naoTerminal;
    private boolean pronta = false;


    public RecursaoEsquerdaException(NaoTerminal naoTerminal) {
        this.naoTerminal = naoTerminal;
    }

    public Iterator<Regra> iterator(){
        return pilha.iterator();
    }

    public void adiciona(Regra r){
        pilha.push(r);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recursão a esquerda com o não-terminal " + naoTerminal.toString() + ":");
        sb.append('\n');
        Iterator<Regra> it = this.iterator();
        while(it.hasNext()){
            Regra r = it.next();
            sb.append(r);
            sb.append('\n');
        }
        return sb.toString();
    }

    public boolean isPronta() {
        return pronta;
    }

    public void setPronta(boolean pronta) {
        this.pronta = pronta;
    }

    public NaoTerminal getNaoTerminal() {
        return naoTerminal;
    }


    


}
