/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.visao.tabelas;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import sintelo.glc.TabelaParse;
import sintelo.lexico.TabelaTransicao;

/**
 *
 * @author phireis
 */
public class Tabelas { 

    public Tabelas() {
    }
    
    private static JTable criaCabecalho(TableModel headerData){
        JTable rowHeader = new JTable(headerData){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        LookAndFeel.installColorsAndFont
            (rowHeader, "TableHeader.background",
            "TableHeader.foreground", "TableHeader.font");


        //rowHeader.setIntercellSpacing(new Dimension(0, 0));
        Dimension d = rowHeader.getPreferredScrollableViewportSize();
        d.width = (int)(rowHeader.getPreferredSize().width * 1.5);
        rowHeader.setPreferredScrollableViewportSize(d);
        //rowHeader.setRowHeight(table.getRowHeight());
        rowHeader.setRowSelectionAllowed(false);

        return rowHeader;
    }

    public static JScrollPane criaTabelaTransicao(TabelaTransicao tabela){
        JScrollPane scroll = new JScrollPane();
        ModeloTabelaTransicao modelo = new ModeloTabelaTransicao(tabela);
        JTable table = new JTable(modelo);
        
        
        DefaultTableModel headerData = new DefaultTableModel(0, 1);

        for (int i = 0; i < tabela.getQtdLinhas(); i++){
            String s = "q" + i;
            if(tabela.getReconhecedores()[i] != Integer.MIN_VALUE){
                s += "(" + tabela.getReconhecedores()[i] + ")";
            }
            headerData.addRow(new Object[] {s} );
        }

        JTable rowHeader = criaCabecalho(headerData);
        
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        

        scroll.setRowHeaderView(rowHeader);
        scroll.setViewportView(table);
        return scroll;
    }
    
    public static JScrollPane criaTabelaParse(TabelaParse tabela){
        JScrollPane scroll = new JScrollPane();
        JTable table = new JTable(new ModeloTabelaParse(tabela));
        
        
        DefaultTableModel headerData = new DefaultTableModel(0, 1);

        for (int i = 0; i < tabela.getQtdLinhas(); i++){
            String s = tabela.getLinhas()[i];
            headerData.addRow(new Object[] {s} );
        }

        JTable rowHeader = criaCabecalho(headerData);
        
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        scroll.setRowHeaderView(rowHeader);
        scroll.setViewportView(table);
        return scroll;
    }
    
    public static JScrollPane criaTabelaFirstFollow(TabelaParse tabela){
        JScrollPane scroll = new JScrollPane();
        JTable table = new JTable(new ModeloTabelaFirstFollow(tabela.getGramatica()));
        
        
        DefaultTableModel headerData = new DefaultTableModel(0, 1);

        for (int i = 0; i < tabela.getQtdLinhas(); i++){
            String s = tabela.getLinhas()[i];
            headerData.addRow(new Object[] {s} );
        }

        JTable rowHeader = criaCabecalho(headerData);
        
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);

        scroll.setRowHeaderView(rowHeader);
        scroll.setViewportView(table);
        return scroll;
    }


}
