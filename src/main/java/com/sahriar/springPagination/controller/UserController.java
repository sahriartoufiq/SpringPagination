package com.sahriar.springPagination.controller;

import com.sahriar.springPagination.Storage.StorageService;
import com.sahriar.springPagination.custom.DataAccess;
import com.sahriar.springPagination.custom.GenericDao;
import com.sahriar.springPagination.domain.CustomUser;
import com.sahriar.springPagination.domain.Pager;
import com.sahriar.springPagination.domain.Post;
import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.repository.MyBaseRepo;
import com.sahriar.springPagination.repository.UserRepo;
import com.sahriar.springPagination.service.UserService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by toufiq on 4/18/18.
 */
@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};
    public int counter = 0;

//    @Autowired
//    HazelcastInstance hazelcastInstance;

    @Autowired
    UserService userService;

    @Autowired
    StorageService storageService;

    @DataAccess(entity = User.class)
    private GenericDao userDao;

    @ModelAttribute("userRoles")
    public List<String> getUserRoles() {
        List<String> userRoles = new ArrayList<String>();
        userRoles.add("admin");
        userRoles.add("staff");
        userRoles.add("user");
        return userRoles;
    }

    @ModelAttribute("today")
    public Date getNewDate() {
        return new Date();
    }


    @ModelAttribute("user")
    public User formBackingObject() {
        return new User();
    }

    @GetMapping({"/"})
    public String index(Model model) {

        log.debug(userDao.getEntityname());

        return "index";
    }

    @GetMapping({"/addUser"})
    public String addUser(Model model) {

        return "addUser";
    }

    @PostMapping("/addUser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "addUser";
        } else {
            try {
                storageService.store(user.getPic());
                user.setPicLocation(user.getPic().getOriginalFilename());
                userService.save(user);
            } catch (Exception e) {
                log.debug("error occured.....");
            }
        }

        return "redirect:/userList";
    }

    @GetMapping("/userList")
    // @PreAuthorize("'Dhaka' == principal.district")
    public ModelAndView listAllUser(@RequestParam("pageSize") Optional<Integer> pageSize,
                                    @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("userList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

     //   Future<Page<User>> asynchPages = userService.listAllUsers(new PageRequest(evalPage, evalPageSize));

        //  Page<User> pages = userService.findAllPageable(new PageRequest(evalPage, evalPageSize));

        Page<User> pages = null;


//        if (!asynchPages.isDone()) {
//            try {
//
//                Thread.sleep(20L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (asynchPages.isDone()) {
//            try {
//                log.debug(".....................");
//                pages = asynchPages.get();
//                setPageable(modelAndView, pages, evalPageSize);
//
//                log.debug("............executed");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        // Page<User> pages = asynchPages.

//        setPageable(modelAndView, pages, evalPageSize);


          CompletableFuture<Page<User>> cmPages = userService.listUsers(new PageRequest(evalPage, evalPageSize));

        //CompletableFuture<Page<User>> cm = cmPages.thenApply((s) -> {});

        CompletableFuture<Page<User>>  pa = cmPages.thenApply((s) -> {
            Page<User> u = s;
            setPageable(modelAndView, u, evalPageSize);
            return null;
        });



//        log.debug(".....................");
//        try {
//            pages = asynchPages.get();
//            setPageable(modelAndView, pages, evalPageSize);
//
//            log.debug("............executed");
//        }catch (Exception e){
//            log.debug(e.getMessage());
//        }


        CompletableFuture<User> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            User s = new User();
            s.setName("work");
            return s;
        });

        CompletableFuture<User> future = completableFuture
                .thenApply(s -> {
                    s.setName(s.getName() + "ed");
                    return s;
                });

        try {
            log.debug(future.get().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            pa.get();
        }catch (Exception e){
            log.debug(e.getMessage());
        }

        return modelAndView;


    }


    public void setPageable(ModelAndView modelAndView, Page<?> pages, int evalPageSize) {
        Pager pager = new Pager(pages.getTotalPages(), pages.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("pages", pages);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
    }

    @GetMapping(value = "/userList", params = {"remove"})
    public String removeUser(HttpServletRequest req) {
        Long rowId = Long.valueOf(req.getParameter("remove"));

        log.debug("row->" + rowId);
        userService.removeUser(rowId);
        return "redirect:/userList";

    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth instanceof AnonymousAuthenticationToken)) {

            if (error != null) {
                model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
            }
            if (logout != null) {
                model.addObject("msg", "You've been logged out successfully.");
            }
            model.setViewName("login");
        } else {
            model.setViewName("redirect:/");
        }

        return model;

    }

    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession().getAttribute(key);

        String error;
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }

        return error;
    }


    @GetMapping("/post")
    public String post(Model model) {
        model.addAttribute("post", new Post());
        return "post/post";
    }

    @PostMapping("/post")
    public String post(@Valid @ModelAttribute("post") Post post, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "post";
        } else {


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String authorName = auth.getName();
            userService.savePost(post, authorName);

        }
        return "redirect:/postList";
    }


    @GetMapping("/postList")
    public ModelAndView listAllPost(@RequestParam("pageSize") Optional<Integer> pageSize,
                                    @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("post/postList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

//        Page<Post> pages = userService.findAllPostPageable(new PageRequest(evalPage, evalPageSize));
//        modelAndView.addObject("posts", pages);

        String userName = ((CustomUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Page<Post> pages = userService.findAllPostPageableCached(userName, new PageRequest(evalPage, evalPageSize));
        modelAndView.addObject("posts", pages);

        setPageable(modelAndView, pages, evalPageSize);
//
//        List<Post> posts = userService.loadAllPost();
//
//        modelAndView.addObject("posts", posts);

        return modelAndView;

    }


    // @GetMapping()
}
