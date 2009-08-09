/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintelo.visao.realce;

import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 *
 * @author phireis
 */
public class DocumentoRealce extends DefaultStyledDocument {

    private Realce realce;

    public DocumentoRealce(Realce realce) {
        this.realce = realce;
    }

    

    public Realce getRealce() {
        return realce;
    }

    public void setRealce(Realce realce) {
        this.realce = realce;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        int inicio = offset;
        int fim = inicio + str.length();
        int len = getLength();
        String texto = getText(0, len);
        for (inicio--; inicio >= 0 && texto.charAt(inicio) != '\n'; inicio--){

        }
        inicio++;
        while(fim < len && texto.charAt(fim) != '\n'){
            ++fim;
        }
        atualiza(texto, inicio, fim);
    }

    @Override
    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        int inicio = offset;
        int fim = inicio;
        length = getLength();
        String texto = getText(0, length);
        for (inicio--; inicio >= 0 && texto.charAt(inicio) != '\n'; inicio--){

        }
        ++inicio;
        while(fim < length && texto.charAt(fim) != '\n'){
            ++fim;
        }           
        
        atualiza(texto, inicio, fim);
    }

    private void atualiza(String texto,int inicio, int fim) throws BadLocationException {
        UndoableEditListener[] listeners = getUndoableEditListeners();
        for (UndoableEditListener listener : listeners){
            removeUndoableEditListener(listener);
        }

        realce.aplica(this, inicio, fim, texto);

        for (UndoableEditListener listener : listeners){
            addUndoableEditListener(listener);
        }

    }
}
