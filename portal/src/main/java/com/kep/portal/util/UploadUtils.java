package com.kep.portal.util;

import com.kep.portal.config.property.PortalProperty;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@Slf4j
public class UploadUtils {

    @Resource
    private PortalProperty portalProperty;

    /**
     * 이미지 체크
     * @param file
     * @return
     */
    public boolean isImage(MultipartFile file){
        String mimeType = getMimeType(file);
        return mimeType.startsWith("image/");
    }

    public String getExt(@NotBlank String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    public boolean validateExt(String originalFilename) {
        return portalProperty.getAllocateExtension().contains(getExt(originalFilename).toLowerCase());
    }

    /**
     * 파일 저장
     */
    @Nullable
    public File upload(@NotNull MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new UnsupportedOperationException("<<CODE>> uploaded file cannot be empty");
        }

        // TODO: 업무(타입)별 디렉토리 생성
        String basePath = getDefaultPath(isImage(multipartFile) ? "image" : "file");
        log.info("기본 패스 :::::::;; {} ",basePath);
        String fileName = getFileName(multipartFile);
        log.info("파일이름 :::::::;; {} ",fileName);

        //FIXME :: 확장자 검증 로직 추가 (확장자 정해지면 적용 20240717 volka)
//        Assert.isTrue(validateExt(multipartFile.getOriginalFilename()), "not allowed file extension");

        try {
            File targetDirectory = new File(portalProperty.getStoragePath()
                    + File.separator + basePath);
            if (!targetDirectory.exists()) {
                if (!targetDirectory.mkdirs()) {
                    log.error("FAILED CREATE DIRECTORY: {}", portalProperty.getStoragePath()
                            + File.separator + basePath);
                    return null;
                }
                targetDirectory.setReadable(true);
                targetDirectory.setExecutable(true);
            }

            File targetFile = new File(portalProperty.getStoragePath()
                    + File.separator + basePath
                    + File.separator + fileName);
            multipartFile.transferTo(targetFile);
            targetFile.setReadable(true);
            log.info("targetFile = {}", targetFile.getAbsolutePath());
            return targetFile;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 파일 이름 변경
     * @param file
     * @return
     */
    private String getFileName(MultipartFile file){
        String uuid = UUID.randomUUID().toString();
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return uuid + (ObjectUtils.isEmpty(ext) ? "" : "." + ext);
    }

    /**
     * file mime type
     * @param file
     * @return
     */
    public String getMimeType(MultipartFile file){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file.getOriginalFilename());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    /**
     * upload storage path
     */
    private String getDefaultPath(String folder){

        LocalDate today = LocalDate.now();
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = today.format(DateTimeFormatter.ofPattern("MM"));

        return folder + File.separator + year + File.separator + month;
    }
}
