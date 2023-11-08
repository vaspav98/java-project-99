package hexlet.code.controller;

import hexlet.code.dto.AuthRequest;
import hexlet.code.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public String logIn(@RequestBody AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

        authenticationManager.authenticate(authentication);

        String token = jwtUtils.generateToken(authRequest.getEmail());
        return token;
    }
}
