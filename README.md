###Endpoints: 
- `/` and `/home/` redirects to download page view
- endpoint `/download/mp3?url=<URL HERE>` is used to download video as MP3

##Required properties:

### For yt-dl based download
`downloader.binary.download.directory=` - target temporary download directory

`downloader.binary.youtubedl.path=` - path to yt-dl executable