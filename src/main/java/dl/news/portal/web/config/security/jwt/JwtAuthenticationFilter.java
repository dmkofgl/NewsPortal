package dl.news.portal.web.config.security.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.response.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String tokenSub = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");
            try {
                tokenSub = jwtTokenUtil.getSubFromToken(authToken);
            } catch (IllegalArgumentException e) {
                responseError(res, "an error occured during getting username from token", HttpStatus.UNAUTHORIZED);
                return;
            } catch (ExpiredJwtException e) {
                responseError(res, "the token is expired and not valid anymore", HttpStatus.UNAUTHORIZED);
                return;
            } catch (SignatureException e) {
                responseError(res, "Authentication Failed. Username or Password not valid.", HttpStatus.UNAUTHORIZED);
                return;
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        if (tokenSub != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(tokenSub);
            if (jwtTokenUtil.validateAccessToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(req, res);
    }

    private void responseError(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String result = mapper.writeValueAsString(
                new ErrorResponse(message, httpStatus.getReasonPhrase()));
        response.setStatus(httpStatus.value());
        response.setContentLength(result.length());
        response.getWriter().write(result);
    }
}
