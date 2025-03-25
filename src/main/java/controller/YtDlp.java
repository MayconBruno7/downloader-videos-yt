package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.regex.*;

public class YtDlp {

    public interface DownloadListener {
        void onOutput(String line);
        void onError(String error);
        void onComplete(int exitCode);
        void onProgress(int progress);
    }

    public static void baixarVideo(String url, String qualidade, String destino, DownloadListener listener) {
        String outputTemplate = destino + File.separator + "%(title)s.%(ext)s";
        
        // Formato mais simples que já baixa vídeo e áudio juntos
        String command = String.format(
            "yt-dlp --newline --progress --restrict-filenames " +
            "-f \"bestvideo[height<=%s]+bestaudio/best[height<=%s]\" " +
            "--merge-output-format mp4 -o \"%s\" \"%s\"",
            qualidade, qualidade, outputTemplate, url
        );
        
        executarComando(command, listener);
    }
    
    public static void baixarMusica(String url, String formato, String destino, DownloadListener listener) {
        String outputTemplate = destino + File.separator + "%(title)s.%(ext)s";
        
        // Comando simplificado para áudio
        String command = String.format(
            "yt-dlp --newline --progress -x --audio-format %s " +
            "--audio-quality 0 --restrict-filenames -o \"%s\" \"%s\"",
            formato.toLowerCase(), outputTemplate, url
        );
        
        executarComando(command, listener);
    }

    private static void executarComando(String comando, DownloadListener listener) {
        ProcessBuilder builder;
        
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd.exe", "/c", comando);
        } else {
            builder = new ProcessBuilder("bash", "-c", comando);
        }

        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            // Padrão simplificado para detectar progresso
            Pattern progressPattern = Pattern.compile("\\[download\\]\\s*(\\d+\\.?\\d*)%");

            while ((line = reader.readLine()) != null) {
                listener.onOutput(line);
                
                // Processa o progresso do download
                Matcher progressMatcher = progressPattern.matcher(line);
                if (progressMatcher.find()) {
                    try {
                        float percent = Float.parseFloat(progressMatcher.group(1));
                        listener.onProgress((int) percent);
                    } catch (NumberFormatException e) {
                        // Ignora erros de parsing
                    }
                }
                // Detecta conclusão
                else if (line.contains("100%") || 
                         line.contains("Deleting original file") ||
                         line.contains("Merging formats into")) {
                    listener.onProgress(100);
                }
            }

            int exitCode = process.waitFor();
            listener.onComplete(exitCode);

        } catch (IOException | InterruptedException e) {
            listener.onError("Erro ao executar o comando: " + e.getMessage());
        }
    }

    private static void extractAndNotifyProgress(String line, DownloadListener listener) {
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            if (part.endsWith("%")) {
                try {
                    String percentStr = part.replace("%", "");
                    int progress = (int) Float.parseFloat(percentStr);
                    listener.onProgress(progress);
                    break;
                } catch (NumberFormatException e) {
                    // Ignora se não for um número válido
                }
            }
        }
    }

    private static void extractFFmpegProgress(String line, DownloadListener listener) {
        // Exemplo de linha: frame=  123 fps= 23 q=28.0 size=    1234kB time=00:00:04.23 bitrate=1234kbits/s speed=0.923x
        try {
            // Extrai o tempo atual
            String timePart = line.split("time=")[1].split(" ")[0];
            String[] timeComponents = timePart.split(":");

            double totalSeconds = Double.parseDouble(timeComponents[0]) * 3600
                    + Double.parseDouble(timeComponents[1]) * 60
                    + Double.parseDouble(timeComponents[2]);

            // Você precisa saber a duração total do vídeo (pode ser obtida antes)
            // Aqui estou usando um valor fixo como exemplo - você precisará adaptar
            double totalDuration = 300.0; // 5 minutos como exemplo

            int progress = (int) ((totalSeconds / totalDuration) * 100);
            if (progress > 100) {
                progress = 100;
            }

            listener.onProgress(progress);
        } catch (Exception e) {
            // Caso o parsing falhe
        }
    }

    private static String getDownloadsPath() {
        String userHome = System.getProperty("user.home");
        return userHome + "/Downloads";
    }

    private static void criarPasta(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
