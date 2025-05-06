package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import az.mingle.exception.UserNotFoundException;
import az.mingle.mapper.UserMapper;
import az.mingle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDto getCurrentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUsername));

        return userMapper.toPublicDto(user);
    }

    @Override
    public Page<User> searchUsers(String name, String surname, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.search(name, surname, username, pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı: " + username));
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı"))
                .getUserId();
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
    }
}
