package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }
    @PostMapping("/save")
    public String saveOrUpdateCredential(Authentication authentication, Credential credential, Model model) {
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodeKey = Base64.getEncoder().encodeToString(key);
            String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodeKey);
            credential.setKey(encodeKey);
            credential.setPassword(encryptedPassword);

            if (credential.getCredentialId() == null) {
                credential.setUserId(userId);
                credentialService.addCredential(credential);
            } else {
                credentialService.updateCredential(credential);
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving the credential.");
            model.addAttribute("success", false);
        }

        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/delete/{credentialId}")
    public String delete(@PathVariable("credentialId") Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("success", true);
        return "result";
    }

}
