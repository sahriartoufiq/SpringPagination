package com.sahriar.springPagination.service;

import com.sahriar.springPagination.domain.Post;
import com.sahriar.springPagination.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by toufiq on 4/18/18.
 */
public interface UserService {

    void save(User user);


//    @PreAuthorize("hasAnyAuthority('admin', 'use')")
@PreAuthorize("hasAnyAuthority('admin', 'use')")
    Page<User> findAllPageable(Pageable pageable);

    Page<User> findByNamePageable(String name, Pageable pageable);

    void removeUser(Long id);

    void savePost(Post post, String author);

    List<Post> loadAllPost();

    Page<Post> findAllPostPageable(Pageable pageable);

    Page<Post> findAllPostPageableCached(String userName, Pageable pageable);

    Page<Post> findAllPostPageableCached(String name, String userName, Pageable pageable);

    public Future<Page<User>> listAllUsers(Pageable pageable);

    public CompletableFuture<Page<User>> listUsers(Pageable pageable);

}
