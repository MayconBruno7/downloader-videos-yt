/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package view;

import controller.YtDlp;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author maycon-linux
 */
public class JFInfoDownload extends javax.swing.JFrame {

    private String pastaDestino = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    private JLabel JLdiretorio;
    private JButton JBselecionarPasta;
    private JProgressBar progressBar;

    public JFInfoDownload() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Componentes da interface
        JLYtDownloader = new javax.swing.JLabel();
        JLlinkVideo = new javax.swing.JLabel();
        JTlinkVideo = new javax.swing.JTextField();
        JLTipoDownload = new javax.swing.JLabel();
        JCTipoDownload = new javax.swing.JComboBox<>();
        JLTipoDownload1 = new javax.swing.JLabel();
        JCQualidade = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        JBbaixar = new javax.swing.JButton();
        progressBar = new JProgressBar(0, 100);

        // Botão para selecionar pasta
        JBselecionarPasta = new JButton("Selecionar Pasta");
        JLdiretorio = new JLabel("Destino: " + pastaDestino);

        // Configuração da janela
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("YouTube Downloader");
        setResizable(false);

        // Textos dos componentes
        JLYtDownloader.setText("YouTube Downloader (Java + YT-DLP + FFMPEG)");
        JLlinkVideo.setText("Link do vídeo:");
        JLTipoDownload.setText("Tipo de Download:");
        JLTipoDownload1.setText("Qualidade:");
        jLabel1.setText("Progresso:");
        JBbaixar.setText("Baixar");

        // Configuração do combobox de tipos
        JCTipoDownload.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Selecione o tipo", "Vídeo", "Música"}));
        JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Selecione primeiro o tipo"}));

        // Ação do botão de seleção de pasta
        JBselecionarPasta.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Selecione a pasta de destino");

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                pastaDestino = chooser.getSelectedFile().getAbsolutePath();
                JLdiretorio.setText("Destino: " + pastaDestino);
            }
        });

        // Ação do botão de download
        JBbaixar.addActionListener(this::iniciarDownload);

        // Ação ao mudar o tipo de download
        JCTipoDownload.addActionListener(e -> atualizarOpcoesQualidade());

        // Layout da interface
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
                                                .addComponent(JTlinkVideo, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLTipoDownload)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JCTipoDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLTipoDownload1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JCQualidade, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(JBselecionarPasta)
                                        .addComponent(JLdiretorio)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(JBbaixar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JLYtDownloader)
                                .addGap(18, 18, 18)
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
                                .addGap(18, 18, 18)
                                .addComponent(JBselecionarPasta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JLdiretorio)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(JBbaixar)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void atualizarOpcoesQualidade() {
        String tipoSelecionado = (String) JCTipoDownload.getSelectedItem();

        if (tipoSelecionado != null) {
            if (tipoSelecionado.equals("Vídeo")) {
                JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(
                        new String[]{"Selecione a qualidade", "1080p", "720p", "480p", "360p", "240p", "144p"}));
            } else if (tipoSelecionado.equals("Música")) {
                JCQualidade.setModel(new javax.swing.DefaultComboBoxModel<>(
                        new String[]{"Selecione o formato", "MP3", "AAC", "OPUS", "M4A", "FLAC", "WAV"}));
            }
        }
    }

    private void iniciarDownload(ActionEvent evt) {
        String url = JTlinkVideo.getText().trim();

        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o link do vídeo!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = (String) JCTipoDownload.getSelectedItem();
        if (tipo == null || tipo.equals("Selecione o tipo")) {
            JOptionPane.showMessageDialog(this, "Selecione o tipo de download!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qualidade = (String) JCQualidade.getSelectedItem();
        if (qualidade == null || qualidade.startsWith("Selecione")) {
            JOptionPane.showMessageDialog(this, "Selecione a qualidade/formato!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Configura a interface
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        setComponentsEnabled(false);

        // Worker para execução em segundo plano
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                YtDlp.DownloadListener listener = new YtDlp.DownloadListener() {
                    @Override
                    public void onOutput(String line) {
                        publish(line);
                    }

                    @Override
                    public void onError(String error) {
                        publish("ERRO: " + error);
                    }

                    @Override
                    public void onComplete(int exitCode) {
                        publish(exitCode == 0 ? "Concluído com sucesso!" : "Falha no download!");
                    }

                    @Override
                    public void onProgress(int progress) {
                        publish("PROGRESSO: " + progress);
                    }
                };

                if (tipo.equals("Vídeo")) {
                    YtDlp.baixarVideo(url, qualidade.replace("p", ""), pastaDestino, listener);
                } else {
                    YtDlp.baixarMusica(url, qualidade, pastaDestino, listener);
                }

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    System.out.println(line);
                    if (line.startsWith("PROGRESSO: ")) {
                        int progress = Integer.parseInt(line.replace("PROGRESSO: ", ""));
                        progressBar.setValue(progress);
                        progressBar.setString(progress + "%");
                    } else if (line.startsWith("ERRO: ")) {
                        JOptionPane.showMessageDialog(JFInfoDownload.this,
                                line.replace("ERRO: ", ""), "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (line.contains("Concluído") || line.contains("Falha")) {
                        progressBar.setValue(100);
                        progressBar.setString(line.contains("sucesso") ? "Concluído!" : "Falhou!");
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    get(); 
                    JOptionPane.showMessageDialog(null, "Download concluido!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(JFInfoDownload.this,
                            "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    progressBar.setValue(0);
                    progressBar.setString("0%");
                } finally {
                    setComponentsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void setComponentsEnabled(boolean enabled) {
        JBbaixar.setEnabled(enabled);
        JTlinkVideo.setEnabled(enabled);
        JCTipoDownload.setEnabled(enabled);
        JCQualidade.setEnabled(enabled);
        JBselecionarPasta.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFInfoDownload frame = new JFInfoDownload();
            frame.setVisible(true);
        });
    }

    // Variáveis de componentes
    private javax.swing.JButton JBbaixar;
    private javax.swing.JComboBox<String> JCQualidade;
    private javax.swing.JComboBox<String> JCTipoDownload;
    private javax.swing.JLabel JLTipoDownload;
    private javax.swing.JLabel JLTipoDownload1;
    private javax.swing.JLabel JLYtDownloader;
    private javax.swing.JLabel JLlinkVideo;
    private javax.swing.JTextField JTlinkVideo;
    private javax.swing.JLabel jLabel1;
}
