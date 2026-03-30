package com.auhpp.event_management.util;

import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    private static final List<String> ALLOWED_AUDIO_EXTENSIONS = Arrays.asList("webm", "mp3", "m4a", "wav", "ogg");

    private static final long MAX_IMAGE_FILE_SIZE = 5 * 1024 * 1024;

    private static final long MAX_AUDIO_FILE_SIZE = 1000 * 1024 * 1024;

    public static void validateImageFile(MultipartFile file) {

        if (file.getSize() > MAX_IMAGE_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension)) {
            throw new AppException(ErrorCode.INVALID_EXTENSION);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.NOT_AN_IMAGE);
        }
    }

    public static void validateAudioFile(MultipartFile file) {

        if (file.getSize() > MAX_AUDIO_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_AUDIO_EXTENSIONS.contains(fileExtension)) {
            throw new AppException(ErrorCode.INVALID_EXTENSION);
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("audio/") && !contentType.equals("video/webm"))) {
            throw new AppException(ErrorCode.NOT_AN_AUDIO);
        }
    }


}
