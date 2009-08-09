/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.gerador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author phireis
 */
public class GravadorCodigo {

    private int nivel;
    private String arquivo;
    private String ident;
    private FileWriter writer;

    public GravadorCodigo(String arquivo, String ident) {
        this.arquivo = arquivo;
        this.ident = ident;
        nivel = 0;
        try {
            File file = new File(arquivo);
            file.getParentFile().mkdirs();
            writer = new FileWriter(file);
        } catch (IOException ex) {
            throw new GeradorException("Não foi possível criar arquivo", ex);
        }
    }

    public GravadorCodigo(String arquivo) {
        this(arquivo, "    ");
    }
    
    
    
    public void fecha(){
        try {
            writer.close();
        } catch (IOException ex) {
            throw new GeradorException(ex);
        }
    }

    public void insere(String linha){
        try {
            writer.write(linha);
        } catch (IOException ex) {
            throw new GeradorException("Erro ao gravar no arquivo", ex);
        }
    }

    public void escreve(String linha){
        identa();
        insere(linha);
        novaLinha();
    }

    public void identa(){
        for(int i = 0; i < nivel; ++i){
            insere(ident);
        }
    }

    public void novaLinha(){
        insere("\r\n");
    }

    public int aumentaNivel(){
        return ++nivel;
    }

    public int diminuiNivel(){
        return --nivel;
    }

}
