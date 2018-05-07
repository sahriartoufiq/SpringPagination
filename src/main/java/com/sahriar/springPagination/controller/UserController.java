package com.sahriar.springPagination.controller;

import com.sahriar.springPagination.Storage.StorageService;
import com.sahriar.springPagination.custom.DataAccess;
import com.sahriar.springPagination.custom.GenericDao;
import com.sahriar.springPagination.domain.Pager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            }catch (Exception e){
                log.debug("error occured");
            }
        }

        return "redirect:/userList";
    }

    @GetMapping("/userList")
    public ModelAndView listAllUser(@RequestParam("pageSize") Optional<Integer> pageSize,
                                    @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("userList");

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<User> users = userService.findAllPageable(new PageRequest(evalPage, evalPageSize));
        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("users", users);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;

    }

    @GetMapping(value = "/userList", params = {"remove"})
    public String removeUser(@RequestParam("pageSize") Optional<Integer> pageSize,
                             @RequestParam("page") Optional<Integer> page, HttpServletRequest req, Model model) {
        Long rowId = Long.valueOf(req.getParameter("remove"));

        log.debug("row->" + rowId);
        userService.removeUser(rowId);

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<User> users = userService.findAllPageable(new PageRequest(evalPage, evalPageSize));

        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);
        model.addAttribute("users", users);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);

        return "userList";

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

    @GetMapping("/403")
    public String error403(Model model) {
        return "403";
    }
}
