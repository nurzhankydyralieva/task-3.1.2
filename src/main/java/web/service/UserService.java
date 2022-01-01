package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.entity.Role;
import web.entity.User;
import web.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailsServiceImpl")
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @PostConstruct
    public void addInitUsers() {
        roleService.saveRole(new Role("ROLE_USER"));
        roleService.saveRole(new Role("ROLE_ADMIN"));

        Set<Role> roleSet = new HashSet<>();
        User user;

        roleSet.add(roleService.getRole("ROLE_USER"));
        //password -> user
        user = new User("USER", "USEROV", 22, "user@gmail.com", "$2a$12$K20M/E924j7vX4Mh8ligE.4sXfpp08OynpoOeG290kHyr/sss1ipS");
        user.getRoles().addAll(roleSet);
        userRepository.save(user);


        roleSet.add(roleService.getRole("ROLE_ADMIN"));
        //password -> admin
        user = new User("ADMIN", "ADMINOV", 23, "admin@gmail.com", "$2a$12$/U8PG2iMi8QMLftAsjHISOGcVrHad3ec1k6B2.TeurP6my2QO7z.q");
        user.getRoles().addAll(roleSet);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(username);
    }

    public User getUserByName(String s) {
        return userRepository.getUserByEmail(s);
    }
}
