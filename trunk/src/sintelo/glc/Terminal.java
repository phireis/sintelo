
package sintelo.glc;

import sintelo.glc.conjuntos.Alcancaveis;
import sintelo.glc.conjuntos.First;


public class Terminal implements Simbolo{

    private int codigo;
    private String nome;
    private First first;

    private static Terminal epsilon = new Terminal(0, "î");
    private static Terminal dollar = new Terminal(1, "$");

    public Terminal(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public Terminal(int codigo) {
        this(codigo, "");
    }




    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    public First getFirst() {
        if(first == null){
            first = new First();
            first.add(this);
        }
        return first;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Terminal other = (Terminal) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return codigo;
    }

    @Override
    public String toString() {
        return nome;
    }


    public static Terminal getEpsilon(){
        return epsilon;
    }

    public static Terminal getDollar() {
        return dollar;
    }

    public boolean isGerador() {
        return true;
    }

    public void calculaAlcancaveis(Alcancaveis a) {
        a.add(this);
    }





}
