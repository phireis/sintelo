
package sintelo.glc;


public class AcaoSemantica{

    private int numero;
    private int posicao;

    public AcaoSemantica(int numero, int posicao) {
        this.numero = numero;
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }

    

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "#" + numero;
    }


}
