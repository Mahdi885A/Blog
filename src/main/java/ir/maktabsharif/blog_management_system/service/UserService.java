package ir.maktabsharif.blog_management_system.service;


import ir.maktabsharif.blog_management_system.dto.UserRegisterDTO;
import ir.maktabsharif.blog_management_system.dto.UserLoginDTO;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User register(UserRegisterDTO dto) {

        if (dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        return user;
    }

    public User login(UserLoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null ||
                !BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public boolean delete(Long id) {
        return userRepository.delete(id);
    }
}