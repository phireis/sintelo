/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.utils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author phireis
 */
public class LeituraArquivo {
    
    public static String leArquivo(String a){
        FileInputStream f = null;
        try{
            StringBuilder s = new StringBuilder();
            f = new FileInputStream(a);
            int c = f.read();

            while(c != -1){
                char ch = (char)c;
                s.append(ch);

                c = f.read();
            }

            return s.toString();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(f != null) {
                try {
                    f.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "";
    }

}
