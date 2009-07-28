/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.realce;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import sintelo.reconhecedor.ErroLexico;
import sintelo.reconhecedor.Token;
import sintelo.reconhecedor.lexico.AnalisadorLexico;
import sintelo.reconhecedor.lexico.EspecificacaoLexica;

/**
 *
 * @author phireis
 */
public class Realce {

    private MapaEstilos estilos;
    private EspecificacaoLexica lexica;

    public Realce(MapaEstilos estilos, EspecificacaoLexica lexica) {
        this.estilos = estilos;
        this.lexica = lexica;
    }

    public void aplica(StyledDocument doc, int inicio, int fim, String texto){
        
        AnalisadorLexico lexico = new AnalisadorLexico(lexica);
        String entrada = null;

        entrada = texto.substring(inicio, fim);
        lexico.setEntrada(entrada);
        do{
            int comeco = 0;
            int len;
            try{
                comeco = inicio + lexico.getPosicao();
                if(!lexico.temProximo()){
                    break;
                }
                Token t = lexico.proximo();
                SimpleAttributeSet estilo = estilos.getEstilo(t.getNumero());
                comeco = inicio + t.getPosicao();
                len = t.getTexto().length();

                doc.setCharacterAttributes(comeco, len, estilo, true);
            }catch(ErroLexico e){
                len = e.getPosicao() + inicio - comeco + 1;
                SimpleAttributeSet estilo = estilos.getEstiloPadrao();
                doc.setCharacterAttributes(comeco, len, estilo, true);
            }

        }while(true);

    }
    
    public void aplica(JTextPane textPane){
        StyledDocument doc = textPane.getStyledDocument();
        AnalisadorLexico lexico = new AnalisadorLexico(lexica);
        String entrada = null;
        try {
            entrada = doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        lexico.setEntrada(entrada);
        do{
            int comeco = 0;
            int len;
            try{
                comeco = lexico.getPosicao();
                if(!lexico.temProximo()){
                    break;
                }
                Token t = lexico.proximo();
                SimpleAttributeSet estilo = estilos.getEstilo(t.getNumero());
                comeco = t.getPosicao();
                len = t.getTexto().length();
                doc.setCharacterAttributes(comeco, len, estilo, true);
            }catch(ErroLexico e){
                len = e.getPosicao() - comeco + 1;
                SimpleAttributeSet estilo = estilos.getEstiloPadrao();
                doc.setCharacterAttributes(comeco, len, estilo, true);
            }

        }while(true);
    }
    
    public void instala(final JTextPane textPane){
        DocumentoRealce doc = new DocumentoRealce(this);
        textPane.setStyledDocument(doc);
    }
    
        
}
