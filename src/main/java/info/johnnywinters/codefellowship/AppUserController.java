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
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false, defaultValue = "") String showMessage, Model m) {
        m.addAttribute("shouldShowExtraMessage", !showMessage.equals(""));
        return "login";
    }

    @GetMapping("/users/{id}")
    public String getSpecficUser(@PathVariable long id, Model m) {
        // .get to get value inside of optional
        AppUser user = appUserRepository.findById(id).get();
        System.out.println(user.toString());
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
