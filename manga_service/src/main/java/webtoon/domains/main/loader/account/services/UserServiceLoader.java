package webtoon.domains.main.loader.account.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webtoon.account.repositories.IAuthorityRepository;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.impl.UserServiceImpl;

//@Service
public class UserServiceLoader extends UserServiceImpl {
    public UserServiceLoader(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAuthorityRepository authorityRepository) {
        super(passwordEncoder, userRepository, authorityRepository);
    }
}
