package org.orderpaymentsystem.security.service;

import java.util.HashSet;
import java.util.List;

import org.orderpaymentsystem.exceptions.GenericException;
import org.orderpaymentsystem.exceptions.ResourceNotFoundException;
import org.orderpaymentsystem.security.entity.Role;
import org.orderpaymentsystem.security.entity.User;
import org.orderpaymentsystem.security.model.UserDTO;
import org.orderpaymentsystem.security.repository.RoleRepository;
import org.orderpaymentsystem.security.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final private UserRepository repo;
    
    final private RoleRepository roleRepo;
    
    final private BCryptPasswordEncoder encoder;
    
    public UserService(UserRepository repo, 
        RoleRepository roleRepo,
        BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }
    public void saveUser(UserDTO dto) {
        User user = new User();
        mapDtoToEntityUser(user, dto);
        User checkUser = repo.findByUsername(user.getUsername());
        if(checkUser!=null){
            throw new GenericException("Registration Failed: User ["+user.getUsername()+"], already exists.");
        }
        repo.save(user);
    }

    private void mapDtoToEntityUser(User user, UserDTO userDto) {
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        List<Role> roleList = userDto.getRoles().stream()
                .map(role-> 
                    roleRepo.findByName(role)
                    .orElseThrow(()->
                        new ResourceNotFoundException("Role not found "+role))
                ).toList();
        user.setRoles(new HashSet<Role>(roleList));
    }
}
