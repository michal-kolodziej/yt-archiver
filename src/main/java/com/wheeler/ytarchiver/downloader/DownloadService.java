package com.wheeler.ytarchiver.downloader;

import java.io.IOException;

public interface DownloadService {
    DownloadResult downloadVideo(String url) throws IOException;
}
