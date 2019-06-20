package info.johnnywinters.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @GetMapping("/")
    public String getRoot(Principal p, Model m) {

        m.addAttribute("principal", p);
        if(p != null) {
            AppUser currentUser = appUserRepository.findByUsername(p.getName());
            m.addAttribute("loggedInUser", currentUser);
            m.addAttribute("hasNoPosts", currentUser.posts.isEmpty());
        }
        return "home";
    }


    @GetMapping("/posts/add")
    public String getNewPost(Principal p, Model m){
        m.addAttribute("principal", p);
        return "createPostForm";
    }

    @PostMapping("/posts")
    public RedirectView addPost(String body, Principal p){
        Post post = new Post();
        post.body = body;
        post.creator = appUserRepository.findByUsername(p.getName());
        postRepository.save(post);
        return new RedirectView("/");
    }




    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    class PostDoesNotBelongToYourException extends RuntimeException {
        public PostDoesNotBelongToYourException(String s){
            super(s);
        }
    }
}
