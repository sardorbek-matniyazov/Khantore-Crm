package khantorecrm.service.impl;

import khantorecrm.model.Delivery;
import khantorecrm.model.Role;
import khantorecrm.model.User;
import khantorecrm.model.Warehouse;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.LoginDto;
import khantorecrm.payload.dto.RegisterDto;
import khantorecrm.repository.DeliveryRepository;
import khantorecrm.repository.RoleRepository;
import khantorecrm.repository.UserRepository;
import khantorecrm.repository.WarehouseRepository;
import khantorecrm.security.JwtProvider;
import khantorecrm.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;
    private final DeliveryRepository deliveryRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            RoleRepository roleRepository,
            DeliveryRepository deliveryRepository, WarehouseRepository warehouseRepository) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
        this.deliveryRepository = deliveryRepository;
        this.warehouseRepository = warehouseRepository;
    }

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
                return OwnResponse.LOGIN_SUCCESSFULLY.setData(user.getCurrentToken());
            }
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        }
        return OwnResponse.PASSWORD_WRONG.setMessage("Password is incorrect");
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public OwnResponse register(RegisterDto dto) {
        try {
            switch (dto.getRoleName()) {
                case DRIVER:
                    deliveryRepository.save(
                            new Delivery(
                                    new User(
                                            dto.getName(),
                                            dto.getPhoneNumber(),
                                            passwordEncoder.encode(dto.getPassword()),
                                            roleRepository.findByRoleName(dto.getRoleName()).orElse(
                                                    new Role(
                                                            dto.getRoleName()
                                                    )
                                            )
                                    ),
                                    new Warehouse(
                                            dto.getName() + "'s Baggage",
                                            ProductType.BAGGAGE
                                    )
                            )
                    );
                    break;
                case ADMIN:
                case SELLER:
                    final User user = createUser(dto);
                    return OwnResponse.CREATED_SUCCESSFULLY.setData(user);
            }
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
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
                        )
                )
        );
    }
}