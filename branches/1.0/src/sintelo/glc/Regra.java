
package sintelo.glc;

import sintelo.glc.problemas.RecursaoEsquerdaException;
import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;
import java.util.List;


public class Regra {

    private int numero;
    private NaoTerminal naoTerminal;
    private Cadeia cadeia;

    public Regra(int numero, NaoTerminal naoTerminal) {
        this.numero = numero;
        this.naoTerminal = naoTerminal;
        cadeia = new Cadeia();
    }

    public void add(Simbolo s){
        cadeia.add(s);
    }

    public void addAcao(int numero){
        cadeia.addAcao(numero);
    }

    public Simbolo getUltimo(){
        return cadeia.getUltimo();
    }

    public Simbolo removeUltimo(){
        return cadeia.removeUltimo();
    }

    public int getNumero() {
        return numero;
    }

    public NaoTerminal getNaoTerminal() {
        return naoTerminal;
    }

    public List<Simbolo> getSimbolos(){
        return cadeia.getSimbolos();
    }

    public List<AcaoSemantica> getAcoes(){
        return cadeia.getAcoes();
    }


    public First getFirst(){
        try{
            return cadeia.getFirst();
        }catch(RecursaoEsquerdaException e){
            if(!e.isPronta()){
                e.adiciona(this);
            }
            throw e;
        }
    }


    @Override
    public String toString() {
        return naoTerminal + " ::=" + cadeia + ";";
    }

    public List toList(){
        return cadeia.toList();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Regra other = (Regra) obj;
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return numero;
    }

    public boolean isGerador() {
        return cadeia.isGerador();
    }

    public void verificaGerador(){
        cadeia.verificaGerador();
    }

    public void calculaAlcancaveis(Alcancaveis a){
        a.add(this);
        cadeia.calculaAlcancaveis(a);
    }


}
