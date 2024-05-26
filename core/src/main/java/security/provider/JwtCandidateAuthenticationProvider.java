package security.provider;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.jngvarr.authservice.services.UserDetailsServiceImpl;
import security.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class JwtCandidateAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthenticationCandidate auth) {
            String jwtString = auth.getJwt();
            // decode JWT. validate JWT.
            // extract:
            if (isValid(jwtString)) {
                String userId = jwtUtil.extractUserId(jwtString); // extract from JWT. validate in database if necessary
                List<String> userRoles = jwtUtil.extractRoles(jwtString); // extract from JWT
                ArrayList<SimpleGrantedAuthority> authorities = userRoles.stream()
                        .map(StringUtils::trimToNull)
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            return new PreAuthenticatedAuthenticationToken(userId, jwtString, authorities);
        }

        // failed to process JWT
        return null;
    }

    private boolean isValid(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationCandidate.class.isAssignableFrom(authentication);
    }
}
