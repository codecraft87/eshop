package org.orderpaymentsystem.security.service;

import java.util.HashSet;
import java.util.List;

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
        
        repo.save(user);
    }
    
    private void mapDtoToEntityUser(User usr, UserDTO dto) {
        usr.setUsername(dto.getUsername());
        usr.setPassword(encoder.encode(dto.getPassword()));
        List<Role> roleList = dto.getRoles().stream()
                .map(role-> roleRepo.findByName(role)).toList();
                usr.setRoles(new HashSet<Role>(roleList));
    }
}
