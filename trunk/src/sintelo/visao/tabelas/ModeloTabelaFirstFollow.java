/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.tabelas;

import javax.swing.table.DefaultTableModel;
import sintelo.glc.Gramatica;
import sintelo.glc.NaoTerminal;
import sintelo.glc.TabelaParse;

/**
 *
 * @author phireis
 */
public class ModeloTabelaFirstFollow extends DefaultTableModel{
    
    private Gramatica gramatica;

    public ModeloTabelaFirstFollow(Gramatica gramatica) {
        super(gramatica.getNaoTerminals().size(), 2);
        this.gramatica = gramatica;
        
        inicializa();
    }
    

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }



    private void inicializa() {
        super.setColumnIdentifiers(new String[]{"First", "Follow"});
        int i = 0;
        for(NaoTerminal nt : gramatica.getNaoTerminals()){
            super.setValueAt(nt.getFirst(), i, 0);
            super.setValueAt(nt.getFollow(), i, 1);
            ++i;
        }
    }
    
    

}
