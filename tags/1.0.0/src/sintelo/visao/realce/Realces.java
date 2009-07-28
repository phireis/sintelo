/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.realce;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import sintelo.metaparsers.sintatico.DadosLexico;

/**
 *
 * @author phireis
 */
public class Realces {
    
    private SimpleAttributeSet normal;
    private SimpleAttributeSet azulNegrito;
    private SimpleAttributeSet verdeNegrito;
    private SimpleAttributeSet magentaNegrito;
    private SimpleAttributeSet vermelhoNegrito;
    private SimpleAttributeSet negrito;
    private SimpleAttributeSet italico;
    private SimpleAttributeSet verde;
        
    private Realce realceLexico;
    private Realce realceGramatica;

    private Realces() {
        criaEstilos();
        criaRealceLexico();
        criaRealceGramatica();
    }
    
    private static Realces realces = new Realces();

    public static Realces getInstance(){
        return realces;
    }
    
    public static Realce getRealceGramatica() {
        return getInstance().realceGramatica;
    }

    public static Realce getRealceLexico() {
        return getInstance().realceLexico;
    }

    



    private void criaRealceGramatica() {
        MapaEstilos mapa = new MapaEstilos();
        mapa.registra(2, negrito);
        mapa.registra(3, azulNegrito);
        mapa.registra(4, verdeNegrito);
        mapa.registra(6, vermelhoNegrito);
        realceGramatica = new Realce(mapa, new DadosLexico());
    }

    private void criaRealceLexico() {
        MapaEstilos mapa = new MapaEstilos();
        mapa.registra(1, verdeNegrito);
        mapa.registra(2, negrito);
        mapa.registra(3, verde);
        mapa.registra(4, azulNegrito);
        realceLexico = new Realce(mapa, new sintelo.metaparsers.lexico.DadosLexico());
    }
    
    private void criaEstilos(){
        
        normal = new SimpleAttributeSet();        
        
        SimpleAttributeSet estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, Color.BLUE);
        StyleConstants.setBold(estilo, true);
        azulNegrito = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, new Color(0, 150, 0));
        StyleConstants.setBold(estilo, true);
        verdeNegrito = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, Color.RED);
        StyleConstants.setBold(estilo, true);
        vermelhoNegrito = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, Color.MAGENTA);
        StyleConstants.setBold(estilo, true);
        magentaNegrito = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setBold(estilo, true);
        negrito = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setItalic(estilo, true);
        italico = estilo;
        
        estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, new Color(0, 150, 0));
        verde = estilo;
    }


}
