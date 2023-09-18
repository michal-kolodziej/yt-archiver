package com.wheeler.ytarchiver.homepage;

import com.wheeler.ytarchiver.downloader.binary.youtubedl.VideoQualityService;
import com.wheeler.ytarchiver.homepage.message.HomepageMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URL;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final VideoQualityService videoQualityService;
    private final HomepageMessageService homepageMessageService;

    @GetMapping
    public String dispatch() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("downloadFormData", new DownloadFormData());
        model.addAttribute("availableVideoFormats", videoQualityService.getAvailableQualities());
        model.addAttribute("homepageMessages", homepageMessageService.getMessages());
        return "home";
    }

    @PostMapping("/download/mp3")
    public String downloadMp3(DownloadFormData downloadFormData) {
        return isUrlInvalid(downloadFormData.getUrl())
                ? "redirect:/home?error=INVALID_URL"
                : "redirect:/api/download/mp3?url=" + downloadFormData.getUrl();
    }

    @PostMapping("/download/mp4")
    public String downloadMp4(DownloadFormData downloadFormData) {
        return isUrlInvalid(downloadFormData.getUrl())
                ? "redirect:/home?error=INVALID_URL"
                : "redirect:/api/download/mp4?url=" + downloadFormData.getUrl() + "&quality=" + downloadFormData.getQuality();
    }

    private boolean isUrlInvalid(String urlAsString) {
        try {
            URL url = new URL(urlAsString);
            url.toURI();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
