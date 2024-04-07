package sean.ea.labs.lab5.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sean.ea.labs.lab5.aop.ExecutionTime;
import sean.ea.labs.lab5.dto.UserDto;
import sean.ea.labs.lab5.entity.User;
import sean.ea.labs.lab5.exception.ResourceNotFoundException;
import sean.ea.labs.lab5.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @ExecutionTime
    public void create(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepo.save(user);
    }

    public void update(Long id, UserDto userDto) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        userRepo.save(user);
    }

    public UserDto get(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @ExecutionTime
    public List<UserDto> getAll() {
        List<User> users = userRepo.findAll();
        List<UserDto> ret = new ArrayList<>();
        users.forEach(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            ret.add(userDto);
        });
        return ret;
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
