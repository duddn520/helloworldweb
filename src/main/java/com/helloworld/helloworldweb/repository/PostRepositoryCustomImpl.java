package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.QPost;
import com.helloworld.helloworldweb.domain.QPostImage;
import com.helloworld.helloworldweb.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Optional;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public PostRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Post> findPostWithImagesById(Long postId) {
        QPost post = QPost.post;
        QPostImage postImage = QPostImage.postImage;

        Post findPost = jpaQueryFactory.selectFrom(post)
                .where(post.id.eq(postId))
                .leftJoin(post.postImages, postImage)
                .fetchJoin()
                .distinct()
                .fetchOne();
        Optional<Post> result = Optional.ofNullable(findPost);

        return result;
    }
}
