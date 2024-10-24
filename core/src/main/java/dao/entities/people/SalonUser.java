package dao.entities.people;

import dao.entities.Authority;
import dao.entities.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@EqualsAndHashCode(callSuper = true)

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
public class SalonUser extends SomeOne implements UserDetails {

    //   @NotEmpty
    @Column(name = "user_name")
    private String username;
    //    @Size(min = 8, message = "Password must be at least 6 characters long")
    @Setter
    @Column(name = "password")
    private String password;
    //   @Email
    @Column(name = "email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    List<Authority> authorities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_tokens",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "token_id"))
    List<RefreshToken> tokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authority authority) {
        this.authorities.add(authority);
    }

    @Override
    public String getUsername() {
        return username;
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
