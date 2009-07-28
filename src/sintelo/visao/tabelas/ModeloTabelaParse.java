/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.tabelas;

import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import sintelo.glc.TabelaParse;

/**
 *
 * @author phireis
 */
public class ModeloTabelaParse extends DefaultTableModel{
    
    private TabelaParse tabela;

    public ModeloTabelaParse(TabelaParse tabela) {
        super(tabela.getQtdLinhas(), tabela.getQtdColunas());
        this.tabela = tabela;
        
        inicializa();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }



    private void inicializa() {
        super.setColumnIdentifiers(tabela.getColunas());
        for(int i = 0; i < tabela.getQtdLinhas(); ++i){
            for(int j = 0; j < tabela.getQtdColunas(); ++j){
                int val = tabela.getValor(i, j);
                if(val == -1){
                    super.setValueAt("", i, j);
                }
                else{
                    TabelaParse.Conflito conflito = tabela.getConflito(i, j);
                    if(conflito == null){
                        super.setValueAt(val+1, i, j);
                    }
                    else{
                        StringBuilder sb = new StringBuilder();
                        Iterator<Integer> it = conflito.getValores().keySet().iterator();
                        if(it.hasNext()){
                            sb.append(it.next() + 1);
                            while(it.hasNext()){
                                sb.append(", ");
                                sb.append(it.next() + 1);
                            }
                        }
                        super.setValueAt(sb.toString(), i, j);
                    }
                }
            }
        }
    }
    
    

}
