package leonardosaes.gerenciador_de_tarefas.infra.security;

import com.auth0.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);
        logger.debug("Token recuperado: {}", token);

        String login = tokenService.validateToken(token);
        logger.debug("Login (email) do token: {}", login);

        if (login != null) {
            try {
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("User Not Found"));
                logger.debug("Usuário encontrado no banco de dados: {}", user.getEmail());

                // Cria um Authentication com authorities vazias (já que não estamos usando roles)
                var authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Autenticação estabelecida para o usuário: {} com authorities vazias.", user.getEmail());

            } catch (Exception e) {
                logger.error("Erro ao autenticar usuário com o token", e);
            }
        } else {
            logger.debug("Token inválido ou ausente.");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Header Authorization ausente ou formato inválido.");
            return null;
        }
        String token = authHeader.substring(7);
        logger.debug("Token extraído: {}", token);
        return token;
    }
}