/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Sintelo.java
 *
 * Created on 27/08/2008, 22:23:05
 */

package sintelo.visao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import sintelo.gerador.GeradorReconhecedores;
import sintelo.glc.Gramatica;
import sintelo.glc.TabelaParse;
import sintelo.glc.Terminal;
import sintelo.lexico.Automato;
import sintelo.lexico.TabelaTransicao;
import sintelo.metaparsers.lexico.CompiladorLexico;
import sintelo.metaparsers.sintatico.CompiladorSintatico;
import sintelo.persistencia.Especificacoes;
import sintelo.simulador.FabricaGerador;
import sintelo.visao.realce.Realce;
import sintelo.visao.realce.Realces;
import sintelo.visao.tabelas.Tabelas;

/**
 *
 * @author Kieling
 */
public class Sintelo extends javax.swing.JFrame {
    
    private Realce realceGramatica;
    private Realce realceLexico;
    private Documento documento;
    private JFileChooser selecionadorArquivo;

    /** Creates new form Sintelo */
    public Sintelo() {

        documento = new Documento();

        initComponents();
        realceGramatica = Realces.getRealceGramatica();
        realceLexico = Realces.getRealceLexico();

        realceLexico.instala(txtLexico);
        realceGramatica.instala(txtSintatico);

        selecionadorArquivo = new JFileChooser();
        selecionadorArquivo.setFileFilter(new FiltroArquivos(".sintelo"));
        selecionadorArquivo.setCurrentDirectory(new File("."));

        UndoableEditListener monitorAlteracao = new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent arg0) {
                documento.setAlterado();
            }
        };
        
        txtLexico.getDocument().addUndoableEditListener(monitorAlteracao);
        txtSintatico.getDocument().addUndoableEditListener(monitorAlteracao);
    }

    private void abreManual() {
        TelaAjuda tela = new TelaAjuda("ajuda/Indice.html");
        tela.setTitle("Manual do Sintelo");
        tela.setVisible(true);
    }
    
    private void abreAjuda() {
        TelaAjuda tela = new TelaAjuda("ajuda/indice_revisao teoriaca.html");
        tela.setTitle("Ajuda");
        tela.setVisible(true);
    }
    

    private void alertaNaoUtilizados(GeradorReconhecedores gerador){
        txtMensagens.setText("");
        StringBuilder ss = new StringBuilder();
        for(String s : gerador.getTokensNaoUtilizados()){
            ss.append("Token ")
                    .append(s)
                    .append(" não utilizado na gramática!")
                    .append('\n');
        }
        txtMensagens.setText(ss.toString());
    }

    private void adicionaTokensNaoDeclarados(){
        try{
            CompiladorLexico clex = new CompiladorLexico();
            CompiladorSintatico csin = new CompiladorSintatico();
            Automato aut = clex.compila(txtLexico.getText().trim());
            Gramatica gram = csin.compila(txtSintatico.getText());

            Set<Terminal> terminais = new LinkedHashSet<Terminal>(gram.getTerminais());
            terminais.remove(Terminal.getDollar());
            terminais.remove(Terminal.getEpsilon());

            Set<String> novos = new LinkedHashSet<String>();
            Map<String, Integer> tokens = aut.getTokens();

            for(Terminal t : terminais){
                String s = t.toString();
                if(!tokens.containsKey(s)){
                    novos.add(s);
                }
            }

            String elex = txtLexico.getText();
            StringBuilder novaesp = new StringBuilder();
            novaesp.append(elex);
            for(String s : novos){
                novaesp.append('\n');
                novaesp.append(s);
                if(s.charAt(s.length()-1) != '"'){
                    novaesp.append(" : ");
                }
            }
            elex = novaesp.toString();
            if(tokens.size() == 1){
                elex = elex.trim();
            }
            txtLexico.setText(elex);

        }catch(Exception e){
            e.printStackTrace();
            Mensagens.alerta(this, e.getMessage(), "Declaração de tokens");
        }

    }
    
    private void abreGeracao() {
        try{
            String elex = txtLexico.getText().trim();
            String esin = txtSintatico.getText().trim();
            if(esin.length() == 0){
                GeradorReconhecedores gerador = FabricaGerador.criaGerador(elex);
                TelaGeracaoJava tela = new TelaGeracaoJava(this, gerador);
                tela.desabilitaSintatico();
                tela.setVisible(true);
            }
            else{
                TabelaTransicao ttransicao = FabricaGerador.criaTabelaTransicao(elex);
                TabelaParse tparse = FabricaGerador.criaTabelaParse(esin);
                resolveConflitosSintaticos(tparse);
                GeradorReconhecedores gerador = new GeradorReconhecedores(ttransicao, tparse);
                alertaNaoUtilizados(gerador);
                TelaGeracaoJava tela = new TelaGeracaoJava(this, gerador);            
                tela.setVisible(true);
            }
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Geração de código");
        }
    }

    private void mostraSobre() {
        new TelaSobre(this).setVisible(true);
    }

    private void resolveConflitosSintaticos(TabelaParse tabela){
        if(tabela.getConflitos().size() > 0){
            Collection<TabelaParse.Conflito> conflitos = new ArrayList<TabelaParse.Conflito>(tabela.getConflitos());
            for(TabelaParse.Conflito conflito : conflitos){
                int val = DialogConflito.resolve(this, conflito);
                conflito.resolve(val);
            }
        }
    }

    private void abreSimulacao() {
        String elex = txtLexico.getText().trim();
        String esin = txtSintatico.getText();

        try{
            TabelaTransicao ttransicao = FabricaGerador.criaTabelaTransicao(elex);
            TabelaParse tparse = FabricaGerador.criaTabelaParse(esin);
            resolveConflitosSintaticos(tparse);
            GeradorReconhecedores gerador = new GeradorReconhecedores(ttransicao, tparse);
            alertaNaoUtilizados(gerador);
            TelaSimulador tela = new TelaSimulador(gerador);
            tela.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
            Mensagens.alerta(this, e.getMessage(), "Erro"); 
        }
    }

    private void criaNovo(){
        txtLexico.setText("");
        txtSintatico.setText("");
        documento = new Documento();
    }

    private void abreArquivo(File file){
        documento.setArquivo(file);
        Especificacoes esp = documento.abre();
        txtLexico.setText(esp.getEspecificacaoLexica());
        txtSintatico.setText(esp.getEspecificacaoSintatica());
        documento.setAlterado(false);
    }
    
    private File abrir(){
        
        int ret = selecionadorArquivo.showOpenDialog(this);

        if(ret != JFileChooser.APPROVE_OPTION){
            return null;
        }
        
        File file = selecionadorArquivo.getSelectedFile();
        return file;
    }

    private void novoArquivo() {
        if(fechaArquivo()){
            criaNovo();
        }
    }
    
    private void abreArquivo(){
        File file = abrir();
        if(file == null){
            return;
        }
        if(fechaArquivo()){
            abreArquivo(file);
        }
    }

    private void removeInuteis() {
        try{
            CompiladorSintatico comp = new CompiladorSintatico();
            Gramatica gram = comp.compila(txtSintatico.getText());
            gram.removeInuteis();
            txtSintatico.setText(gram.toString());
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Remoção de símbolos inúteis");
        }
    }

    private void salvaArquivo(File file){
        documento.setArquivo(file);
        
        Especificacoes esp = new Especificacoes(txtLexico.getText(), txtSintatico.getText());

        documento.salva(esp);
    }
    
    private void salvaArquivo(){
        if(documento.getArquivo() != null){
            documento.salva(new Especificacoes(txtLexico.getText(), txtSintatico.getText()));   
        }
        else{
            salvarComo();
        }
    }
    
    private void salvarComo(){
        int ret = selecionadorArquivo.showSaveDialog(this);

        if(ret != JFileChooser.APPROVE_OPTION){
            return;
        }

        File file = selecionadorArquivo.getSelectedFile();
        if(!file.exists()){
            if(!file.toString().endsWith(".sintelo")){
                file = new File(file.toString() + ".sintelo");
            }
        }
        if(file == null){
            return;
        }
        salvaArquivo(file);
    }

    private boolean fechaArquivo(){
        if(!documento.isAlterado()){
            return true;
        }
        int ret = JOptionPane.showConfirmDialog(this, "O documento foi alterado. Deseja salvá-lo?", "Salvar documento", JOptionPane.YES_NO_CANCEL_OPTION);
        if(ret == JOptionPane.YES_OPTION){
            salvaArquivo();
            return true;
        }
        if(ret == JOptionPane.NO_OPTION){
            return true;
        }
        return false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jToolBar1 = new javax.swing.JToolBar();
        btnNovo = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        btnExtraiTokens = new javax.swing.JButton();
        btnRemoveInuteis = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        btnGeracao = new javax.swing.JButton();
        btnSimulaLexico = new javax.swing.JButton();
        btnSimulacao = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        btnManual = new javax.swing.JButton();
        btnSobre = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMensagens = new javax.swing.JTextPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLexico = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSintatico = new javax.swing.JTextPane();
        jMenuBar2 = new javax.swing.JMenuBar();
        menuArquivo = new javax.swing.JMenu();
        miNovo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        miAbrir = new javax.swing.JMenuItem();
        miSalvar = new javax.swing.JMenuItem();
        miSalvarComocomo = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        miSair = new javax.swing.JMenuItem();
        menuFerramentas = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        miLexico = new javax.swing.JMenu();
        miTabelaTransicao = new javax.swing.JMenuItem();
        miSimularLexico = new javax.swing.JMenuItem();
        miSintatico = new javax.swing.JMenu();
        miFirstFollow = new javax.swing.JMenuItem();
        miTabParse = new javax.swing.JMenuItem();
        miSimulacao = new javax.swing.JMenuItem();
        miExtrairTokens = new javax.swing.JMenuItem();
        miGeracao = new javax.swing.JMenuItem();
        miRemoverInuteis = new javax.swing.JMenuItem();
        menuAjuda = new javax.swing.JMenu();
        miAjuda = new javax.swing.JMenuItem();
        miTutorial = new javax.swing.JMenuItem();
        miSobre = new javax.swing.JMenuItem();

        jPopupMenu2.setToolTipText("Simulação");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sintelo");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setRollover(true);

        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/filenew.png"))); // NOI18N
        btnNovo.setToolTipText("Novo");
        btnNovo.setFocusable(false);
        btnNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNovo);

        btnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/abrir2.png"))); // NOI18N
        btnAbrir.setToolTipText("Abrir");
        btnAbrir.setFocusable(false);
        btnAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAbrir);

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/salvar.png"))); // NOI18N
        btnSalvar.setToolTipText("Salvar");
        btnSalvar.setFocusable(false);
        btnSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSalvar);

        jToolBar2.setRollover(true);
        jToolBar1.add(jToolBar2);

        btnExtraiTokens.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/2leftarrow.png"))); // NOI18N
        btnExtraiTokens.setToolTipText("Extrai tokens da gramatica");
        btnExtraiTokens.setFocusable(false);
        btnExtraiTokens.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExtraiTokens.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExtraiTokens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExtraiTokensActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExtraiTokens);

        btnRemoveInuteis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/lex.png"))); // NOI18N
        btnRemoveInuteis.setToolTipText("Remover símbolos inúteis");
        btnRemoveInuteis.setFocusable(false);
        btnRemoveInuteis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveInuteis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveInuteis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveInuteisActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemoveInuteis);

        jToolBar3.setRollover(true);
        jToolBar1.add(jToolBar3);

        btnGeracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/knode.png"))); // NOI18N
        btnGeracao.setToolTipText("Gerar reconhecedores (código fonte)");
        btnGeracao.setFocusable(false);
        btnGeracao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGeracao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGeracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeracaoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGeracao);

        btnSimulaLexico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/view_text.png"))); // NOI18N
        btnSimulaLexico.setToolTipText("Simular léxico");
        btnSimulaLexico.setFocusable(false);
        btnSimulaLexico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimulaLexico.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimulaLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimulaLexicoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSimulaLexico);

        btnSimulacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/windowlist.png"))); // NOI18N
        btnSimulacao.setToolTipText("Simular análise LL(1)");
        btnSimulacao.setFocusable(false);
        btnSimulacao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimulacao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSimulacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimulacaoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSimulacao);

        jToolBar4.setRollover(true);
        jToolBar1.add(jToolBar4);

        btnManual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/helpcenter.png"))); // NOI18N
        btnManual.setToolTipText("Manual");
        btnManual.setFocusable(false);
        btnManual.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnManual.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualActionPerformed(evt);
            }
        });
        jToolBar1.add(btnManual);

        btnSobre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sintelo/visao/imagens/ajudar2.png"))); // NOI18N
        btnSobre.setToolTipText("Sobre");
        btnSobre.setFocusable(false);
        btnSobre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSobre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSobreActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSobre);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 100));

        txtMensagens.setMaximumSize(new java.awt.Dimension(10000, 120));
        txtMensagens.setMinimumSize(new java.awt.Dimension(300, 100));
        txtMensagens.setPreferredSize(new java.awt.Dimension(300, 100));
        jScrollPane3.setViewportView(txtMensagens);

        getContentPane().add(jScrollPane3, java.awt.BorderLayout.SOUTH);

        jPanel1.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 250));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Léxico");
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(250, 450));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 150));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 250));

        txtLexico.setFont(new java.awt.Font("Dialog", 0, 14));
        txtLexico.setMinimumSize(new java.awt.Dimension(150, 150));
        txtLexico.setPreferredSize(new java.awt.Dimension(300, 250));
        jScrollPane1.setViewportView(txtLexico);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 250));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sintático");
        jPanel2.add(jLabel2, java.awt.BorderLayout.NORTH);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(150, 150));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 250));

        txtSintatico.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtSintatico.setMaximumSize(new java.awt.Dimension(250, 450));
        txtSintatico.setMinimumSize(new java.awt.Dimension(150, 150));
        txtSintatico.setPreferredSize(new java.awt.Dimension(300, 250));
        jScrollPane2.setViewportView(txtSintatico);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        menuArquivo.setText("Arquivo");

        miNovo.setText("Novo");
        miNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miNovoActionPerformed(evt);
            }
        });
        menuArquivo.add(miNovo);
        menuArquivo.add(jSeparator1);

        miAbrir.setText("Abrir");
        miAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAbrirActionPerformed(evt);
            }
        });
        menuArquivo.add(miAbrir);

        miSalvar.setText("Salvar");
        miSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalvarActionPerformed(evt);
            }
        });
        menuArquivo.add(miSalvar);

        miSalvarComocomo.setText("Salvar como");
        miSalvarComocomo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalvarComocomoActionPerformed(evt);
            }
        });
        menuArquivo.add(miSalvarComocomo);
        menuArquivo.add(jSeparator3);

        miSair.setText("Sair");
        miSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSairActionPerformed(evt);
            }
        });
        menuArquivo.add(miSair);

        jMenuBar2.add(menuArquivo);

        menuFerramentas.setText("Ferramentas");
        menuFerramentas.add(jSeparator2);

        miLexico.setText("Léxico");

        miTabelaTransicao.setText("Tabela de Transição");
        miTabelaTransicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTabelaTransicaoActionPerformed(evt);
            }
        });
        miLexico.add(miTabelaTransicao);

        miSimularLexico.setText("Simular");
        miSimularLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSimularLexicoActionPerformed(evt);
            }
        });
        miLexico.add(miSimularLexico);

        menuFerramentas.add(miLexico);

        miSintatico.setText("Sintático");

        miFirstFollow.setText("First e Follow");
        miFirstFollow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miFirstFollowActionPerformed(evt);
            }
        });
        miSintatico.add(miFirstFollow);

        miTabParse.setText("Tabela de Parse");
        miTabParse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTabParseActionPerformed(evt);
            }
        });
        miSintatico.add(miTabParse);

        miSimulacao.setText("Simular LL(1)");
        miSimulacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSimulacaoActionPerformed(evt);
            }
        });
        miSintatico.add(miSimulacao);

        menuFerramentas.add(miSintatico);

        miExtrairTokens.setText("Extrair tokens");
        miExtrairTokens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExtrairTokensActionPerformed(evt);
            }
        });
        menuFerramentas.add(miExtrairTokens);

        miGeracao.setText("Geração de Código");
        miGeracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGeracaoActionPerformed(evt);
            }
        });
        menuFerramentas.add(miGeracao);

        miRemoverInuteis.setText("Remover símbolos inúteis");
        miRemoverInuteis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRemoverInuteisActionPerformed(evt);
            }
        });
        menuFerramentas.add(miRemoverInuteis);

        jMenuBar2.add(menuFerramentas);

        menuAjuda.setText("Ajuda");

        miAjuda.setText("Ajuda");
        miAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAjudaActionPerformed(evt);
            }
        });
        menuAjuda.add(miAjuda);

        miTutorial.setText("Manual");
        miTutorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTutorialActionPerformed(evt);
            }
        });
        menuAjuda.add(miTutorial);

        miSobre.setText("Sobre");
        miSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSobreActionPerformed(evt);
            }
        });
        menuAjuda.add(miSobre);

        jMenuBar2.add(menuAjuda);

        setJMenuBar(jMenuBar2);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-621)/2, (screenSize.height-494)/2, 621, 494);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimulacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimulacaoActionPerformed
        abreSimulacao();
        
        
}//GEN-LAST:event_btnSimulacaoActionPerformed

    private void miTabelaTransicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTabelaTransicaoActionPerformed
        try{
            CompiladorLexico lexico = new CompiladorLexico();
            Automato aut = lexico.compila(txtLexico.getText().trim());
            aut.minimiza();
            TabelaTransicao tabela = new TabelaTransicao(aut);
            //System.out.println(tabela);
            JFrame frame = new JFrame("Tabela de transição");
            frame.add(Tabelas.criaTabelaTransicao(tabela));
            frame.pack();
            frame.setVisible(true);
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Erro");
        }
    }//GEN-LAST:event_miTabelaTransicaoActionPerformed

    private void miTabParseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTabParseActionPerformed
        try{
            CompiladorSintatico sintatico = new CompiladorSintatico();
            Gramatica gram = sintatico.compila(txtSintatico.getText());
            gram.calculaFollow();
            TabelaParse tabela = new TabelaParse(gram);
            TelaMatrizParse tela = new TelaMatrizParse(tabela);
            tela.pack();
            tela.setVisible(true);
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Erro");
        }
}//GEN-LAST:event_miTabParseActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed
        abreArquivo();
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvaArquivo();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        novoArquivo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void miNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miNovoActionPerformed
        novoArquivo();
    }//GEN-LAST:event_miNovoActionPerformed

    private void miAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAbrirActionPerformed
        abreArquivo();
    }//GEN-LAST:event_miAbrirActionPerformed

    private void miSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalvarActionPerformed
        salvaArquivo();
    }//GEN-LAST:event_miSalvarActionPerformed

    private void miSalvarComocomoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalvarComocomoActionPerformed
        salvarComo();
    }//GEN-LAST:event_miSalvarComocomoActionPerformed

    private void miSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSairActionPerformed
        if(fechaArquivo()){
            System.exit(0);
        }
    }//GEN-LAST:event_miSairActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(fechaArquivo()){
            System.exit(0);
        }        
    }//GEN-LAST:event_formWindowClosing

    private void miSimulacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSimulacaoActionPerformed
        abreSimulacao();
    }//GEN-LAST:event_miSimulacaoActionPerformed

    private void miGeracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGeracaoActionPerformed
        abreGeracao();
    }//GEN-LAST:event_miGeracaoActionPerformed

    private void btnGeracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeracaoActionPerformed
        abreGeracao();
    }//GEN-LAST:event_btnGeracaoActionPerformed

    private void btnExtraiTokensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtraiTokensActionPerformed
        adicionaTokensNaoDeclarados();
    }//GEN-LAST:event_btnExtraiTokensActionPerformed

    private void btnSimulaLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimulaLexicoActionPerformed
        simulaLexico();

    }//GEN-LAST:event_btnSimulaLexicoActionPerformed

    private void btnRemoveInuteisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveInuteisActionPerformed
        removeInuteis();
    }//GEN-LAST:event_btnRemoveInuteisActionPerformed

    private void miSimularLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSimularLexicoActionPerformed
        simulaLexico();
    }//GEN-LAST:event_miSimularLexicoActionPerformed

    private void miExtrairTokensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExtrairTokensActionPerformed
        adicionaTokensNaoDeclarados();
    }//GEN-LAST:event_miExtrairTokensActionPerformed

    private void miRemoverInuteisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRemoverInuteisActionPerformed
        removeInuteis();
    }//GEN-LAST:event_miRemoverInuteisActionPerformed

    private void miFirstFollowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miFirstFollowActionPerformed
        try{
            CompiladorSintatico sintatico = new CompiladorSintatico();
            Gramatica gram = sintatico.compila(txtSintatico.getText());
            gram.calculaFollow();
            TabelaParse tabela = new TabelaParse(gram);
            //System.out.println(tabela);
            JFrame frame = new JFrame("First e follow");
            frame.add(Tabelas.criaTabelaFirstFollow(tabela));
            frame.pack();
            frame.setVisible(true);
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Erro");
        }
    }//GEN-LAST:event_miFirstFollowActionPerformed

    private void btnSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSobreActionPerformed
        mostraSobre();
    }//GEN-LAST:event_btnSobreActionPerformed

    private void miSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSobreActionPerformed
        mostraSobre();
    }//GEN-LAST:event_miSobreActionPerformed

    private void btnManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualActionPerformed
        abreManual();
}//GEN-LAST:event_btnManualActionPerformed

    private void miTutorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTutorialActionPerformed
        abreManual();
    }//GEN-LAST:event_miTutorialActionPerformed

    private void miAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAjudaActionPerformed
        abreAjuda();
    }//GEN-LAST:event_miAjudaActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sintelo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnExtraiTokens;
    private javax.swing.JButton btnGeracao;
    private javax.swing.JButton btnManual;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemoveInuteis;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSimulaLexico;
    private javax.swing.JButton btnSimulacao;
    private javax.swing.JButton btnSobre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JMenu menuAjuda;
    private javax.swing.JMenu menuArquivo;
    private javax.swing.JMenu menuFerramentas;
    private javax.swing.JMenuItem miAbrir;
    private javax.swing.JMenuItem miAjuda;
    private javax.swing.JMenuItem miExtrairTokens;
    private javax.swing.JMenuItem miFirstFollow;
    private javax.swing.JMenuItem miGeracao;
    private javax.swing.JMenu miLexico;
    private javax.swing.JMenuItem miNovo;
    private javax.swing.JMenuItem miRemoverInuteis;
    private javax.swing.JMenuItem miSair;
    private javax.swing.JMenuItem miSalvar;
    private javax.swing.JMenuItem miSalvarComocomo;
    private javax.swing.JMenuItem miSimulacao;
    private javax.swing.JMenuItem miSimularLexico;
    private javax.swing.JMenu miSintatico;
    private javax.swing.JMenuItem miSobre;
    private javax.swing.JMenuItem miTabParse;
    private javax.swing.JMenuItem miTabelaTransicao;
    private javax.swing.JMenuItem miTutorial;
    private javax.swing.JTextPane txtLexico;
    private javax.swing.JTextPane txtMensagens;
    private javax.swing.JTextPane txtSintatico;
    // End of variables declaration//GEN-END:variables

    private void simulaLexico() {
        try{

            CompiladorLexico comp = new CompiladorLexico();
            Automato aut = comp.compila(txtLexico.getText().trim());
            aut.minimiza();
            TabelaTransicao tabela = new TabelaTransicao(aut);
            GeradorReconhecedores gerador = new GeradorReconhecedores(tabela, null);
            TelaSimulacaoLexico tela = new TelaSimulacaoLexico(gerador);
            tela.setVisible(true);
        }catch(Exception e){
            Mensagens.alerta(this, e.getMessage(), "Problema na especificação léxica");
        }
    }

}
