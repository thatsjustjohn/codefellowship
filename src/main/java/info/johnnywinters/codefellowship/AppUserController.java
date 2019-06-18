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
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

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
        return new RedirectView("/login");
    }

    @GetMapping("/users/{id}")
    public String getSpecficUser(@PathVariable long id, Model m) {
        // .get to get value inside of optional
        AppUser user = appUserRepository.findById(id).get();
        System.out.println(user.toString());
        m.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "createUserForm";
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }
}
