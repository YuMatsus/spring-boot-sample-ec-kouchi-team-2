package com.example.springbootsampleec.security; 
 
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.repositories.UserRepository;
 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
public class SimpleUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
 
    public SimpleUserDetailsService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }
 
    // メールアドレスを指定して、ユーザー情報を取得
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        assert(email != null);
        Optional<User> user = userRepository.findByEmail(email);
        entityManager.refresh(user.get());
        return user.map(SimpleLoginUser::new)
            .orElseThrow();
    }
}