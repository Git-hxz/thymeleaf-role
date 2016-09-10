package com.example.thymeleaf.common;

import com.example.thymeleaf.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * current user details
 *
 * @author hxz
 */
public class CurrentUserDetails extends User implements UserDetails {

    private List<GrantedAuthority> grantedAuthorities;

    public CurrentUserDetails(User user) {
        setId(user.getId());
        setName(user.getName());
        setPassword(user.getPassword());
        grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public String getUsername() {
        return String.valueOf(getId());
    }


    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
