package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.QPost;
import com.helloworld.helloworldweb.domain.QUser;
import com.helloworld.helloworldweb.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.helloworld.helloworldweb.domain.QPost.post;
import static com.helloworld.helloworldweb.domain.QUser.user;

@Repository
public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public UserRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findUserWithPostByEmail(String email) {
        QUser user = QUser.user;
        QPost post = QPost.post;

        User findUser = jpaQueryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .leftJoin(user.posts, post)
                .fetchJoin()
                .distinct()
                .fetchOne();
        Optional<User> result = Optional.ofNullable(findUser);
        return result;
    }
}
