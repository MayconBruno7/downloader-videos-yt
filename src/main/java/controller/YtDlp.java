package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;

public class YtDlp {

    public interface DownloadListener {
        void onOutput(String line);
        void onError(String error);
        void onComplete(int exitCode);
    }

    public static void baixarVideo(String url, String qualidade, DownloadListener listener) {
        // Recuperar o caminho da pasta Downloads de forma dinâmica
        String downloadsPath = getDownloadsPath();
        
        // Criar a pasta se não existir
        criarPasta(downloadsPath);
        
        // Definir o modelo de nome de arquivo
        String outputTemplate = downloadsPath + "/maycon-downloader-videos-musicas-yt/%(title)s.%(ext)s";

        // Comando para baixar o vídeo
        String command = String.format(
            "yt-dlp --hls-prefer-ffmpeg --restrict-filenames -f \"bv*[height<=%s]+ba/best[height<=%s]\" --merge-output-format mp4 -o \"%s\" \"%s\"",
            qualidade, qualidade, outputTemplate, url
        );
        System.out.println(command);
        executarComando(command, listener);
    }
    
    public static void baixarMusica(String url, DownloadListener listener) {

        String downloadsPath = getDownloadsPath();

        criarPasta(downloadsPath);
        String outputTemplate = downloadsPath + "/maycon-downloader-videos-musicas-yt/%(title)s.%(ext)s";

        String command = String.format(
            "yt-dlp -x --audio-format mp3 --hls-prefer-ffmpeg --restrict-filenames -o \"%s\" \"%s\"",
            outputTemplate, url

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
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                listener.onOutput(line);
            }

            while ((line = errorReader.readLine()) != null) {
                listener.onError(line);
            }

            int exitCode = process.waitFor();
            listener.onComplete(exitCode);

        } catch (IOException | InterruptedException e) {
            listener.onError("Erro ao executar o comando: " + e.getMessage());
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
