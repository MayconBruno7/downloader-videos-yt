# YouTube Downloader (Java + YT-DLP + FFMPEG)

## Descrição
Este é um aplicativo Java para download de vídeos e músicas do YouTube, utilizando o **YT-DLP** e o **FFmpeg**. O programa permite escolher o tipo de download (vídeo ou música), bem como a qualidade do arquivo baixado.

## Funcionalidades
- Download de vídeos em diferentes qualidades (1080p, 720p, 144p).
- Download de músicas em diversos formatos (MP3, AAC, OPUS, M4A, FLAC).
- Interface gráfica intuitiva com barra de progresso.
- Escolha do diretório de download.
- **YT-DLP** e **FFmpeg** são necessários para a funcionalidade de download e conversão de vídeos/músicas.

## Requisitos
1. **Java Development Kit (JDK) 8 ou superior**: necessário para compilar e executar programas Java.
2. **YT-DLP**: ferramenta de linha de comando para download de vídeos de plataformas como YouTube.
3. **FFmpeg**: ferramenta de linha de comando para manipulação de áudio e vídeo, usada para conversão de formatos.

### Como instalar os requisitos

#### 1. **Instalar o JDK (Java Development Kit)**:

- **No Windows**:
  1. Abra o terminal **PowerShell** como administrador.
  2. Execute o comando abaixo para instalar o JDK com **winget**:
     ```powershell
     winget install --id Oracle.OpenJDK.11
     ```
  3. Verifique a instalação com:
     ```powershell
     java -version
     ```

- **No Linux** (Debian/Ubuntu):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo apt update
     sudo apt install openjdk-11-jdk
     ```
  2. Verifique a instalação com:
     ```bash
     java -version
     ```

- **No Linux** (Fedora):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo dnf install java-11-openjdk
     ```
  2. Verifique a instalação com:
     ```bash
     java -version
     ```

#### 2. **Instalar o YT-DLP**:

- **No Windows**:
  1. Abra o terminal **PowerShell** como administrador.
  2. Execute o comando abaixo para instalar o **YT-DLP** com **winget**:
     ```powershell
     winget install -e --id yt-dlp.yt-dlp
     ```
  3. Verifique a instalação com:
     ```powershell
     yt-dlp --version
     ```

- **No Linux** (Debian/Ubuntu):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo apt update
     sudo apt install yt-dlp
     ```
  2. Verifique a instalação com:
     ```bash
     yt-dlp --version
     ```

- **No Linux** (Fedora):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo dnf install yt-dlp
     ```
  2. Verifique a instalação com:
     ```bash
     yt-dlp --version
     ```

#### 3. **Instalar o FFmpeg**:

- **No Windows**:
  1. Abra o terminal **PowerShell** como administrador.
  2. Execute o comando abaixo para instalar o **FFmpeg** com **winget**:
     ```powershell
     winget install --id Gyan.FFmpeg
     ```
  3. Verifique a instalação com:
     ```powershell
     ffmpeg -version
     ```

- **No Linux** (Debian/Ubuntu):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo apt update
     sudo apt install ffmpeg
     ```
  2. Verifique a instalação com:
     ```bash
     ffmpeg -version
     ```

- **No Linux** (Fedora):
  1. Abra o terminal e execute o comando:
     ```bash
     sudo dnf install ffmpeg
     ```
  2. Verifique a instalação com:
     ```bash
     ffmpeg -version
     ```

## Como Usar

1. Execute o arquivo **.jar** da aplicação.
2. Insira o link do vídeo do YouTube.
3. Escolha o tipo de download (Vídeo ou Música).
4. Selecione a qualidade desejada.
5. Clique em "Baixar" e acompanhe o progresso na barra de progresso.

## Tecnologias Utilizadas
- **Java Swing** (Interface Gráfica)
- **YT-DLP** (Download de vídeos)
- **FFmpeg** (Conversão de formatos)
- **SwingWorker** (Gerenciamento de tarefas em segundo plano)

## Licença
Este projeto está sob a licença MIT. Sinta-se à vontade para modificar e distribuir conforme necessário.

