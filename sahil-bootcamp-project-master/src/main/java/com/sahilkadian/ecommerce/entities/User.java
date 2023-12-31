package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditing implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private boolean isDeleted;
    private boolean isActive;
    private boolean isExpired;
    private boolean isLocked;
    private Long invalidAttemptCount=0L;
    private Date passwordUpdateDate;
    private String activationToken;
    private Date activationTokenExpiry;
    private String resetPasswordToken;
    private Date resetPasswordTokenExpiry;

    @Transient
    @Column
    @ToString.Exclude
    List<GrantedAuthority> grantAuthorityList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
