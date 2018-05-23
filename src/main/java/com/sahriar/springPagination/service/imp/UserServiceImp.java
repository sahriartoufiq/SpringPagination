package com.sahriar.springPagination.service.imp;

import com.sahriar.springPagination.domain.Post;
import com.sahriar.springPagination.domain.Privilege;
import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.domain.UserRole;
import com.sahriar.springPagination.repository.MyBaseRepo;
import com.sahriar.springPagination.repository.PostRepo;
import com.sahriar.springPagination.repository.UserRepo;
import com.sahriar.springPagination.repository.UserRoleRepo;
import com.sahriar.springPagination.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by toufiq on 4/18/18.
 */
@Service
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
    @PreAuthorize("'Sylhet' == principal.district")
    public List<Post> loadAllPost() {
        return postRepo.findAll();
    }

    @Transactional(readOnly = true)
    //   @PostFilter("filterObject.name = authentication.name")
    //  @PostAuthorize("'Dhaka' == principal.district")
    public Page<Post> findAllPostPageable(Pageable pageable) {
//        return postRepo.findByPostTitle("got", pageable);
        return postRepo.findAll(pageable);
    }


}
