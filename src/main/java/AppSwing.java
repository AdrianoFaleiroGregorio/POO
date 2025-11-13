import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppSwing extends JFrame {

    // Formatador de data/hora para exibição
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Modelos de tabela
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloCursos;

    // Componentes (para acesso dentro de métodos)
    private JTable tabelaUsuarios;
    private JTable tabelaCursos;
    private JTextField txtNome;
    private JComboBox<String> cbGenero;
    private JCheckBox chkEmail;
    private JRadioButton rbAluno;
    private JRadioButton rbProfessor;
    private JTextArea txtComentarios;
    private JTextField txtBuscar; // campo de busca para usuários
    private JSpinner spinIdade; // NOVO: JSpinner para Idade

    // Curso components
    private JTextField txtCursoNome;
    private JTextField txtCursoCodigo; // Código do curso

    // Filtro para a tabela de usuários
    private TableRowSorter<DefaultTableModel> sorterUsuarios;

    public AppSwing() {
        setTitle("Aplicação de Teste - Java Swing (Expandida com JSpinner e Filtro)");
        setSize(850, 650); // Aumentei um pouco o tamanho
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        add(painelPrincipal);

        // MENU
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSalvar = new JMenuItem("Salvar...");
        JMenuItem itemCarregar = new JMenuItem("Carregar...");
        JMenuItem itemSair = new JMenuItem("Sair");
        menuArquivo.add(itemSalvar);
        menuArquivo.add(itemCarregar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);
        setJMenuBar(menuBar);

        itemSalvar.addActionListener(this::acaoSalvar);
        itemCarregar.addActionListener(this::acaoCarregar);
        itemSair.addActionListener(e -> System.exit(0));

        // TITULO
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("Demonstração de Componentes Swing (Expandida)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelSuperior.add(lblTitulo);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

        // TABS: Usuários e Cursos
        JTabbedPane tabs = new JTabbedPane();

        // --- TAB USUÁRIOS ---
        JPanel painelUsuarios = criarPanelUsuarios();
        tabs.addTab("Usuários", painelUsuarios);

        // --- TAB CURSOS ---
        JPanel painelCursos = criarPanelCursos();
        tabs.addTab("Cursos", painelCursos);

        painelPrincipal.add(tabs, BorderLayout.CENTER);
    }

    // Cria painel para a aba Usuários
    private JPanel criarPanelUsuarios() {
        JPanel container = new JPanel(new BorderLayout(10, 0)); // Adicionado gap
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário (esquerda)
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;

        // Linha 1: Nome
        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Nome:"), c);
        c.gridx = 1;
        txtNome = new JTextField(20);
        form.add(txtNome, c);

        // Linha 2: Gênero
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Gênero:"), c);
        c.gridx = 1;
        String[] generos = {"Masculino", "Feminino", "Outro"};
        cbGenero = new JComboBox<>(generos);
        form.add(cbGenero, c);

        // Linha 3: Idade (NOVO JSpinner)
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Idade:"), c);
        c.gridx = 1;
        // JSpinner com modelo numérico (Valor inicial: 18, Mínimo: 0, Máximo: 150, Passo: 1)
        SpinnerModel modeloIdade = new SpinnerNumberModel(18, 0, 150, 1);
        spinIdade = new JSpinner(modeloIdade);
        // Garante que o spinner não ocupe toda a largura
        JPanel painelSpinner = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSpinner.add(spinIdade);
        form.add(painelSpinner, c);


        // Linha 4: Preferência
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Preferência:"), c);
        c.gridx = 1;
        chkEmail = new JCheckBox("Receber e-mails");
        form.add(chkEmail, c);

        // Linha 5: Tipo de usuário
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Tipo de usuário:"), c);
        c.gridx = 1;
        JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbAluno = new JRadioButton("Aluno XPTO");
        rbProfessor = new JRadioButton("Professor XPTO");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbAluno);
        grupo.add(rbProfessor);
        painelRadios.add(rbAluno);
        painelRadios.add(rbProfessor);
        form.add(painelRadios, c);

        // Linha 6: Comentários
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Comentários:"), c);
        c.gridx = 1;
        txtComentarios = new JTextArea(3, 20);
        txtComentarios.setLineWrap(true);
        form.add(new JScrollPane(txtComentarios), c);

        // Linha 7: Botões do formulário
        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar Usuário");
        JButton btnLimpar = new JButton("Limpar Campos");
        botoesForm.add(btnAdicionar);
        botoesForm.add(btnLimpar);
        c.gridx = 0; c.gridy++;
        c.gridwidth = 2;
        form.add(botoesForm, c);

        // Adiciona um componente "elástico" para empurrar o formulário para cima
        c.gridy++;
        c.weighty = 1.0;
        form.add(Box.createVerticalGlue(), c);

        // Painel esquerdo com formulário
        container.add(form, BorderLayout.WEST);

        // Painel direito: busca + tabela
        JPanel painelDireito = new JPanel(new BorderLayout(0, 5));

        // Campo de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.add(new JLabel("Buscar (Nome):"));
        txtBuscar = new JTextField(25);
        painelBusca.add(txtBuscar);
        painelDireito.add(painelBusca, BorderLayout.NORTH);

        // Tabela de usuários com coluna de data/hora E IDADE (NOVO)
        String[] colunas = {"Nome", "Gênero", "Idade", "Tipo", "Data/Hora"}; // Coluna "Idade" adicionada
        modeloUsuarios = new DefaultTableModel(colunas, 0) {
            // impedir edição direta das células
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Para que a coluna Idade seja tratada como Integer para ordenação
                if (columnIndex == 2) return Integer.class;
                return String.class;
            }
        };
        tabelaUsuarios = new JTable(modeloUsuarios);
        sorterUsuarios = new TableRowSorter<>(modeloUsuarios);
        tabelaUsuarios.setRowSorter(sorterUsuarios);

        JScrollPane scrollTabela = new JScrollPane(tabelaUsuarios);
        painelDireito.add(scrollTabela, BorderLayout.CENTER);

        // Botões para manipular tabela (Remove)
        JPanel painelAcoesTabela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRemoverSelecionado = new JButton("Remover Selecionado");
        painelAcoesTabela.add(btnRemoverSelecionado);
        painelDireito.add(painelAcoesTabela, BorderLayout.SOUTH);

        container.add(painelDireito, BorderLayout.CENTER);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnLimpar.addActionListener(e -> limparCamposUsuario());
        btnRemoverSelecionado.addActionListener(e -> removerUsuarioSelecionado());

        // Filtro ao digitar no campo de busca
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
        });

        return container;
    }

    // Cria painel para a aba Cursos
    private JPanel criarPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário simples para cursos
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do Curso"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;

        // Linha 1: Nome do Curso
        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Nome do Curso:"), c);
        c.gridx = 1;
        txtCursoNome = new JTextField(20);
        form.add(txtCursoNome, c);

        // Linha 2: Código (com filtro de dígitos)
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Código (Somente Dígitos):"), c);
        c.gridx = 1;
        txtCursoCodigo = new JTextField(10);
        // NOVO: Adiciona o DocumentFilter para aceitar apenas dígitos
        ((AbstractDocument) txtCursoCodigo.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        form.add(txtCursoCodigo, c);

        // Linha 3: Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionarCurso = new JButton("Adicionar Curso");
        JButton btnLimparCurso = new JButton("Limpar");
        botoes.add(btnAdicionarCurso);
        botoes.add(btnLimparCurso);
        c.gridx = 0; c.gridy++;
        c.gridwidth = 2;
        form.add(botoes, c);

        // Empurrar o formulário para cima
        c.gridy++;
        c.weighty = 1.0;
        form.add(Box.createVerticalGlue(), c);

        panel.add(form, BorderLayout.WEST);

        // Tabela de cursos
        String[] colunasCurso = {"Nome do Curso", "Código"};
        modeloCursos = new DefaultTableModel(colunasCurso, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaCursos = new JTable(modeloCursos);
        tabelaCursos.setRowSorter(new TableRowSorter<>(modeloCursos)); // Adicionado sorter
        JScrollPane scrollCursos = new JScrollPane(tabelaCursos);
        panel.add(scrollCursos, BorderLayout.CENTER);

        // Botões da tabela de cursos
        JPanel acoesCursos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRemoverCurso = new JButton("Remover Selecionado");
        acoesCursos.add(btnRemoverCurso);
        panel.add(acoesCursos, BorderLayout.SOUTH);

        // Ações
        btnAdicionarCurso.addActionListener(e -> adicionarCurso());
        btnLimparCurso.addActionListener(e -> limparCamposCurso());
        btnRemoverCurso.addActionListener(e -> removerCursoSelecionado());

        return panel;
    }

    // ---------------------- FILTRO DE DOCUMENTO (NOVO) ----------------------
    /**
     * DocumentFilter que permite a inserção apenas de dígitos (0-9).
     */
    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            if (string.matches("\\d+")) { // Regex para verificar se a string contém apenas dígitos
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep(); // Toca um som de aviso
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // ---------------------- AÇÕES USUÁRIO ----------------------
    private void adicionarUsuario() {
        try {
            String nome = txtNome.getText().trim();
            String genero = (String) cbGenero.getSelectedItem();
            int idade = (Integer) spinIdade.getValue(); // Obtém valor do JSpinner (NOVO)
            String tipo = rbAluno.isSelected() ? "Aluno" :
                    rbProfessor.isSelected() ? "Professor" : "Não informado";

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, informe o nome.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dataHora = LocalDateTime.now().format(DTF);
            // Adiciona Idade na terceira coluna (índice 2)
            modeloUsuarios.addRow(new Object[]{nome, genero, idade, tipo, dataHora});
            JOptionPane.showMessageDialog(this, "Registro adicionado com sucesso!");
            limparCamposUsuario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar usuário: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCamposUsuario() {
        txtNome.setText("");
        cbGenero.setSelectedIndex(0);
        spinIdade.setValue(18); // Reseta JSpinner (NOVO)
        chkEmail.setSelected(false);
        rbAluno.setSelected(false);
        rbProfessor.setSelected(false);
        txtComentarios.setText("");
    }

    private void removerUsuarioSelecionado() {
        try {
            int linhaView = tabelaUsuarios.getSelectedRow();
            if (linhaView == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma linha para remover.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // converter índice da view para modelo, devido ao sorter/filtro
            int linha = tabelaUsuarios.convertRowIndexToModel(linhaView);

            String nome = (String) modeloUsuarios.getValueAt(linha, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente remover o usuário \"" + nome + "\"?",
                    "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modeloUsuarios.removeRow(linha);
                JOptionPane.showMessageDialog(this, "Registro removido com sucesso.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover registro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltro() {
        String texto = txtBuscar.getText().trim();
        if (texto.length() == 0) {
            sorterUsuarios.setRowFilter(null);
        } else {
            // filtra a coluna 0 (Nome) por caso-insensitivo contendo
            sorterUsuarios.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }

    // ---------------------- AÇÕES CURSOS ----------------------
    private void adicionarCurso() {
        try {
            String nome = txtCursoNome.getText().trim();
            String codigo = txtCursoCodigo.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Informe o nome do curso.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Não é mais necessário validar código, pois o DocumentFilter já garante que é numérico

            modeloCursos.addRow(new Object[]{nome, codigo});
            JOptionPane.showMessageDialog(this, "Curso adicionado com sucesso!");
            limparCamposCurso();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar curso: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCamposCurso() {
        txtCursoNome.setText("");
        txtCursoCodigo.setText("");
    }

    private void removerCursoSelecionado() {
        try {
            int linha = tabelaCursos.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma linha de curso para remover.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Não precisa de convertRowIndexToModel pois não foi aplicado filtro/sorter na tabela de cursos
            String nome = (String) modeloCursos.getValueAt(linha, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente remover o curso \"" + nome + "\"?",
                    "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modeloCursos.removeRow(linha);
                JOptionPane.showMessageDialog(this, "Curso removido com sucesso.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover curso: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------- SALVAR / CARREGAR ----------------------
    // Formato simples do arquivo:
    // [USUARIOS]
    // Nome;Genero;Idade;Tipo;DataHora <-- NOVO: Idade adicionada
    // ...
    // [CURSOS]
    // Nome;Codigo
    private void acaoSalvar(ActionEvent ev) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Salvar registros (.txt)");
        int res = fc.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        // garantir extensão .txt
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }

        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // usuarios
            bw.write("[USUARIOS]");
            bw.newLine();
            for (int i = 0; i < modeloUsuarios.getRowCount(); i++) {
                String nome = safeString(modeloUsuarios.getValueAt(i, 0));
                String genero = safeString(modeloUsuarios.getValueAt(i, 1));
                String idade = safeString(modeloUsuarios.getValueAt(i, 2)); // NOVO: Idade
                String tipo = safeString(modeloUsuarios.getValueAt(i, 3));
                String dataHora = safeString(modeloUsuarios.getValueAt(i, 4));
                // separar por ';'
                bw.write(nome + ";" + genero + ";" + idade + ";" + tipo + ";" + dataHora);
                bw.newLine();
            }

            // cursos
            bw.write("[CURSOS]");
            bw.newLine();
            for (int i = 0; i < modeloCursos.getRowCount(); i++) {
                String nome = safeString(modeloCursos.getValueAt(i, 0));
                String codigo = safeString(modeloCursos.getValueAt(i, 1));
                bw.write(nome + ";" + codigo);
                bw.newLine();
            }

            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso em:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar arquivo: " + ex.getMessage(),
                    "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoCarregar(ActionEvent ev) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Carregar registros (.txt)");
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Arquivo não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            // limpar modelos antes de carregar
            modeloUsuarios.setRowCount(0);
            modeloCursos.setRowCount(0);

            String line;
            String section = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("[USUARIOS]")) {
                    section = "USUARIOS";
                    continue;
                } else if (line.equalsIgnoreCase("[CURSOS]")) {
                    section = "CURSOS";
                    continue;
                }

                if ("USUARIOS".equals(section)) {
                    // Nome;Genero;Idade;Tipo;DataHora <-- NOVO
                    String[] parts = line.split(";", -1);
                    String nome = parts.length > 0 ? parts[0] : "";
                    String genero = parts.length > 1 ? parts[1] : "";
                    // Tenta converter Idade para Integer, se falhar, usa 0
                    String idadeStr = parts.length > 2 ? parts[2] : "0";
                    int idade = 0;
                    try { idade = Integer.parseInt(idadeStr); } catch (NumberFormatException ignored) {}

                    String tipo = parts.length > 3 ? parts[3] : "";
                    String dataHora = parts.length > 4 ? parts[4] : LocalDateTime.now().format(DTF);
                    // Adiciona Idade (Integer) na linha
                    modeloUsuarios.addRow(new Object[]{nome, genero, idade, tipo, dataHora});
                } else if ("CURSOS".equals(section)) {
                    String[] parts = line.split(";", -1);
                    String nome = parts.length > 0 ? parts[0] : "";
                    String codigo = parts.length > 1 ? parts[1] : "";
                    modeloCursos.addRow(new Object[]{nome, codigo});
                } else {
                    // se encontrar linhas antes de seção, ignorar (ou logar)
                }
            }

            JOptionPane.showMessageDialog(this, "Dados carregados com sucesso de:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar arquivo: " + ex.getMessage(),
                    "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar o arquivo: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String safeString(Object o) {
        // Garante que a idade (Integer) seja tratada corretamente e remove quebras de linha
        return o == null ? "" : o.toString().replace("\n", " ").replace("\r", " ");
    }

    // ---------------------- MAIN ----------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppSwing().setVisible(true));
    }
}