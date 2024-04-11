package com.kep.platform.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 파일 유틸리티
 */
@Component
@Slf4j
public class FileUtils {

	private static final int CONNECTION_TIME_OUT = 1000 * 2;
	private static final int READ_TIME_OUT = 1000 * 5;

	/**
	 * URL 리소스를 파일로 저장
	 */
	@Nullable
	public File save(String sourceUrl, File file) {
		try {
			if (file == null) {
				file = File.createTempFile("__tmp__", "__file__");
			}
			org.apache.commons.io.FileUtils.copyURLToFile(new URL(sourceUrl), file, CONNECTION_TIME_OUT, READ_TIME_OUT);
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
}
