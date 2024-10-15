package com.min429.tourism_platform_server.common.service.firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.min429.tourism_platform_server.common.exception.common.InternalException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

@Service
public class FirebaseService {

	@Value("${app.firebase-bucket}")
	private String firebaseBucket;

	public String uploadFiles(MultipartFile file, String fileName) {
		Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

		try {
			InputStream content = new ByteArrayInputStream(file.getBytes());
			Blob blob = bucket.create(fileName, content, file.getContentType());

			return "https://firebasestorage.googleapis.com/v0/b/" + firebaseBucket + "/o/" +
				URLEncoder.encode(fileName, "UTF-8") + "?alt=media";
		} catch (IOException e) {
			throw new InternalException(ErrorCode.FIREBASE_ERROR);
		}
	}
}
