package com.kep.platform.util;

import com.kep.platform.config.property.CoreProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 파일 유틸리티
 */
@Component
@Slf4j
public class FileUtils {

	@Resource
	private CoreProperty coreProperty;

	private static final int CONNECTION_TIME_OUT = 1000 * 2;
	private static final int READ_TIME_OUT = 1000 * 5;

	/**
	 * URL 리소스를 파일로 저장
	 */
	@Nullable
	public File save(String sourceUrl, File file) {
		try {
			if (file == null) file = File.createTempFile("__tmp__", "__file__");

			URL url = isPortalDomain(sourceUrl) ? createPortalServiceUrl(sourceUrl) : new URL(sourceUrl);
			org.apache.commons.io.FileUtils.copyURLToFile(url, file, CONNECTION_TIME_OUT, READ_TIME_OUT);

			return file;
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * URL 리소스를 임시 파일로 저장
	 */
	@Nullable
	public File save(String sourceUrl) {

		return save(sourceUrl, null);
	}

	private URL createPortalServiceUrl(String sourceUrl) throws MalformedURLException {
		return new URL(sourceUrl.replace(coreProperty.getPortalDomain(), coreProperty.getPortalServiceUri()));
	}


	private boolean isPortalDomain(String sourceUrl) {
		return sourceUrl.startsWith(coreProperty.getPortalDomain());
	}
}
