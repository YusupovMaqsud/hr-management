package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.RoleName;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.payload.LoginDto;
import uz.pdp.hrmanagement.payload.RegisterDto;
import uz.pdp.hrmanagement.repository.RoleRepository;
import uz.pdp.hrmanagement.repository.UserRepository;
import uz.pdp.hrmanagement.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager; //login yuliga
    @Autowired
    JwtProvider jwtProvider; //login yuliga


    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("Bunday email allaqachon mavjud", false);
        }
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());


        //SISTEMADA QAYSI USER EKANLIGINI ANIQLAYMIZ
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User systemUser = (User) authentication.getPrincipal();

        // SISTEMADAGI USER LAVOZIMINI ANIQLIMIZ
        //AGAR DIRECTOR BOLSA MANAJERNI SAQLAYDI
        Set<Role> systemUserRoles = systemUser.getRoles();
        if (systemUserRoles.equals("DIRECTOR")) {
            if (user.getRoles().equals("MANAGER")) {
                userRepository.save(user);
                // MANAGER EMAILIGA XABAR YUBORILADI
                sendEmail(user.getEmail(),user.getEmailCode());
                return new ApiResponse("Manager royhatga qo'shildi", true);
            }
            return new ApiResponse("Direktor faqat managerni qosha oladi", false);
        }
        //AGAR LAVOZIMI MANAGER BOLSA XODIMNI SAQLAYDI
        if (systemUserRoles.equals("HR_MANAGER")) {
            if (user.equals("EMPLOYEE")) {
                userRepository.save(user);
                // XODIM EMAILIGA XABAR YUBORILADI
                sendEmail(user.getEmail(),user.getEmailCode());
                return new ApiResponse("Xodim qo'shildi", true);
            }
            return new ApiResponse("HR_Manager faqat xodimlarni qosha oladi", false);
        }
        return new ApiResponse("Siz xodimlarni qosha olmaysiz", false);
    }



    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com"); //kimdan kelganligi
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Accauntni tasdiqlash"); //tekst
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode="
                    + emailCode + "+&email=" + sendingEmail + "'>Tasdiqlang</a>");//tasdiqlashni bossa shu yulga
            //http://localhost:8080/api/auth/verifyEmail?emailCode=2666511111g8kkk&email=Test@Pdp.com mana shunday bo'ladi
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Accaount tasdiqlangan", true);

        }
        return new ApiResponse("Accaount allaqachon tasdiqlangan", false);
    }





    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                    loginDto.getPassword()));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki login xato", false);

        }

    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(username + "topilmadi");
        //yoki
        //return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username+"topilmadi"))
    }
}
