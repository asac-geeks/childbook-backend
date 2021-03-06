package com.example.finalProject.controller;

import com.example.finalProject.entity.AppUser;
import com.example.finalProject.entity.Event;
import com.example.finalProject.entity.Groups;
import com.example.finalProject.entity.Post;
import com.example.finalProject.repository.GroupRepository;
import com.example.finalProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins= "*")
public class GroupController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @PostMapping("/addgroup")
    public ResponseEntity<Groups> addGroup(@RequestBody Groups group){
        try{
            if((SecurityContextHolder.getContext().getAuthentication()) != null){
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                group.setAppUser(userDetails);
                group = groupRepository.save(group);
            }

        }catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity(group, HttpStatus.OK);
    };

    @GetMapping("/usergroups")
    public ResponseEntity handleGroupsFromUser() {
        try {
            if ((SecurityContextHolder.getContext().getAuthentication()) != null) {
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                if (userDetails.getGroups() != null) {
                    Set<Groups> userGroups = userDetails.getGroups();
                    return new ResponseEntity(userGroups, HttpStatus.OK);
                }
            }
            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/group/{title}")
    public ResponseEntity groupByName(@PathVariable String title) {
        try {
                List<Groups> groups = groupRepository.findByTitle(title);
                System.out.println("hi");
                return new ResponseEntity(groups,HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/group/{id}")
    public RedirectView deleteGroup(@PathVariable Integer id) {
        Groups groups = groupRepository.findById(id).get();
        try {
            if ((SecurityContextHolder.getContext().getAuthentication()) != null) {
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

                if (groups != null && groups.getAppUser().getId() == userDetails.getId()) {
                    groupRepository.delete(groups);
                }
            }

        } catch (Exception ex) {
            return new RedirectView("/error?message=Used%username");
        }
        return new RedirectView("/events");
    }
}
