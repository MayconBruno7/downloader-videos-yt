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

        // Criar a pasta download-teste se não existir
        criarPasta(downloadsPath);

        // Definir o modelo de nome de arquivo
        String outputTemplate = downloadsPath + "/maycon-downloader-videos-musicas-yt/%(title)s.%(ext)s";

        // Comando para baixar o vídeo
        String command = String.format(
                "yt-dlp -f \"bestvideo[height<=%s]+bestaudio/best[height<=%s]\" --merge-output-format mp4 -o \"%s\" \"%s\"",
                qualidade, qualidade, outputTemplate, url
        );
        executarComando(command, listener);
    }

    public static void baixarMusica(String url, DownloadListener listener) {
        // Recuperar o caminho da pasta Downloads de forma dinâmica
        String downloadsPath = getDownloadsPath();

        // Criar a pasta download-teste se não existir
        criarPasta(downloadsPath);

        // Definir o modelo de nome de arquivo
        String outputTemplate = downloadsPath + "/maycon-downloader-videos-musicas-yt/%(title)s.%(ext)s";

        // Comando para baixar a música
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

    private static String getDownloadsPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String downloadsPath = "";

        // Para Windows
        if (os.contains("win")) {
            String userProfile = System.getenv("USERPROFILE");
            downloadsPath = userProfile + "\\Downloads";  // No Windows, barras invertidas
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            String homeDirectory = System.getenv("HOME");
            downloadsPath = homeDirectory + "/Downloads";  // No Linux/Mac, usa barra normal
        }

        System.out.println("Caminho das Downloads: " + downloadsPath);  // Verifique o caminho
        return downloadsPath;
    }

    private static void criarPasta(String path) {
        String pastaPath = path + File.separator + "maycon-downloader-videos-musicas-yt";
        File pasta = new File(pastaPath);

        if (!pasta.exists()) {
            if (pasta.mkdirs()) {
                System.out.println("Pasta 'maycon-downloader-videos-musicas-yt' criada com sucesso.");
            } else {
                System.out.println("Falha ao criar a pasta 'maycon-downloader-videos-musicas-yt'.");
                System.out.println("Erro: " + pastaPath);  // Exibe o caminho exato para depuração
            }
        } else {
            System.out.println("A pasta já existe.");
        }
    }

}
