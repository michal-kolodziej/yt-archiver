# Endpoints:
- `/` and `/home/` redirects to download page view
- Endpoint `/download/mp3?url=<URL HERE>` is used to download video as MP3

# Requirements
You need the latest yt-dlp and ffmpeg to use the app.

### FFMPEG
- When on Linux: `sudo apt install ffmpeg`
- When on Windows: [Download FFMPEG](https://ffmpeg.org/download.html)

### YT-DLP
Download the latest version from: [YT-DLP Releases](https://github.com/yt-dlp/yt-dlp/releases)  
Replace it with the existing version in the youtube-dl project folder.

## APP Properties:
For yt-dl based download:   
- `downloader.binary.download.directory=` - target temporary download directory  
- `downloader.binary.youtubedl.path=` - path to yt-dl executable