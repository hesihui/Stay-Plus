package com.staybooking.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.staybooking.exception.GCSUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {
    //read gcs bucket
    @Value("${gcs.bucket}")
    private String bucketName;

    public String save(MultipartFile file) throws GCSUploadException {
        Credentials credentials = null;
        try {
            //read gcp credential
            credentials = GoogleCredentials.fromStream(getClass().getClassLoader().getResourceAsStream("credentials.json"));
        } catch (IOException e) {
            throw new GCSUploadException("Failed to load GCP credentials");
        }

        //build storage object
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        // generate random file names
        // blob: binary large object
        String filename = UUID.randomUUID().toString();
        BlobInfo blobInfo = null;
        // uploading image files  to GCS
        try {
            blobInfo = storage.createFrom(
                    BlobInfo
                            .newBuilder(bucketName, filename)
                            .setContentType("image/jpeg")
                            .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(),
                    file.getInputStream());
        } catch (IOException e) {
            throw new GCSUploadException("Failed to upload images to GCS");
        }
        //return the url of uploaded images
        return blobInfo.getMediaLink();
    }
}