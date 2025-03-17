package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class YtDlp {

    public interface DownloadListener {
        void onOutput(String line);
        void onError(String error);
        void onComplete(int exitCode);
    }

    public static void baixarVideo(String url, String qualidade, DownloadListener listener) {
        String outputTemplate = "%(title)s.%(ext)s";
        String command = String.format(
                "yt-dlp -f \"bestvideo[height<=%s]+bestaudio/best[height<=%s]\" --merge-output-format mp4 -o \"%s\" \"%s\"",
                qualidade, qualidade, outputTemplate, url
        );
        executarComando(command, listener);
    }

    public static void baixarMusica(String url, DownloadListener listener) {
        String outputTemplate = "%(title)s.%(ext)s";
        String command = String.format(
                "yt-dlp -x --audio-format mp3 -o \"%s\" \"%s\"",
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
            listener.onError("Erro ao executar yt-dlp: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}