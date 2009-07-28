/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.tabelas;

import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import sintelo.lexico.TabelaTransicao;

/**
 *
 * @author phireis
 */
public class ModeloTabelaTransicao extends DefaultTableModel{

    private TabelaTransicao tabela;

    public ModeloTabelaTransicao(TabelaTransicao tabela) {
        super(new Object[][]{}, tabela.getColunas());
        //super(tabela.getQtdLinhas(), tabela.getQtdColunas());
        this.tabela = tabela;

        inicializa();
    }
    
    private void inicializa(){

        for(int i = 0; i < tabela.getQtdLinhas(); ++i){
            int[] linha = tabela.getTabela()[i];
            String[] lin = new String[linha.length];
            for(int j = 0; j < linha.length; ++j){
                int val = linha[j];
                if(val != -1){
                    lin[j] = "q" + val;
                }
                else{
                    lin[j] = "";
                }
            }
            super.addRow(lin);            
        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }





}
