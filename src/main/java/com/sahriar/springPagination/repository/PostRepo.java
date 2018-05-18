package com.sahriar.springPagination.repository;

import com.sahriar.springPagination.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepo extends MyBaseRepo<Post, Long> {

    @Query("SELECT entity FROM Post  entity " +
            "WHERE entity.postTitle = :title " +
            "AND entity.author.userName = :#{principal.username}")
    Page<Post> findByPostTitle(@Param("title") String postTitle, Pageable pageable);


    @Query("SELECT entity FROM Post  entity WHERE entity.author.userName = :#{principal.username}")
    Page<Post> findAll(Pageable pageable);
}
