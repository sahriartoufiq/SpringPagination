package com.sahriar.springPagination.service.imp;

import com.sahriar.springPagination.domain.*;
import com.sahriar.springPagination.repository.MyBaseRepo;
import com.sahriar.springPagination.repository.PostRepo;
import com.sahriar.springPagination.repository.UserRepo;
import com.sahriar.springPagination.repository.UserRoleRepo;
import com.sahriar.springPagination.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by toufiq on 4/18/18.
 */
@Service
//@CacheConfig(cacheNames = "users")
public class UserServiceImp implements UserService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    public int count = 0;

    ReentrantLock lock = new ReentrantLock();

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserRoleRepo userRoleRepo;

    @Autowired
    PostRepo postRepo;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public void save(User user) {
        user.setEncodedPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        UserRole userRole = null;

//        Set<Privilege> privileges = new HashSet<>();
//        for(Privilege privilege : privileges){
//            privilege.setName("read");
//        }

        for (String role : user.getUserRoles()) {
            userRole = new UserRole();
            userRole.setUser(user);


            userRole.setUserRoles(role);
            userRoleRepo.save(userRole);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable("users")
    //   @PostFilter("filterObject.name = authentication.name")
    //  @PostAuthorize("'Dhaka' == principal.district")
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByNamePageable(String name, Pageable pageable) {
        return userRepo.findByName(name, pageable);
    }


    @Override
    @Transactional
    public void removeUser(Long id) {

        User user = userRepo.getOne(id);

        log.debug(user.getUserName());
        log.debug(user.getUserRoleSet() + "");

        //userRoleRepo.deleteAll(user.getUserRoleSet());
        userRepo.delete(user);

    }

//    public String getUserName(){
//        return ((CustomUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
//    }

    @Override
    @Transactional
    public void savePost(Post post, String author) {
        User user = userRepo.findByUserName(author);
        post.setAuthor(user);
        postRepo.save(post);
    }

    @Override
//    @PostFilter("filterObject.author.userName == authentication.name")
//    @PostFilter("'Dhaka' == principal.district")


    @Cacheable("postsAll")
    //@PreAuthorize("'Sylhet' == principal.district")
    public List<Post> loadAllPost() {
        return postRepo.findAll();
    }

    @Transactional(readOnly = true)
    //   @PostFilter("filterObject.name = authentication.name")
    //  @PostAuthorize("'Dhaka' == principal.district")


    //   @Cacheable("posts")
    @Cacheable(value = "postList")
    public Page<Post> findAllPostPageable(Pageable pageable) {
//        return postRepo.findByPostTitle("got", pageable);

        if (((CustomUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername().equals("sahriar")) {
            return postRepo.findAll(pageable);
        } else {
            return postRepo.findByPostTitle("got", pageable);
        }
    }

    //   @Cacheable("posts")
    @Cacheable(value = "posts", key = "#userName")
    public Page<Post> findAllPostPageableCached(String userName, Pageable pageable) {
//        return postRepo.findByPostTitle("got", pageable);

        if (((CustomUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername().equals("sahriar")) {
            return postRepo.findAll(pageable);
        } else {
            return postRepo.findByPostTitle("Mis", pageable);
        }
    }

    @Cacheable(value = "postCache", key = "#userName")
    public Page<Post> findAllPostPageableCached(String name, String userName, Pageable pageable) {
//        return postRepo.findByPostTitle("got", pageable);

        if (((CustomUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername().equals("sahriar")) {
            return postRepo.findAll(pageable);
        } else {
            return postRepo.findByPostTitle("got", pageable);
        }
    }


    @Async
    public Future<Page<User>> listAllUsers(Pageable pageable){

        return new AsyncResult<Page<User>>(userRepo.findAll(pageable));

    }



    @Async
    public CompletableFuture<Page<User>> listUsers(Pageable pageable){
        CompletableFuture<Page<User>> completableFuture =  CompletableFuture.supplyAsync(() -> userRepo.findAll(pageable));
        return completableFuture;
    }


}
