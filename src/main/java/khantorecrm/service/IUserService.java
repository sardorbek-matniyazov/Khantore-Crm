package khantorecrm.service;

import khantorecrm.model.User;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.LoginDto;
import khantorecrm.payload.dto.RegisterDto;

import java.util.List;

public interface IUserService {
    OwnResponse login(LoginDto dto);
    OwnResponse register(RegisterDto dto);
    List<User> getAllUsers();

    User getCurrentUser();
}
