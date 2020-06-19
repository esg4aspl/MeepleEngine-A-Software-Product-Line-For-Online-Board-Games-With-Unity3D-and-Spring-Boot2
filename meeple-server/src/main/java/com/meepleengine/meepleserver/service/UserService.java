package com.meepleengine.meepleserver.service;

import com.meepleengine.meepleserver.exception.BusinessException;
import com.meepleengine.meepleserver.model.User;
import com.meepleengine.meepleserver.model.dto.TokenDTO;
import com.meepleengine.meepleserver.model.request.UserRequest;
import com.meepleengine.meepleserver.repository.UserRepository;
import com.meepleengine.meepleserver.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public TokenDTO login(UserRequest userRequest) {
        User user = userRepository.get(userRequest.getUsername());

        if (user == null) {
            throw new BusinessException("User does not exist!");
        }

        // TODO: password hash
        if (!userRequest.getPassword().equals(user.getPassword())) {
            throw new BusinessException("Password mismatch!");
        }

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(jwtService.createToken(user.getId().toHexString()));
        return tokenDTO;
    }

    public void register(UserRequest userRequest) {
        User user = userRepository.get(userRequest.getUsername());

        if (user != null) {
            throw new BusinessException("User already exists!");
        }

        User newUser = new User(userRequest.getUsername(), userRequest.getPassword());
        userRepository.insert(newUser);
    }

}
