package khantorecrm.security;

import khantorecrm.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MyUserDetailsService service;

    @Autowired
    public MyFilter(JwtProvider jwtProvider, MyUserDetailsService service) {
        this.jwtProvider = jwtProvider;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
            String username = jwtProvider.parseToken(token);
            User user;
            try {
                 user = service.loadUserByUsername(username);
//                 if (!token.equals(user.getCurrentToken()))
//                     throw new UsernameNotFoundException("Token doesn't match, there is another user is connected");
            } catch (UsernameNotFoundException e) {
                filterChain.doFilter(request, response);
                log.error(e.getMessage());
                return;
            }

            // authenticate user
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
