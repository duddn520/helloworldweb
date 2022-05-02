package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostImage;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileProcessService fileProcessService;

    @Transactional
    //Post 한 개 조회
    //Post 조회, 수정, 삭제 시에 사용.
    public Post getPost(Long postId) {

        Post findPost = postRepository.findById(postId).orElseThrow((()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")));

        return findPost;
    }

    @Transactional
    public Post addPost(Post post, User user) {

        post.updateUser(user);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    //AWS S3에 파일을 업로드 하는 서비스
    //controller로 부터 Post 객체, User 객체, front에서 받은 file을 받음
    @Transactional
    public void addPostWithImage(Post post, User user, List<MultipartFile> files) {

        //Post와 User 연관관계 맺어줌
        post.updateUser(user);

        for(MultipartFile file : files){

            //Aws S3 file server에 file을 업로드
            String uploadImageUrl = fileProcessService.uploadImage(file);

            PostImage postImage = PostImage.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(fileProcessService.getFileName(uploadImageUrl))
                    .storedUrl(uploadImageUrl)
                    .fileSize(file.getSize())
                    .build();

            //Post 와 PostImage의 연관관계 맺어줌
            postImage.updatePost(post);
        }

        postRepository.save(post);

    }

    //로컬에 저장할 때 사용한 함수 -> S3를 사용하는 현재 필요없음.
    @Transactional
    public void addPostWithImageForLocal(Post post, User user, List<MultipartFile> files) throws IOException {

        //Post와 User 연관관계 맺어줌
        post.updateUser(user);

        // 파일을 저장할 경로(로컬)를 지정
        String absolutePath = new File("").getAbsolutePath() + "/";
        File tempFile = new File(absolutePath+"images/");
        // 저장할 위치의 디렉토리가 존지하지 않을 경우
        if(!tempFile.exists()){
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            tempFile.mkdirs();
        }

        for(MultipartFile file : files){
            //파일 이름이 같으면 안되므로 uuid 사용하여 새로운 이름을 만들어 줌
            UUID uuid = UUID.randomUUID();
            //새로운 이름 = uuid_원래이름
            String newFileName = uuid+"_"+file.getOriginalFilename();

            PostImage postImage = PostImage.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(newFileName)
                    .storedUrl(newFileName) //로컬에 저장할 떄는 일단 url 없음
                    .fileSize(file.getSize())
                    .build();

            postImage.updatePost(post);

            //path에 해당하는 File 객체를 만들어줌
            File newFile = new File(absolutePath+"images/"+newFileName);
            //지정된 경로에 Frontend에서 받은 file 저장
            file.transferTo(newFile);
        }

        postRepository.save(post);
    }

    @Transactional
    //사용자 본인의 게시물만 전체조회
    //userId를 매개변수로 받음.
    public List<Post> getAllUserPosts(Long userId, Category category) {

        List<Post> posts = postRepository.findByUserIdAndCategory(userId, category).orElseGet(() -> new ArrayList<>());

        return posts;
    }

    @Transactional
    //모든 Post 조회
    //카테고리를 매개변수로 받음.
    public List<Post> getAllPost(Category category){

        List<Post> posts = postRepository.findByCategory(category).orElseGet(() -> new ArrayList<>());

        return posts;
    }

    @Transactional
    public void deletePost(Post post) {

        List<PostImage> postImages = post.getPostImages();

        //삭제할 post의 postImage들도 Aws S3에서 함께 삭제.
        for(PostImage postImage : postImages){
            String targetName = postImage.getStoredFileName();
            fileProcessService.deleteImage(targetName);
        }
        postRepository.delete(post);
    }



}
