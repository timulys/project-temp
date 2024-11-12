package com.kep.portal.util;

import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.PortalProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class UploadUtils {

    @Resource
    private PortalProperty portalProperty;
    //이미지 타입 확장자 : jpg, png, gif
    // FIXME :: 임시. 에버리 상의 후 확장자 확정되면 프로퍼티로 빼서 수정 -> 현재 자바 8에선 Set.of() 지원 안함
    // TODO :: 상담톡 스펙상 버튼 링크에 들어가는 이미지는 gif 비허용. 가이드 추가 시 해당 부분 수정인데 파일관련 정책 고도화때 정한다고 했으니 노티만 20241031
    // 현재 이미지, 이미지 링크 버튼 타입 둘 다 jpg, png 확장자로 고정 20241106 volka
    private static final Set<String> allowedImageFileExtSet = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png"));
//    private static final Set<String> allowedButtonLinkImageFileExtSet = new HashSet<>(Arrays.asList(".jpg", ".png"));
    private static final long IMAGE_MAX_SIZE = 5 * 1024 * 1024; //5MB
    private static final long LINK_BUTTON_IMAGE_MAX_SIZE = 500 * 1024; //500kB


    /**
     * 이미지 체크
     * @param file
     * @return
     */
    public boolean isImage(MultipartFile file){
        String mimeType = file.getContentType() == null ? getMimeType(file) : file.getContentType();
//        String mimeType = getMimeType(file);
        return mimeType.startsWith("image/");
    }

    /**
     * 파일 확장자 반환 dot 포함 + 소문자 변환
     * @param originalFilename
     * @return fileExtension (included dot)
     */
    public String getExt(@NotBlank String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf(".");
        return dotIndex == -1 ? "" : originalFilename.substring(dotIndex).toLowerCase();
    }

//    public boolean validateExt(String originalFilename) {
//        return portalProperty.getAllowedExtension().contains(getExt(originalFilename).toLowerCase());
//    }

    //TODO :: 링크 버튼용 검증 분기 추가
    private void validImage(@NotNull MultipartFile file) {
        if (!allowedImageFileExtSet.contains(getExt(file.getOriginalFilename()))) throw new BizException("it's not allowed to upload image. image extension can be jpg, png, gif");
        if (file.getSize() > IMAGE_MAX_SIZE) throw new BizException("it's not allowed to upload image. image file size is too large");
    }

    //링크버튼용 이미지 검증
    public void validLinkImage(@NotNull MultipartFile file) {
        if (!allowedImageFileExtSet.contains(getExt(file.getOriginalFilename()))) throw new BizException("it's not allowed to upload image. image extension can be jpg, png, gif");
        if (file.getSize() > LINK_BUTTON_IMAGE_MAX_SIZE) throw new BizException("it's not allowed to upload image. image file size is too large");
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
        return String.format("%s%s", uuid, getExt(file.getOriginalFilename()));
//        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
//        return uuid + (ObjectUtils.isEmpty(ext) ? "" : "." + ext);
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
