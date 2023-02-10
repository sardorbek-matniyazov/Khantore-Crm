package khantorecrm.config;

import khantorecrm.model.Balance;
import khantorecrm.model.Role;
import khantorecrm.model.User;
import khantorecrm.model.enums.RoleName;
import khantorecrm.repository.UserRepository;
import khantorecrm.security.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

//@Component
@Slf4j
@Component
public class Creator implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public Creator(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        final User save = userRepository.save(
                new User(
                        "admin",
                        "phone",
                        passwordEncoder.encode("admin"),
                        jwtProvider.generateToken("admin"),
                        new Role(
                                RoleName.ADMIN
                        ),
                        0.0D,
                        new Balance()
                )
        );
        log.info("Admin created: " + save);
    }
}


