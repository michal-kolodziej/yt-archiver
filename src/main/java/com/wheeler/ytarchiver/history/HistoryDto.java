package com.wheeler.ytarchiver.history;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
@Getter
public class HistoryDto {
    final long downloadsInLast7Days;
    final List<DownloadDto> downloads;
}
