package com.wheeler.ytarchiver.downloader.binary.youtubedl;

import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

@Service
public class VideoQualityService {
    private static final String QUALITY_720P = "bestvideo[height<=720]+bestaudio/best[height<=720]";

    private static final Map<String, String> formatNameToQualityValue = ImmutableMap.of(
            "480p", "bestvideo[height<=480]+bestaudio/best[height<=480]",
            "720p", QUALITY_720P,
            "best", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best"
    );

    public List<String> getAvailableQualities(){
        return Lists.newArrayList(formatNameToQualityValue.keySet().iterator());
    }

    public String getFormatForQuality(String formatName){
        return formatNameToQualityValue.getOrDefault(formatName, QUALITY_720P);
    }
}
