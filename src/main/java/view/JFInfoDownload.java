package view;

import controller.YtDlp;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JFInfoDownload extends javax.swing.JFrame {

    private String pastaDestino = System.getProperty("user.home"); // Padrão: pasta do usuário
    private JLabel JLdiretorio;
    private JButton JBselecionarPasta;

    public JFInfoDownload() {
        initComponents();
        setLocationRelativeTo(null); // Centraliza a janela
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

        JCTipoDownload.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Vídeo", "Música", "Ambos"}));

        JLTipoDownload1.setText("Qualidade: ");

        JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1080p", "720p", "MP3", "MP4"}));

        jLabel1.setText("Progresso: ");

        JBbaixar.setText("Baixar");

        JBbaixar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String url = JTlinkVideo.getText().trim();
                String tipo = JCTipoDownload.getSelectedItem().toString();
                String qualidade = JCQualidade.getSelectedItem().toString();

                // Verificando se o link do vídeo foi informado
                if (url.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Informe o link do vídeo!");
                    return;
                }

                // Exibindo o progresso do download e desabilitando o botão durante o processo
                progressBar.setIndeterminate(true);
                JBbaixar.setEnabled(false);

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Chama o método de download
                        YtDlp.baixarVideo(url, qualidade, new YtDlp.DownloadListener() {
                            @Override
                            public void onOutput(String line) {
                                publish(line);  // Atualiza a UI com a saída
                            }

                            @Override
                            public void onError(String error) {
                                publish("Erro: " + error);
                            }

                            @Override
                            public void onComplete(int exitCode) {
                                publish("Download " + (exitCode == 0 ? "concluído" : "falhou"));
                            }
                        });
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            // Atualiza a barra de progresso ou log na interface gráfica
                            progressBar.setIndeterminate(false);
                            progressBar.setValue(progressBar.getValue() + 10);  // Ajuste conforme necessário
                        }
                    }

                    @Override
                    protected void done() {
                        // Finaliza a ação após o download
                        JBbaixar.setEnabled(true);
                    }
                };
                worker.execute();

                try {
                    if (tipo.equals("Vídeo") || tipo.equals("Ambos")) {
                        // Baixa vídeo e áudio, mesclando-os em um único arquivo MP4
                        YtDlp.baixarVideo(url, qualidade, new YtDlp.DownloadListener() {
                            @Override
                            public void onOutput(String line) {
                                System.out.println(line);  // Imprime a saída do processo no console
                            }

                            @Override
                            public void onError(String error) {
                                System.out.println("Erro: " + error);
                                JOptionPane.showMessageDialog(null, "Erro ao executar o download: " + error);
                                progressBar.setIndeterminate(false);
                                JBbaixar.setEnabled(true);
                            }

                            @Override
                            public void onComplete(int exitCode) {
                                progressBar.setIndeterminate(false);
                                JBbaixar.setEnabled(true);

                                if (exitCode == 0) {
                                    JOptionPane.showMessageDialog(null, "Download finalizado com sucesso!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erro ao finalizar download. Código: " + exitCode);
                                }
                            }
                        });
                    } else if (tipo.equals("Música")) {
                        // Baixa apenas o áudio em formato MP3
                        YtDlp.baixarMusica(url, new YtDlp.DownloadListener() {
                            @Override
                            public void onOutput(String line) {
                                System.out.println(line);
                            }

                            @Override
                            public void onError(String error) {
                                System.out.println("Erro: " + error);
                                JOptionPane.showMessageDialog(null, "Erro ao executar o download: " + error);
                                progressBar.setIndeterminate(false);
                                JBbaixar.setEnabled(true);
                            }

                            @Override
                            public void onComplete(int exitCode) {
                                progressBar.setIndeterminate(false);
                                JBbaixar.setEnabled(true);

                                if (exitCode == 0) {
                                    JOptionPane.showMessageDialog(null, "Download finalizado com sucesso!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erro ao finalizar download. Código: " + exitCode);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro durante o processo de download!");
                    progressBar.setIndeterminate(false);
                    JBbaixar.setEnabled(true);
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
