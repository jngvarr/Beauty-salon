package security.provider;

import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import security.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
public class JwtCandidateAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthenticationCandidate auth) {
            String jwtString = auth.getJwt();
            // decode JWT. validate JWT.
            // extract:
            Claims claims = jwtUtil.extractAllClaims(jwtString);
            // extract from JWT. validate in database if necessary
            String userName = jwtUtil.extractUsername(claims);
            ArrayList<SimpleGrantedAuthority> authorities;
            List<String> userRoles = jwtUtil.extractRoles(claims); // extract from JWT
            authorities = userRoles.stream()
                    .map(StringUtils::trimToNull)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toCollection(ArrayList::new));
            return new PreAuthenticatedAuthenticationToken(userName, jwtString, authorities);
        }
        // failed to process JWT
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationCandidate.class.isAssignableFrom(authentication);
    }
}
