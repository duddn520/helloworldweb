package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public PostRepositorySupport(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    public PageImpl<Post> findCustomSearchResultsWithPagination(String sentence, Pageable pageable) {
        ArrayList<String> phrase_list = new ArrayList<>();
        ArrayList<String> tag_list = new ArrayList<>();
        ArrayList<String> exact_phrase_list = new ArrayList<>();

        boolean exact_phrase_flag = false;
        String exact_phrase_concat = "";
        for ( String keyword : sentence.split(" ") ) {
            // 1. [태그] 검색일 경우 -> %태그% 로 변환하여 요청받음.
            if( keyword.startsWith("%") && keyword.endsWith("%") ) {
                // [tag] -> tag
                tag_list.add( keyword.substring(1,keyword.length()-1) );
            }

            // 2. "정확한 문구" 검색일 경우
            // ex) "react"
            else if( keyword.startsWith("\"") && keyword.endsWith("\"")){
                // "react" -> react
                exact_phrase_list.add( keyword.substring(1,keyword.length()-1) );
            }
            // ex) "react and native" -> "react
            else if( keyword.startsWith("\"")){
                exact_phrase_flag = true;
                exact_phrase_concat += keyword.substring(1);
            }
            // native"
            else if( keyword.endsWith("\"")){
                exact_phrase_list.add( keyword.substring(1,keyword.length()-1) );
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
                phrase_list.add(keyword);
            }
        }

        BooleanBuilder searchBuilder = new BooleanBuilder();

        QPost post = QPost.post;

        // 태그
        for( String tag : tag_list ){
            searchBuilder.or(QPost.post.tags.contains(tag));
        }
        for( String exact_phrase : exact_phrase_list ){
            searchBuilder.or(QPost.post.title.contains(exact_phrase));
            searchBuilder.or(QPost.post.content.contains(exact_phrase));
        }
        for( String phrase : phrase_list ){
            searchBuilder.or(QPost.post.title.contains(phrase));
            searchBuilder.or(QPost.post.content.contains(phrase));
        }

        JPAQuery<Post> postJPAQuery = jpaQueryFactory.selectFrom(QPost.post)
                .where(searchBuilder)
                .orderBy(QPost.post.createdTime.desc());

        long totalCount = postJPAQuery.stream().count();
        List<Post> results = getQuerydsl().applyPagination(pageable, postJPAQuery).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }
}
