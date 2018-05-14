package com.sahriar.springPagination.service;

import com.sahriar.springPagination.domain.Post;
import com.sahriar.springPagination.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by toufiq on 4/18/18.
 */
public interface UserService {

    void save(User user);

    Page<User> findAllPageable(Pageable pageable);

    Page<User> findByNamePageable(String name, Pageable pageable);

    void removeUser(Long id);

    void savePost(Post post, String author);

    List<Post> loadAllPost();

}
