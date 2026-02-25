// Importações necessárias da biblioteca Swing (interface gráfica)
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// Classe principal que estende JFrame (janela da aplicação)
public class CompiladorApp extends JFrame {
    
    // Componentes visuais da interface
    private JTextArea txtCodigo = new JTextArea(); // Área de texto para o usuário digitar código
    private JTable tabelaTokens; // Tabela que exibe os tokens encontrados
    private DefaultTableModel modelTabela; // Modelo de dados para a tabela
    private JLabel lblContador = new JLabel("Tokens encontrados: 0"); // Label que mostra quantidade de tokens

    // Construtor da aplicação - executa quando a janela é criada
    public CompiladorApp() {
        // Define o título da janela
        setTitle("Analisador Léxico");
        
        // Define o tamanho da janela (largura: 800px, altura: 500px)
        setSize(800, 500);
        
        // Define que a aplicação fecha quando o botão X é clicado
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Define o layout da janela como BorderLayout (divide em 5 regiões: NORTH, SOUTH, EAST, WEST, CENTER)
        setLayout(new BorderLayout());

        // ===== PAINEL SUPERIOR (BOTÕES) =====
        JPanel painelBotoes = new JPanel(); // Cria um painel para conter os botões
        
        JButton btnAnalisar = new JButton("Analisar Léxico"); // Botão para iniciar análise
        JButton btnArquivo = new JButton("Abrir Arquivo .txt"); // Botão para abrir arquivo
        
        // Adiciona os botões ao painel
        painelBotoes.add(btnArquivo);
        painelBotoes.add(btnAnalisar);
        
        // Adiciona o painel de botões na parte superior da janela
        add(painelBotoes, BorderLayout.NORTH);

        // ===== PAINEL CENTRAL (CÓDIGO E TABELA) =====
        // Cria um painel divisível (SplitPane) com divisão horizontal
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Adiciona a área de texto (com barra de rolagem) no lado esquerdo
        split.setLeftComponent(new JScrollPane(txtCodigo));
        
        // Cria o modelo da tabela com 2 colunas: "Lexema" e "Token", e 0 linhas inicialmente
        modelTabela = new DefaultTableModel(new Object[]{"Lexema", "Token"}, 0);
        
        // Cria a tabela usando o modelo
        tabelaTokens = new JTable(modelTabela);
        
        // Adiciona a tabela (com barra de rolagem) no lado direito
        split.setRightComponent(new JScrollPane(tabelaTokens));
        
        // Define a posição do divisor entre os painéis (400px do lado esquerdo)
        split.setDividerLocation(400);
        
        // Adiciona o painel divisível no centro da janela
        add(split, BorderLayout.CENTER);
        
        // Adiciona o label de contagem na parte inferior
        add(lblContador, BorderLayout.SOUTH);

        // ===== CONFIGURAÇÃO DE EVENTOS (AÇÕES DOS BOTÕES) =====
        
        // Adiciona ação ao botão "Analisar Léxico"
        btnAnalisar.addActionListener(e -> realizarAnalise());

        // Adiciona ação ao botão "Abrir Arquivo"
        btnArquivo.addActionListener(e -> {
            // Cria um seletor de arquivos
            JFileChooser chooser = new JFileChooser();
            
            // Define filtro para mostrar apenas arquivos .txt
            chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt"));
            
            // Mostra a caixa de diálogo e verifica se o usuário clicou em "Abrir"
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    // Obtém o arquivo selecionado
                    File f = chooser.getSelectedFile();
                    
                    // Lê o conteúdo do arquivo e coloca na área de texto
                    txtCodigo.setText(Files.readString(f.toPath()));
                } catch (IOException ex) {
                    // Se houver erro na leitura, mostra uma mensagem de erro
                    JOptionPane.showMessageDialog(this, "Erro ao ler arquivo.");
                }
            }
        });
    }

    // Método que realiza a análise léxica do código digitado
    private void realizarAnalise() {
        // Limpa todas as linhas da tabela
        modelTabela.setRowCount(0);
        
        // Cria uma instância do analisador léxico
        AnalisadorLexico scanner = new AnalisadorLexico();
        
        // Analisa o texto digitado e retorna uma lista de tokens
        List<Token> tokens = scanner.analisar(txtCodigo.getText());
        
        // Percorre cada token encontrado
        for (Token t : tokens) {
            // Adiciona uma nova linha na tabela com o lexema e o tipo do token
            modelTabela.addRow(new Object[]{t.lexema, t.texto});
        }
        
        // Atualiza o label mostrando a quantidade total de tokens encontrados
        lblContador.setText("Tokens encontrados: " + tokens.size());
    }

    // Método principal - ponto de entrada da aplicação
    public static void main(String[] args) {
        // Executa a criação e exibição da janela na thread de eventos (thread segura para Swing)
        SwingUtilities.invokeLater(() -> new CompiladorApp().setVisible(true));
    }
}