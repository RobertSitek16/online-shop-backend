package com.robert.shop.security.service;

import com.robert.shop.common.exception.LinkExpiredException;
import com.robert.shop.common.exception.ObjectNotFoundException;
import com.robert.shop.common.exception.ObjectNotIdenticalException;
import com.robert.shop.common.mail.EmailClientService;
import com.robert.shop.security.model.User;
import com.robert.shop.security.model.dto.ChangePassword;
import com.robert.shop.security.model.dto.EmailObject;
import com.robert.shop.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LostPasswordService {

    private final UserRepository userRepository;
    private final EmailClientService emailClientService;
    @Value("${app.serviceAddress}")
    private final String serviceAddress;

    @Transactional
    public void sendLostPasswordLink(EmailObject email) {
        User user = userRepository.findByUsername(email.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException("Email does not exist!"));
        String hash = generateHashForLostPassword(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());
        emailClientService.getInstance()
                .send(email.getEmail(), "Reset your password", createMessage(createLink(hash)));
    }

    @Transactional
    public void changePassword(ChangePassword changePassword) {
        if (!Objects.equals(changePassword.getPassword(), changePassword.getRepeatPassword())) {
            throw new ObjectNotIdenticalException("Passwords are not identical!");
        }
        User user = userRepository.findByHash(changePassword.getHash())
                .orElseThrow(() -> new ObjectNotIdenticalException("Password are not identical!"));
        if (user.getHashDate().plusMinutes(10).isAfter(LocalDateTime.now())) {
            user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(changePassword.getPassword()));
            user.setHash(null);
            user.setHashDate(null);
        } else {
            throw new LinkExpiredException("Link has expired!");
        }
    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    private String createMessage(String hashLink) {
        return "We have generated a link for you to change your password" +
                "\n\nClick on the link to reset your password: " +
                "\n" + hashLink +
                "\n\nThank you.";
    }

    private String createLink(String hash) {
        return serviceAddress + "/lostPassword/" + hash;
    }

}
