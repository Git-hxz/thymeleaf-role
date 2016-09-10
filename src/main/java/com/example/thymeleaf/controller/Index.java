package com.example.thymeleaf.controller;

import com.example.thymeleaf.common.CurrentUserDetails;
import com.example.thymeleaf.model.User;
import com.example.thymeleaf.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author hxz
 * @since 2016/08/24 19:38
 */
@Log4j
@Controller
public class Index {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RememberMeServices rememberMeServices;

    /**
     * 设置cookie的作用域名
     */
    @Value("${info.domain}")
    private String domain;

    @RequestMapping("/")
    public String login() {
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String loginGet() {
        return "login";
    }

    @RequestMapping("/logout")
    public String logoutGet(HttpServletRequest request, HttpServletResponse response) {
        //清空cookie
        cancelCookie("remember-me", response);
        return "redirect:/login";
    }

    private void cancelCookie(String cookieName, HttpServletResponse response) {

        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    public String registerPOST(Model model,
                               @RequestParam("name") String name,
                               @RequestParam("password") String password,
                               HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole("ROLE_USER");
        user = userRepository.save(user);

        if (user != null) {
            CurrentUserDetails currentUserDetails = new CurrentUserDetails(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUserDetails, currentUserDetails.getPassword(), currentUserDetails.getAuthorities());
            HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
                @Override
                public void addCookie(Cookie cookie) {
                    if (cookie.getDomain() == null) {
                        cookie.setDomain(domain);
                    }
                    cookie.setPath("/");
                    cookie.setMaxAge(3600 * 8);
                    super.addCookie(cookie);
                }
            };
            rememberMeServices.loginSuccess(request, wrapper, authentication);

            model.addAttribute("user", user);
            return "redirect:/index";
        }
        return "redirect:/login";
    }



        @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
        public String loginPost (Model model, @RequestParam("name") String name,
                @RequestParam("password") String password,
                HttpServletRequest request, HttpServletResponse response){
            User user = userRepository.findByNameAndPassword(name, password);
            if (user != null) {
                CurrentUserDetails currentUserDetails = new CurrentUserDetails(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUserDetails, currentUserDetails.getPassword(), currentUserDetails.getAuthorities());
                HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
                    @Override
                    public void addCookie(Cookie cookie) {
                        if (cookie.getDomain() == null) {
                            cookie.setDomain(domain);
                        }
                        cookie.setPath("/");
                        cookie.setMaxAge(3600 * 8);
                        super.addCookie(cookie);
                    }
                };
                rememberMeServices.loginSuccess(request, wrapper, authentication);

                return "redirect:/index";
            } else {
                return "redirect:/login";
            }
        }

        @RequestMapping(value = "/index")
        public String index (@AuthenticationPrincipal User user
                , Model model){
            User user1 = userRepository.findOne(user.getId());
            model.addAttribute("user", user1);
            return "index";
        }

        @RequestMapping(value = "/register")
        public String register () {
            return "register";
        }
    }
