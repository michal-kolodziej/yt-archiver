package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

@Service
//TODO: Expose as REST API when moving from thymeleaf to proper frontend
public class VideoQualityService {
    private static final Map<String, String> formatNameToQualityValue = ImmutableMap.of(
            "480p", "bestvideo[height<=480]+bestaudio/best[height<=480]",
            "720p", "bestvideo[height<=720]+bestaudio/best[height<=720]",
            "best", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best"
    );

    public List<String> getAvailableQualities(){
        return Lists.newArrayList(formatNameToQualityValue.keySet().iterator());
    }

    public String getFormatForQuality(String formatName){
        return formatNameToQualityValue.getOrDefault(formatName, "720p");
    }
}
