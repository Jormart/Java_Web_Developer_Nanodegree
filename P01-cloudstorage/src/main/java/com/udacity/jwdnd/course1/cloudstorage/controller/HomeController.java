package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, EncryptionService encryptionService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Model model) {
        // Fetch the user using authentication name
        User user = userService.getUser(authentication.getName());

        if (user == null) {
            model.addAttribute("error", "User not found. Please log in again.");
            return "redirect:/login";
        }

        Integer userId = user.getUserId();

        // Add model attributes for Thymeleaf binding
        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("files", fileService.getFiles(userId));
        model.addAttribute("credentials", credentialService.getCredentials(userId));

        // Add empty objects for form binding in modal dialogs
        model.addAttribute("note", new Note());
        model.addAttribute("credential", new Credential());
        model.addAttribute("file", new File());

        // Add encryption service for decryption in view
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }
}
