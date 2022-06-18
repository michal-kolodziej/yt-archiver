package com.wheeler.ytarchiver.home;

import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String dispatch() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("downloadFormData", new DownloadFormData());
        return "home";
    }

    @PostMapping("/download")
    public String download(DownloadFormData downloadFormData) {
        if (!isValidUrl(downloadFormData.getUrl())) {
            return "redirect:/home?error=INVALID_URL";
        }

        return "redirect:/download/mp3?url=" + downloadFormData.getUrl();
    }

    private boolean isValidUrl(String url) {
        return !Strings.isNullOrEmpty(url);
    }
}
