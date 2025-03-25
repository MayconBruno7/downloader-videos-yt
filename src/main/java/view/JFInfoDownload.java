package view;

import controller.YtDlp;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;

public class JFInfoDownload extends javax.swing.JFrame {

    private String pastaDestino = System.getProperty("user.home"); 
    private JLabel JLdiretorio;
    private JButton JBselecionarPasta;

    public JFInfoDownload() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        JLYtDownloader = new javax.swing.JLabel();
        JLlinkVideo = new javax.swing.JLabel();
        JTlinkVideo = new javax.swing.JTextField();
        JLTipoDownload = new javax.swing.JLabel();
        JCTipoDownload = new javax.swing.JComboBox<>();
        JLTipoDownload1 = new javax.swing.JLabel();
        JCQualidade = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        JBbaixar = new javax.swing.JButton();
        progressBar = new JProgressBar();  // Barra de progresso

        JBselecionarPasta = new JButton("Selecionar Pasta");
        JLdiretorio = new JLabel("Destino: " + pastaDestino);

        JBselecionarPasta.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retorno = chooser.showOpenDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                pastaDestino = chooser.getSelectedFile().getAbsolutePath();
                JLdiretorio.setText("Destino: " + pastaDestino);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Baixar ");

        JLYtDownloader.setText("Youtube Downloader (Java + yt-dlp)");

        JLlinkVideo.setText("Link do vídeo:");

        JLTipoDownload.setText("Tipo de Download: ");

        JCTipoDownload.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Selecione o tipo", "Vídeo", "Música"}));

        JLTipoDownload1.setText("Qualidade: ");

        jLabel1.setText("Progresso: ");

        JBbaixar.setText("Baixar");

        JBbaixar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String url = JTlinkVideo.getText().trim();

                // Validação imediata antes de qualquer outra coisa
                if (url.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Informe o link do vídeo!");
                    return;
                }

                Object tipoSelecionado = JCTipoDownload.getSelectedItem();
                if (tipoSelecionado == null || tipoSelecionado.toString().equals("Selecione o tipo")) {
                    JOptionPane.showMessageDialog(null, "Informe o tipo de Download!");
                    return;
                }
                String tipo = tipoSelecionado.toString();

                Object qualidadeSelecionada = JCQualidade.getSelectedItem();
                if (qualidadeSelecionada == null || qualidadeSelecionada.toString().equals("Selecione a qualidade")) {
                    JOptionPane.showMessageDialog(null, "Informe a qualidade de download!");
                    return;
                }
                String qualidade = qualidadeSelecionada.toString();

                // Desativa botão e inicia progresso
                progressBar.setIndeterminate(true);
                JBbaixar.setEnabled(false);

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        if (tipo.equals("Vídeo")) {
                            String qualidadeFormat = qualidade.replace(String.valueOf('p'), "");
                            YtDlp.baixarVideo(url, qualidadeFormat, new YtDlp.DownloadListener() {
                                @Override
                                public void onOutput(String line) {
                                    publish(line);
                                    // Verifique se a linha contém a porcentagem de progresso
                                    if (line.contains("%")) {
                                        // Usando expressão regular para extrair a porcentagem
                                        String percentString = line.replaceAll("[^0-9%]", ""); // Remove qualquer coisa que não seja número ou %

                                        // Verifique se a string tem o formato correto de porcentagem
                                        if (percentString.matches("\\d+%")) {
                                            try {
                                                // Remove o '%' e converte para inteiro
                                                int progress = Integer.parseInt(percentString.replace("%", ""));

                                                // Certifique-se de que o valor de progresso esteja dentro do intervalo válido de 0 a 100
                                                if (progress >= 0 && progress <= 100) {
                                                    progressBar.setIndeterminate(false);  // Desativa o modo indeterminado
                                                    progressBar.setValue(progress); // Atualiza a barra de progresso
                                                }
                                            } catch (NumberFormatException e) {
                                                // Caso ocorra erro na conversão, ignore ou trate da maneira que preferir
                                                System.err.println("Erro ao converter o progresso: " + e.getMessage());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    publish("Erro: " + error);
                                }

                                @Override
                                public void onComplete(int exitCode) {
                                    if (exitCode == 0) {
                                        publish("Download concluído com sucesso!");
                                    } else {
                                        publish("Download falhou!");
                                    }
                                }
                            });
                        } 
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String line : chunks) {
                            System.out.println(line); // Aqui você pode adicionar uma área de log se quiser
                        }
                    }

                    @Override
                    protected void done() {
                        progressBar.setIndeterminate(false);
                        JBbaixar.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Processo finalizado.");
                    }
                };
                worker.execute();
            }
        });

        JCTipoDownload.addActionListener(e -> {
            String tipoSelecionado = (String) JCTipoDownload.getSelectedItem();

            if (tipoSelecionado != null) {
                System.out.println("Tipo de download selecionado: " + tipoSelecionado);

                // Aqui você pode reagir ao tipo escolhido:
                if (tipoSelecionado.equals("Vídeo")) {
                    JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Selecione a qualidade", "1080p", "720p", "144p"}));
                } else if (tipoSelecionado.equals("Música")) {
                    JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Selecione a qualidade", "MP3", "AAC", "OPUS", "M4A", "FLAC"}));
                }
            }
        });

        // Layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(JLYtDownloader)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLlinkVideo)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JTlinkVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLTipoDownload)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JCTipoDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLTipoDownload1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JCQualidade, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(JBbaixar))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(JLYtDownloader)
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(JLlinkVideo)
                                        .addComponent(JTlinkVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(JLTipoDownload)
                                        .addComponent(JCTipoDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(JLTipoDownload1)
                                        .addComponent(JCQualidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addComponent(JBbaixar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new JFInfoDownload().setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton JBbaixar;
    private javax.swing.JComboBox<String> JCQualidade;
    private javax.swing.JComboBox<String> JCTipoDownload;
    private javax.swing.JLabel JLTipoDownload;
    private javax.swing.JLabel JLTipoDownload1;
    private javax.swing.JLabel JLYtDownloader;
    private javax.swing.JLabel JLlinkVideo;
    private javax.swing.JTextField JTlinkVideo;
    private javax.swing.JLabel jLabel1;
    private JProgressBar progressBar;
    // End of variables declaration
}
