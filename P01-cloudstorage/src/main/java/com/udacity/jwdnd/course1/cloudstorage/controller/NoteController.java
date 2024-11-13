package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/notes")
    public String insertOrUpdateNote(Authentication authentication, @ModelAttribute Note note, Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        note.setUserId(userId);

        if (note.getNoteId() == null) {
            noteService.addNote(note);
        } else {
            noteService.updateNote(note);
        }

        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("success", true);

        return "result"; // Ensure a proper redirect
    }

    @GetMapping("/notes/delete/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable("noteId") Integer noteId, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        noteService.deleteNote(noteId);
        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("success", true);

        return "result"; // Ensure a proper redirect
    }
}

