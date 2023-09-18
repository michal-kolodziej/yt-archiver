package com.wheeler.ytarchiver.homepage.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomepageMessage {
    private String header;
    private String message;
    private String date;
    private String author;
}