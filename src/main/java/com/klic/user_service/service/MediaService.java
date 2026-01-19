package com.klic.user_service.service;

import com.klic.user_service.external.resources.Media;
import com.klic.user_service.external.resources.MediaRepository;
import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;
    private final MinioStorageService minioStorageService;

    @Transactional
    public Media uploadMedia(UUID postId, MultipartFile file, String takenLocationWkt, String uploadedLocationWkt) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String mediaPath = minioStorageService.uploadFile(file);

        Media media = new Media();
        media.setPost(post);
        media.setUser(post.getUser());
        media.setMediaPath(mediaPath);
        
        try {
            WKTReader reader = new WKTReader();
            if (takenLocationWkt != null && !takenLocationWkt.isEmpty()) {
                media.setTakenLocation((Point) reader.read(takenLocationWkt));
            }
            if (uploadedLocationWkt != null && !uploadedLocationWkt.isEmpty()) {
                media.setUploadedLocation((Point) reader.read(uploadedLocationWkt));
            }
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing location WKT", e);
        }

        return mediaRepository.save(media);
    }
}
