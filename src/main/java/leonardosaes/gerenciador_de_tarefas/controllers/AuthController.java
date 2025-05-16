//package leonardosaes.gerenciador_de_tarefas.controllers;
//
//import leonardosaes.gerenciador_de_tarefas.domain.user.User;
//import leonardosaes.gerenciador_de_tarefas.dto.LoginRequestDTO;
//import leonardosaes.gerenciador_de_tarefas.dto.RegisterRequestDTO;
//import leonardosaes.gerenciador_de_tarefas.dto.ResponseDTO;
//import leonardosaes.gerenciador_de_tarefas.infra.security.TokenService;
//import leonardosaes.gerenciador_de_tarefas.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final TokenService tokenService;
//
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody LoginRequestDTO body){
//        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User Not Found"));
//        if(passwordEncoder.matches(body.password(), user.getSenha() )){
//            String token = this.tokenService.generateToken(user);
//            return ResponseEntity.ok(new ResponseDTO(user.getNome(), token));
//        }
//
//        return ResponseEntity.badRequest().build();
//
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
//        Optional<User> user = this.userRepository.findByEmail(body.email());
//
//        if(user.isEmpty()){
//            User newUser = new User();
//            newUser.setSenha(passwordEncoder.encode(body.password()));
//            newUser.setEmail(body.email());
//            newUser.setNome(body.name());
//            this.userRepository.save(newUser);
//
//            String token = this.tokenService.generateToken(newUser);
//            return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), token));
//        }
//
//        return ResponseEntity.badRequest().build();
//
//    }
//
//}

package leonardosaes.gerenciador_de_tarefas.controllers;

import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.dto.LoginRequestDTO;
import leonardosaes.gerenciador_de_tarefas.dto.RegisterRequestDTO;
import leonardosaes.gerenciador_de_tarefas.dto.ResponseDTO;
import leonardosaes.gerenciador_de_tarefas.infra.security.TokenService;
import leonardosaes.gerenciador_de_tarefas.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager; // Injete o AuthenticationManager

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.email(), body.password())
            );
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User Not Found"));
                String token = this.tokenService.generateToken(user);
                return ResponseEntity.ok(new ResponseDTO(user.getNome(), token));
            } else {
                return ResponseEntity.status(401).body("Credenciais inválidas");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if(user.isEmpty()){
            User newUser = new User();
            newUser.setSenha(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setNome(body.name());
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), token));
        }

        return ResponseEntity.badRequest().build();

    }

}

