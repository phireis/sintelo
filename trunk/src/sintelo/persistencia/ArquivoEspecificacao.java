/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phireis
 */
public class ArquivoEspecificacao {
    
    public static void grava(File arquivo, Especificacoes esp){
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(arquivo);
            ObjectOutputStream obout = new ObjectOutputStream(file);
            obout.writeObject(esp);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }finally{            
            try {
                if(file != null) file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void grava(String arquivo, Especificacoes esp){
        grava(new File(arquivo), esp);
    }
    
    public static Especificacoes le(String arquivo){
        return le(new File(arquivo));
    }
    
    public static Especificacoes le(File arquivo){
        FileInputStream file = null;
        try {
            file = new FileInputStream(arquivo);
            ObjectInputStream obin = new ObjectInputStream(file);
            Object obj = obin.readObject();
            return (Especificacoes)obj;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }finally{            
            try {
                if(file != null) file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
