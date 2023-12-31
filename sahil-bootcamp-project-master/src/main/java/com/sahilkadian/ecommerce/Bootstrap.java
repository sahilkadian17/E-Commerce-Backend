package com.sahilkadian.ecommerce;

import com.sahilkadian.ecommerce.entities.Role;
import com.sahilkadian.ecommerce.entities.User;
import com.sahilkadian.ecommerce.repositories.RoleRepository;
import com.sahilkadian.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Set;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${system.username}")
    private String username;

    @Value("${role.admin}")
    private String ADMIN;

    @Value("${role.seller}")
    private String SELLER;

    @Value("${role.customer}")
    private String CUSTOMER;

    @Override
    public void run(String... args) throws Exception {
        if(!roleRepository.existsByAuthority(ADMIN)) {
            Role role1 = new Role();
            role1.setAuthority(ADMIN);
            roleRepository.save(role1);
        }
        if(!roleRepository.existsByAuthority(SELLER)) {
            Role role2 = new Role();
            role2.setAuthority(SELLER);
            roleRepository.save(role2);
        }
        if(!roleRepository.existsByAuthority(CUSTOMER)) {
            Role role3 = new Role();
            role3.setAuthority(CUSTOMER);
            roleRepository.save(role3);
        }
        if(!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setFirstName("Admin");
            user.setMiddleName("");
            user.setLastName("");
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);
            user.setInvalidAttemptCount(0L);
            user.setPasswordUpdateDate(new Date());

            Role role = roleRepository.findByAuthority(ADMIN).get();
            user.setRoles(Set.of(role));
            user.setCreatedBy(username);
            user.setUpdatedBy(username);
            userRepository.save(user);
        }
    }
}
