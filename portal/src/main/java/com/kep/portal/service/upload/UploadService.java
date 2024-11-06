package com.kep.portal.service.upload;

import com.kep.core.model.dto.upload.UploadDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.entity.upload.Upload;
import com.kep.portal.model.entity.upload.UploadMapper;
import com.kep.portal.repository.upload.UploadRepository;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UploadService {
    @Resource
    private UploadMapper uploadMapper;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private UploadRepository uploadRepository;

    @Resource
    private PortalProperty portalProperty;

    @Resource
    private UploadUtils uploadUtils;

    public UploadDto getById(@NotNull Long id) {

    	log.info("요청받은 파일 ID로 정보를 조회합니다: {}", id);
        Upload upload = uploadRepository.findById(id).orElse(null);
        return uploadMapper.map(upload);
    }

    public UploadDto store(@NotNull UploadDto dto , @NotNull File file){
    	//파일 체크 후 삭제
    	log.info("Received file details: {}", file);
        Assert.isTrue(file.isFile(), "upload file type is not file");
        log.info("받은 파일 정보 : {}",file);
        Upload upload;
        if(dto.getId() == null){
            upload = uploadMapper.map(dto);
            log.info("업로드 파일: {}", upload);
        } else {
            upload = uploadRepository.findById(dto.getId()).orElse(null);
        }

        Assert.notNull(upload, "upload is null");

        String path = file.getAbsolutePath().replace(portalProperty.getStoragePath(), "");
        path = path.replace(File.separator + file.getName(), "");

        // eddie.j download 방식 변경으로 인하여 url 경로 수정
        StringBuilder urlStringBuilder = new StringBuilder();
        String url =  urlStringBuilder.append(portalProperty.getDownloadApiUrl())
                     .append("?filePath=")
                     .append(portalProperty.getStoragePath())
                     .append(path)
                     .append("&fileName=")
                     .append(file.getName()).toString();

        upload.setPath(path);
        upload.setName(file.getName());
        upload.setSize(file.length());
        //upload.setUrl(path + "/" + file.getName());
        upload.setUrl(url);
        upload.setCreator(securityUtils.getMemberId());
        upload.setCreated(ZonedDateTime.now());
        log.info("upload: {}", upload);
        return uploadMapper.map(uploadRepository.save(upload));
    }
    
    
    /**
     * 파일삭제
     * 2023.03.28 / philip.lee7		/ 신규 
     * @param dto
     * @return boolean
     */
    public boolean delete(@NotNull UploadDto dto) {
    	
    	Assert.notNull(dto,"dto is null");
    	
    	Assert.notNull(dto.getId(),"upload id is null");
    	
    	Upload upload ;
    	upload = uploadRepository.findById(dto.getId()).orElse(null);
    	
        Assert.notNull(upload, "upload is null");
        
        String path = portalProperty.getStoragePath();
        
    	path+= upload.getPath();
    	path+= File.separator+upload.getName();
        File file = new File(path);
        boolean result = file.delete();
        
        if(result) {
    	    uploadRepository.delete(upload);
    	    log.info("파일이 성공적으로 삭제되었습니다: {}", file.getAbsolutePath());
    	    log.info("데이터베이스에서 업로드 정보를 삭제했습니다: {}", upload);
        }
       
       return result;
       
    	
    }

    /**
     * 첫 인사말 업로드 -> 첫인사말 검증으로 API 추가. 향후 파일 유틸 리팩토링 후 정리 필요 20241106
     * @param dto
     * @return
     */
    public UploadDto saveFirstMessageImage(@NotNull UploadDto dto) {

        MultipartFile mFile = dto.getFile();

        if (mFile == null || mFile.isEmpty()) throw new BizException("file must be not null");
        if (!uploadUtils.isImage(mFile)) throw new BizException("firstMessage file is only image");

        dto.setOriginalName(dto.getFile().getOriginalFilename());
        dto.setMimeType(uploadUtils.getMimeType(dto.getFile()));

        uploadUtils.validLinkImage(mFile);

        File file = Optional.ofNullable(uploadUtils.upload(mFile)).orElseThrow(()->new BizException("upload image error"));

        return store(dto, file);
    }
}
