package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDtoWithIsOwner;
import com.helloworld.helloworldweb.dto.Post.PostResponseDtoWithPostComments;
import com.helloworld.helloworldweb.firebase.FirebaseCloudMessageService;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import com.helloworld.helloworldweb.service.FileProcessService;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostCommentService postCommentService;
    private final FileProcessService fileProcessService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/post")
    @Transactional
    public ResponseEntity<ApiResponse<PostResponseDto>> registerPost(@RequestHeader(value = "Auth") String jwtToken,
                                                     @RequestParam(value = "content") String content,
                                                     @RequestParam(value = "category") Category category,
                                                     @RequestParam(value = "title") String title,
                                                     @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                                     @RequestParam(value = "imageUrlArray", required = false) String urls) throws IOException {

        User findUser = userService.getUserByJwt(jwtToken);
        //post 객체 생성
        Post post = Post.builder()
                .category(category)
                .title(URLDecoder.decode(title, "UTF-8"))
                .content(URLDecoder.decode(content, "UTF-8"))
                .tags(URLDecoder.decode(tags, "UTF-8"))
                .solved(false)
                .build();

        String[] storedUrls;
        if(urls != "" && urls != null) {
            String decodeResult = URLDecoder.decode(urls, "UTF-8");
            storedUrls = decodeResult.split(",");
        }
        else{
            storedUrls = null;
        }

        Post savedPost = postService.addPost(post, findUser, storedUrls);
        PostResponseDto responseDto = new PostResponseDto(savedPost);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @PostMapping ("/api/image")
    public ResponseEntity<ApiResponse<String>> getImageUrl(@RequestParam(value = "file") MultipartFile file) {

        String uploadImageUrl = fileProcessService.uploadImage(file);

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                uploadImageUrl), HttpStatus.OK);
    }

    @DeleteMapping ("/api/image")
    public ResponseEntity<ApiResponse> deleteImageUrl(@RequestParam(value = "urls") String urls) throws UnsupportedEncodingException {

        if(urls != ""){
            String decodeResult = URLDecoder.decode(urls, "UTF-8");
            String[] finalUrls = decodeResult.split(",");

            for(String url: finalUrls){
                String storedUrl = URLDecoder.decode(url, "UTF-8");
                System.out.println("storedUrl = " + storedUrl);
                fileProcessService.deleteImage(fileProcessService.getFileName(storedUrl));
            }
        }
        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.DELETE_SUCCESS,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/api/post/blogs")
    //blog 리스트로 썸네일 구성시 사용
    public ResponseEntity<ApiResponse<PostResponseDtoWithIsOwner>> getBlogs(@RequestHeader(value = "Auth") String jwtToken,
                                                                            @RequestParam(value = "email") String email) {
        User findUser = userService.getUserByEmail(email);

        // api 호출한 유저가 게시물의 주인인지 판단
        User caller = userService.getUserByJwt(jwtToken);
        boolean isOwner = caller.getId() == findUser.getId();

        List<Post> blogs = postService.getAllUserPosts(findUser.getId(), Category.BLOG);

        PostResponseDtoWithIsOwner responseDtos = new PostResponseDtoWithIsOwner(blogs, isOwner);

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }
    @GetMapping("/api/post/blogsPage")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getPageBlog(@PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                                                                          @RequestHeader(value = "Auth") String jwtToken,
                                                                          @RequestParam(value = "email") String email) {

        User findUser = userService.getUserByEmail(email);

        // api 호출한 유저가 게시물의 주인인지 판단
        User caller = userService.getUserByJwt(jwtToken);
        boolean isOwner = caller.getId() == findUser.getId();

        List<Post> blogs = postService.getPageUserPosts(findUser.getId(), Category.BLOG, pageable);

        PostResponseDtoWithIsOwner responseDtos = new PostResponseDtoWithIsOwner(blogs, isOwner);

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);

    }

    @GetMapping("/api/post/qnas")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllQna() {

        List<Post> qnas = postService.getAllPost(Category.QNA);

        List<PostResponseDto> responseDtos = qnas.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    @GetMapping("/api/post/qnasPage")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getPageQna(@PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        List<Post> qnas = postService.getPagePosts(Category.QNA, pageable);

        List<PostResponseDto> responseDtos = qnas.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    @GetMapping("/api/search")
    public ResponseEntity<ApiResponse<List<PostRequestDto>>> getSearchedPost(@RequestParam(name="sentence") String sentence){

        List<Post> posts = postService.getSearchedPost(sentence);

        List<PostResponseDto> responseDtos = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    // 조회수 + 1
    @PutMapping("/api/post/views")
    public ResponseEntity<ApiResponse> updatePostViews(@RequestParam(name="post_id")Long id){

        postService.updatePostViews(postService.getPost(id));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS), HttpStatus.OK);
    }

    //프론트에서 게시물 주인에게만 삭제버튼이 생기므로 호출유저 비교 필요 없음.
    @DeleteMapping("/api/post")
    @Transactional
    public ResponseEntity<ApiResponse> deletePost(@RequestParam(value = "post_id") Long postId) {

        Post findPost = postService.getPost(postId);
        postService.deletePost(findPost);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.DELETE_SUCCESS,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/api/post")
    @Transactional
    //Post 하나를 표현할 때 사용.
    public ResponseEntity<ApiResponse> getPost(@RequestHeader(value = "Auth") String jwtToken,
                                               @RequestParam(name = "post_id") Long id) {

        User caller = userService.getUserByJwt(jwtToken);
        Post post = postService.getPost(id);
        postService.updatePostViews(post);
        if(post.isSolved()) {
            List<PostComment> postComments = postCommentService.getPostCommentsInOrder(post);

            PostResponseDtoWithPostComments responseDto = new PostResponseDtoWithPostComments(post, postComments, caller);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);
        }
        else
        {
            //postsubcomment 에서 isOwner를 반환할수 있도록, isOwner를 직접 넘기기보다 caller를 넘겨 post, postsubcomment 작성자와 각각 대조할수 있도록 변경.
            PostResponseDtoWithPostComments responseDto = new PostResponseDtoWithPostComments(post, caller);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);

        }


    }

    @PutMapping("/api/post")
    @Transactional
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(@RequestParam(value = "post_id") Long postId,
                                                                   @RequestParam(value = "content") String content,
                                                                   @RequestParam(value = "title") String title,
                                                                   @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                                                                   @RequestParam(value = "imageUrlArray", required = false) String urls) throws IOException {

        Post targetPost = postService.getPost(postId);

        String[] storedUrls;
        if(urls != "" && urls != null) {
            String decodeResult = URLDecoder.decode(urls, "UTF-8");
            storedUrls = decodeResult.split(",");
        }
        else{
            storedUrls = null;
        }

        Post savedPost = postService.updatePost(targetPost,
                URLDecoder.decode(title, "UTF-8"),
                URLDecoder.decode(content, "UTF-8"),
                URLDecoder.decode(tags, "UTF-8"),
                storedUrls);

        PostResponseDto responseDto = new PostResponseDto(savedPost);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @PostMapping("/api/post/alarm")
    public ResponseEntity<ApiResponse> alarmTest(HttpServletRequest request)
    {
        String FCM = request.getHeader("FCM");
        try{
            firebaseCloudMessageService.sendMessageTo(FCM,"123","abc","000");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);

    }

    @GetMapping("/api/post/top-questions")
    public ResponseEntity<ApiResponse> getTopQuestions(){
        List<Post> findPosts = postService.getTop5Questions();

        List<PostResponseDto> responseDtos = findPosts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }
}

