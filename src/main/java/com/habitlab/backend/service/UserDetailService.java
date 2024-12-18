package com.habitlab.backend.service;

import com.habitlab.backend.dto.*;
import com.habitlab.backend.exception.UnauthorizedException;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.RoleEntity;
import com.habitlab.backend.persistance.entity.RoleEnum;
import com.habitlab.backend.persistance.entity.UserEntity;
import com.habitlab.backend.repository.HabitRepository;
import com.habitlab.backend.repository.RoleRepository;
import com.habitlab.backend.repository.UserRepository;
import com.habitlab.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService, IUserDetailService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    /*fixme Change this s**t!!!!*/
    @Autowired
    private IHabitService habitService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${role.default}")
    private String defaultRole;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        /*
        * Transform user entity into UserDetails (understood by Spring Security)
        * */
        user.getRoles()
                .forEach(role -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))
                ));

        user.getRoles().parallelStream()
                .flatMap(role -> role.getPermissions().parallelStream())
                .forEach(permissionEntity -> authorities.add(
                        new SimpleGrantedAuthority(permissionEntity.getName())
                ));
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                authorities
        );
    }

    public AuthResponseDTO login(AuthLoginRequestDTO request){
        String username = request.getUsername();
        String password = request.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponseDTO(username, accessToken);
    }

    public AuthResponseDTO register(AuthRegisterUserRequestDTO request){
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        UserEntity userExists = userRepository.findByUsername(username)
                .orElse(null);

        if(userExists != null){
            throw new UnauthorizedException("User already exists");
        }

        RoleEntity role = roleRepository.findRoleEntityByRole(RoleEnum.valueOf(defaultRole));

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roles(Set.of(role))
                .isEnabled(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .accountNoLocked(true)
                .build();

        UserEntity userCreated = userRepository.save(user);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreated.getRoles().forEach(userRole -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(userRole.getRole().name()))));

        userCreated.getRoles().parallelStream()
                .flatMap(userRole -> userRole.getPermissions().parallelStream())
                .forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(username, userCreated.getPassword(), authorityList);
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        String accessToken = jwtUtils.createToken(auth);

        return new AuthResponseDTO(username, accessToken);
    }

    private Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null){
            throw new UnauthorizedException("Invalid credentials");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public UserProfileDTO getUserProfile(String username){
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<HabitDTO> habits = habitRepository.findAllByUser(user)
                .parallelStream()
                .map(habitService::habitToDTO)
                .toList();

        HabitEntity habitWithMaxStreak = habitRepository.findTopByUserOrderByLastStreakDesc(user)
                .orElse(null);

        int habitsCount = habits.size();

        return UserProfileDTO.builder()
                .username(username)
                .habits(habits)
                .largestStreak(habitWithMaxStreak != null ? habitWithMaxStreak.getLastStreak() : 0)
                .totalHabits(habitsCount)
                .build();
    }

}
