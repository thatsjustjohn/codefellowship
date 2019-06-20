package info.johnnywinters.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Controller
public class AppUserController {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AppUserRepository appUserRepository;

    @PostMapping("/users")
    public RedirectView createUser(String username, String password, String firstname, String lastname, String dateOfBirth, String bio){
        AppUser newUser = new AppUser(username, bCryptPasswordEncoder.encode(password), firstname, lastname, dateOfBirth, bio);
        appUserRepository.save(newUser);
        System.out.println(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    @PostMapping("/users/{id}/follow")
    public RedirectView addFollower(@PathVariable Long id, Long follower, Principal p, Model m){
        // we have the ID of who to follow
        AppUser current = appUserRepository.findByUsername(p.getName());
        AppUser whoToFollow = appUserRepository.findById(id).get();
        // make them be friends
        current.followers.add(whoToFollow);
        whoToFollow.followees.add(current);
        appUserRepository.save(current);
        appUserRepository.save(whoToFollow);
        // redirect back to the person we followed
        return new RedirectView("/users/" + id);
    }

    @GetMapping("/users")
    public String getUsersPage(Principal p, Model m){
        Iterable<AppUser> users = appUserRepository.findAll();
        m.addAttribute("principal", p);
        m.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false, defaultValue = "") String showMessage, Model m) {
        m.addAttribute("shouldShowExtraMessage", !showMessage.equals(""));
        return "login";
    }

    @GetMapping("/users/{id}")
    public String getSpecficUser(@PathVariable long id,Principal p, Model m) {
        // .get to get value inside of optional
        AppUser current = appUserRepository.findByUsername(p.getName());
        AppUser user = appUserRepository.findById(id).get();
        m.addAttribute("principal", p);
        m.addAttribute("myProfile", p.getName().equals(user.username));
        m.addAttribute("isFollowed", current.followers.contains(user));
        System.out.println(current.followers.toString());
        m.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/myprofile")
    public String getProfile(Principal p, Model m) {
        AppUser user = appUserRepository.findByUsername(p.getName());
        m.addAttribute("principal", p);
        m.addAttribute("user", user);
        m.addAttribute("hasNoPosts", user.posts.isEmpty());
        return "myprofile";
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "createUserForm";
    }


    @GetMapping("/home")
    public String getHomePage(Principal p, Model m) {
        m.addAttribute("principal", p);
        return "home";
    }

    @GetMapping("logout_success")
    public String getLogout(){
        return "logout";
    }
}
