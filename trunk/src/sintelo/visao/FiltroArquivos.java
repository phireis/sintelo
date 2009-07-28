/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author phireis
 */
public class FiltroArquivos extends FileFilter{

    private String extensao;

    public FiltroArquivos(String extensao) {
        this.extensao = extensao;
    }

    public boolean accept(File file) {
        return file.isDirectory() || file.toString().endsWith(extensao);
    }

    @Override
    public String getDescription() {
        return "Arquivo de especificações léxicas e sintáticas (.sintelo)";
    }

}
