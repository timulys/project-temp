package com.kep.portal.repository.upload;

import com.kep.portal.model.entity.upload.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {


}
