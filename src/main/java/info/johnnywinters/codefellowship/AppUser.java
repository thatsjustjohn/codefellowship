package info.johnnywinters.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class AppUser implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(unique = true)
    String username;
    String password;
    String firstname;
    String lastname;
    String dateOfBirth;
    String bio;

    @OneToMany(mappedBy = "creator")
    List<Post> posts;

    @ManyToMany
    Set<AppUser> followers;

    @ManyToMany
    Set<AppUser> followees;

    public AppUser(){}


    public AppUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public AppUser(String username, String password, String firstname, String lastname, String dateOfBirth, String bio){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    // Getters
    public long getId() { return this.id; }

    public String getFirstname() { return this.firstname; }

    public String getLastname() {
        return this.lastname;
    }

    public String getBio() {
        return this.bio;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public List<Post> getPosts() { return this.posts; }

    public Set<AppUser> getFollowees() { return this.followees; }

    public Set<AppUser> getFollowers() { return this.followers; }


    // Other Getters
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
