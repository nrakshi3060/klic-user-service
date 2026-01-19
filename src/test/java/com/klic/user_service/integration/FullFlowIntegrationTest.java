package com.klic.user_service.integration;

import com.klic.user_service.model.MediaResponse;
import com.klic.user_service.model.Post;
import com.klic.user_service.model.PostLike;
import com.klic.user_service.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FullFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static UUID userId;
    private static UUID postId;

    @Test
    @Order(1)
    void createUser() {
        User user = new User();
        user.setFirstName("Integration");
        user.setLastName("Test");
        user.setUsername("int_user_" + System.currentTimeMillis());
        user.setEmail("int_" + System.currentTimeMillis() + "@test.com");
        user.setSignupLocation("POINT(-74.0060 40.7128)");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        userId = response.getBody().getId();
        assertEquals("POINT (-74.006 40.7128)", response.getBody().getSignupLocation());
    }

    @Test
    @Order(2)
    void createPost() {
        assertNotNull(userId, "User should have been created in step 1");

        Post post = new Post();
        post.setUserId(userId);
        post.setPostDescription("Integration test post");
        post.setPostType(Post.PostTypeEnum.TEXT);

        ResponseEntity<Post> response = restTemplate.postForEntity("/posts", post, Post.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        postId = response.getBody().getId();
    }

    @Test
    @Order(3)
    void uploadMedia() {
        assertNotNull(postId, "Post should have been created in step 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource("test image content".getBytes()) {
            @Override
            public String getFilename() {
                return "test.jpg";
            }
        });
        body.add("takenLocation", "POINT(30 10)");
        body.add("uploadedLocation", "POINT(31 11)");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<MediaResponse> response = restTemplate.postForEntity(
                "/posts/" + postId + "/media",
                requestEntity,
                MediaResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMediaId());
        assertNotNull(response.getBody().getMediaPath());
    }

    @Test
    @Order(4)
    void likePost() {
        assertNotNull(userId, "User should have been created");
        assertNotNull(postId, "Post should have been created");

        PostLike like = new PostLike();
        like.setUserId(userId);
        like.setPostId(postId);
        like.setIsLike(true);

        ResponseEntity<PostLike> response = restTemplate.postForEntity("/post-likes", like, PostLike.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getIsLike());
    }
    
    @Test
    @Order(5)
    void getPostWithDetails() {
        ResponseEntity<Post> response = restTemplate.getForEntity("/posts/" + postId, Post.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
