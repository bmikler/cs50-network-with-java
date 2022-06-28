package pl.cs50.network.model.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.cs50.network.model.post.Post;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "author")
    private List<Post> posts;
    @Getter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> followings;
    @Getter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> followers;

    private boolean enabled;
    private boolean locked;

    public User(String username, String password, List<Post> posts, Set<User> followings, Set<User> followers, boolean enabled, boolean locked) {
        this.username = username;
        this.password = password;
        this.posts = posts;
        this.followings = followings;
        this.followers = followers;
        this.enabled = enabled;
        this.locked = locked;
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public Set<User> getFollowings() {
        return Collections.unmodifiableSet(followings);
    }

    public Set<User> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public void addFollower(User user) {
        followers.add(user);
    }

    public void removeFollower(User user) {
        followers.remove(user);
    }

    public void addFollowing(User user) {
        followings.add(user);
    }

    public void removeFollowing(User user) {
        followings.remove(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && enabled == user.enabled && locked == user.locked && username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, enabled, locked);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
