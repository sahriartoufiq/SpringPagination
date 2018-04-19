package com.sahriar.springPagination.controller;

import com.sahriar.springPagination.domain.Pager;
import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.service.UserService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    UserService userService;

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

    @GetMapping({"/", "/regUser"})
    public String index(Model model) {

        model.addAttribute("message", "Hello Spring MVC 5!");
        return "index";
    }

    @PostMapping("/addUser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "index";
        } else {
            userService.save(user);
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
       // Page<User> users = userService.findByNamePageable("toufiq", new PageRequest(evalPage, evalPageSize));

        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);

        log.debug("...........................");
        modelAndView.addObject("users", users);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        return modelAndView;

    }
}
