package info.johnnywinters.codefellowship;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class CodeFellowShipController {


    @GetMapping("/")
    public String getRoot(Principal p, Model m) {
        System.out.println(p.getName());
        m.addAttribute("principal", p);
        return "home";  //home??
    }


}
