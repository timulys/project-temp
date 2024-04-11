package com.kep.platform.util;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 이미지 유틸리티
 */
@Component
@Slf4j
public class ImageUtils {

	/**
	 * 이미지를 가로 길이 기준으로 최대 {@param maxWidthPixel}, 최소 {@param minWidthPixel}로 사이즈 조정
	 */
	public File resizeImageByWidth(@NotNull File sourceFile, @Positive int maxWidthPixel, @PositiveOrZero int minWidthPixel, boolean overwrite) {
		try {
			BufferedImage sourceImage = ImageIO.read(sourceFile);
			final int ow = sourceImage.getWidth();
			final int oh = sourceImage.getHeight();
			log.debug("sourceWidth: {}, sourceHeight: {}", ow, oh);

			if (ow <= maxWidthPixel) { // 원본 이미지의 가로 길이가 '기준치 최대값 (maxWidth)' 보다 작을 경우
				if (ow >= minWidthPixel) { // '기준치 최소값 (minWidth)' 보다 클 경우
					log.debug("KEEP SOURCE IMAGE"); // 원본 유지
				} else { // 사이즈 늘림
					log.debug("RESIZE UP TO MIN WIDTH: {}", minWidthPixel);
					sourceImage = Scalr.resize(sourceImage, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH, minWidthPixel); //, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
				}
			} else {
				log.debug("RESIZE DOWN TO MAX WIDTH: {}", maxWidthPixel);
				sourceImage = Scalr.resize(sourceImage, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH, maxWidthPixel); //, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
			}

			log.debug("RE-SIZED WIDTH: {}, HEIGHT: {}", sourceImage.getWidth(), sourceImage.getHeight());

			// OpenJDK, 투명 배경을 처리하는 JPEG 인코더 없음
			// 투명 배경 미지원 이미지로 강제 변환
			BufferedImage resultImage = convertTransparent(sourceImage);

			// 파일 저장
			File result = overwrite ? sourceFile : File.createTempFile("__tmp__", "__file__");
			ImageIO.write(resultImage,"png", result);
			return result;

		} catch (Exception e) {
			log.error("FAILED RESIZE IMAGE: {}", e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * 원본 이미지의 가로 길이를 기준으로 세로 길이를 자름 (원본 비율 유지하지 않음), 원본 이미지가 더 작을 경우 원본 이미지 유지
	 */
	public File cropImage(@NotNull File sourceFile, @Positive int ratioWidth, @Positive int ratioHeight, boolean overwrite) {

		try {
			BufferedImage sourceImage = ImageIO.read(sourceFile);
			final int ow = sourceImage.getWidth();
			final int oh = sourceImage.getHeight();
			log.debug("sourceWidth: {}, sourceHeight: {}", ow, oh);

			final int wantedHeight = (int) (ow / ((double) ratioWidth / (double) ratioHeight));
			log.debug("wantedHeight: {}", wantedHeight);

			if (oh < wantedHeight) { // 원본 이미지가 세로 길이가 더 작을 경우 여백 추가
				sourceImage = padImage(sourceImage);
				log.debug("paddedWidth: {}, paddedHeight: {}", sourceImage.getWidth(), sourceImage.getHeight());
			} if (oh > wantedHeight) { // 원본 이미지가 세로 길이가 더 길 경우 자름
				sourceImage = Scalr.crop(sourceImage, ow, wantedHeight);
			}

			// OpenJDK, 투명 배경을 처리하는 JPEG 인코더 없음
			// 투명 배경 미지원 이미지로 강제 변환
			BufferedImage resultImage = convertTransparent(sourceImage);

			// 파일 저장
			File result = overwrite ? sourceFile : File.createTempFile("__tmp__", "__file__");
			ImageIO.write(resultImage, "png", result);
			return result;

		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * 이미지 위 아래 padding 을 넣는다
	 */
	BufferedImage padImage(@NotNull BufferedImage sourceImage) {

		int originalWidth = sourceImage.getWidth();
		int originalHeight = sourceImage.getHeight();

		// 세로 길이가 2:1 비율 보다 길면 원본 유지
		if (originalHeight >= originalWidth / 2 + 1) {
			return sourceImage;
		}

		// 2:1
		int wantedHeight = originalWidth / 2 + 1;
		int offsetHeight = (wantedHeight - originalHeight) / 2;
		log.debug("offsetHeight: {}, originalHeight: {}, wantedHeight: {}", offsetHeight, originalHeight, wantedHeight);

		// OpenJDK, 투명 배경을 처리하는 JPEG 인코더 없음
		// 투명 배경 미지원 이미지로 강제 변환
		BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), wantedHeight, sourceImage.getType());
		Graphics graphics = resultImage.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0,0, sourceImage.getWidth(), wantedHeight);
		graphics.drawImage(sourceImage, 0, offsetHeight, null);
		graphics.dispose();

		return resultImage;
	}

	/**
	 * 이미지 파일 사이즈 줄이기 위해 흑백으로 변환
	 */
	public File grayScale(@NotNull File sourceFile, @Positive long maxKiloBytes, boolean overwrite) {

		try {
			BufferedImage sourceImage = ImageIO.read(sourceFile);
			BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			resultImage.getGraphics().drawImage(sourceImage, 0, 0, null);

			// 파일 저장
			File result = overwrite ? sourceFile : File.createTempFile("__tmp__", "__file__");
			ImageIO.write(resultImage, "png", result);
			if (result.length() / 1024 > maxKiloBytes) {
				log.error("Failed scale down size: {} KB > {} KB", result.length() / 1024, maxKiloBytes);
			}
			return result;
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * OpenJDK, 투명 배경을 처리하는 JPEG 인코더 없음
	 * 투명 배경 미지원 이미지로 강제 변환
	 */
	private BufferedImage convertTransparent(BufferedImage sourceImage) {

		try {
			//BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
			resultImage.getGraphics().drawImage(sourceImage, 0, 0, null);
			return resultImage;
		} catch (Exception e) {
			log.error("FAILED CONVERT TRANSPARENT: {}", e.getLocalizedMessage());
			return sourceImage;
		}
	}
}
