package mk.ukim.finki.wp.kol2022.g3.service.impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.repository.InterestRepository;
import mk.ukim.finki.wp.kol2022.g3.service.InterestService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InterestServiceImpl implements InterestService, UserDetailsService {

    private final ForumUserRepository forumUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final InterestRepository interestRepository;

    public InterestServiceImpl(ForumUserRepository forumUserRepository, PasswordEncoder passwordEncoder, InterestRepository interestRepository) {
        this.forumUserRepository = forumUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.interestRepository = interestRepository;
    }


    @Override
    public Interest findById(Long id) {
        return this.interestRepository.getById(id);

    }

    @Override
    public List<Interest> listAll() {
        return this.interestRepository.findAll();
    }

    @Override
    public Interest create(String name) {

        return interestRepository.save(new Interest(name));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ForumUser forumUser = forumUserRepository.findForumUserByEmail(email).orElseThrow(() ->new UsernameNotFoundException("badCredentials"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(forumUser.getEmail(),
                forumUser.getPassword() ,
                Stream.of(new SimpleGrantedAuthority("ROLE_"+ forumUser.getType().toString())).collect(Collectors.toList()));
        return userDetails;
    }
}
