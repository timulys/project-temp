package com.kep.portal.service.download;

import com.kep.portal.model.dto.download.DownloadDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DownloadService {

    public File getFile (DownloadDto downloadDto){
        File file = new File(downloadDto.getFilePath(), downloadDto.getFileName());
        return file;
    }

    public static Resource getFileSystemResource(File file) {
        return new FileSystemResource(file);
    }
}
