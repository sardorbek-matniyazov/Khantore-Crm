package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Delivery;
import khantorecrm.model.Role;
import khantorecrm.model.User;
import khantorecrm.model.Warehouse;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.LoginResponse;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.LoginDto;
import khantorecrm.payload.dto.RegisterDto;
import khantorecrm.payload.dto.UpdateUserDto;
import khantorecrm.repository.DeliveryRepository;
import khantorecrm.repository.RoleRepository;
import khantorecrm.repository.UserRepository;
import khantorecrm.security.JwtProvider;
import khantorecrm.service.IUserService;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements IUserService, Updatable<UpdateUserDto, Long> {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            RoleRepository roleRepository,
            DeliveryRepository deliveryRepository) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public OwnResponse login(LoginDto dto) {
        try {
            User user = repository.findByPhoneNumber(dto.getPhoneNumber()).orElseThrow(
                    () -> new NotFoundException("User not found"));
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setCurrentToken(jwtProvider.generateToken(dto.getPhoneNumber()));
                user = repository.save(user);

                // authenticate user
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return OwnResponse.LOGIN_SUCCESSFULLY.setData(
                        new LoginResponse(
                                user.getCurrentToken(),
                                user
                        )
                );
            }
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        }
        return OwnResponse.PASSWORD_WRONG.setMessage("Password is incorrect");
    }

    @Override
    public OwnResponse register(RegisterDto dto) {
        try {
            switch (dto.getRoleName()) {
                case DRIVER:
                    final User user1 = repository.save(
                            new User(
                                    dto.getName(),
                                    dto.getPhoneNumber(),
                                    passwordEncoder.encode(dto.getPassword()),
                                    roleRepository.findByRoleName(dto.getRoleName()).orElse(
                                            new Role(
                                                    dto.getRoleName()
                                            )
                                    ),
                                    dto.getKpiPercent(),
                                    new Balance()
                            )
                    );
                    deliveryRepository.save(
                            new Delivery(
                                    user1.getId(),
                                    user1
                                    ,
                                    new Warehouse(
                                            dto.getName() + "'s Baggage",
                                            ProductType.BAGGAGE
                                    )
                            )
                    );
                    break;
                case ADMIN:
                case LOADER:
                case SELLER:
                    final User user = createUser(dto);
                    return OwnResponse.CREATED_SUCCESSFULLY.setData(user);
            }
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.USER_ALREADY_EXISTS;
        }
    }

    private User createUser(RegisterDto dto) {
        return repository.save(
                new User(
                        dto.getName(),
                        dto.getPhoneNumber(),
                        passwordEncoder.encode(dto.getPassword()),
                        roleRepository.findByRoleName(dto.getRoleName()).orElse(
                                new Role(
                                        dto.getRoleName()
                                )
                        ),
                        dto.getKpiPercent(),
                        new Balance()
                )
        );
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public OwnResponse update(UpdateUserDto dto, Long id) {
        try {
            User user = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("User not found"));
            user.setName(dto.getName());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setKpiPercent(dto.getKpiPercent());

            return OwnResponse.UPDATED_SUCCESSFULLY.setData(repository.save(user));
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.USER_ALREADY_EXISTS;
        }
    }
}
