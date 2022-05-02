package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

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
    // Post 검색
    public List<Post> getSearchedPost(String sentence){
        String[] s = sentence.split(" ");
        List<Post> findPosts = new ArrayList<Post>();

        boolean exact_phrase_flag = false;
        String exact_phrase_concat = "";
        // 검색된 문장을 공백 기준으로 모두 검색하여 결과에 추가한다.
        for ( String keyword : s ) {

            // 1. [태그] 검색일 경우 -> %태그% 로 변환하여 요청받음.
            if( keyword.startsWith("%") && keyword.endsWith("%") ) {
                // [tag] -> tag
                String tag = keyword.substring(1,keyword.length()-1);
                Optional<List<Post>> result = postRepository.findAllByTagsContaining(tag);
                addSearchResults(findPosts,result);
            }

            // 2. "정확한 문구" 검색일 경우
            // ex) "react"
            else if( keyword.startsWith("\"") && keyword.endsWith("\"")){
                // "react" -> react
                String exact_phrase = keyword.substring(1,keyword.length()-1);
                Optional<List<Post>> result = postRepository.findAllByTitleContainingOrContentContaining(exact_phrase,exact_phrase);
                addSearchResults(findPosts,result);
            }
            // ex) "react and native" -> "react
            else if( keyword.startsWith("\"")){
                exact_phrase_flag = true;
                exact_phrase_concat += keyword.substring(1);
            }
            // native"
            else if( keyword.endsWith("\"")){
                exact_phrase_concat += " " + keyword.substring(0,keyword.length()-1);
                Optional<List<Post>> result = postRepository.findAllByTitleContainingOrContentContaining(exact_phrase_concat,exact_phrase_concat);
                addSearchResults(findPosts,result);
                // 초기화
                exact_phrase_flag = false;
                exact_phrase_concat = "";
            }
            // and
            else if( exact_phrase_flag ){
                exact_phrase_concat += " " + keyword;
            }

            // n. 일반적인 검색
            else {
                Optional<List<Post>> result = postRepository.findAllByTitleContainingOrContentContaining(keyword, keyword);
                addSearchResults(findPosts,result);
            }
        }
        return findPosts;
    }

    @Transactional
    public void updatePost(Post post){
        post.updateViews();
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Post post) {

        postRepository.delete(post);
    }

    // postList에 검색된 searchList 추가해주는 내장함수
    private void addSearchResults(List<Post> postList,Optional<List<Post>> searchList){
        if(searchList.isPresent()){
            for(Post p : searchList.get()){
                // 중복삽입 방지
                if( !postList.contains(p) ){
                    postList.add(p);
                    p.updateSearchCount();
                }
            }
        }
    }



}
