/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao;

import java.io.File;
import sintelo.persistencia.ArquivoEspecificacao;
import sintelo.persistencia.Especificacoes;

/**
 *
 * @author phireis
 */
public class Documento {

    private File arquivo;
    private boolean alterado;

    public Documento() {
        setNovo();
        setAlterado(false);
    }

    public boolean isAlterado() {
        return alterado;
    }

    public void setAlterado(boolean alterado) {
        this.alterado = alterado;
    }

    public void setAlterado(){
        setAlterado(true);
    }

    public boolean isNovo(){
        return arquivo == null;
    }

    public void setNovo(){
        arquivo = null;
    }

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    public void salva(Especificacoes esp){
        ArquivoEspecificacao.grava(arquivo, esp);
        setAlterado(false);
    }

    public Especificacoes abre(){
        Especificacoes esp = ArquivoEspecificacao.le(arquivo);
        setAlterado(false);
        return esp;
    }

}
