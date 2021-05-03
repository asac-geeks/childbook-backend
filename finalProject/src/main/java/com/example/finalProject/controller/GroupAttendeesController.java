package com.example.finalProject.controller;


import com.example.finalProject.entity.AppUser;
import com.example.finalProject.entity.EventAttendees;
import com.example.finalProject.entity.GroupAttendees;
import com.example.finalProject.entity.Groups;
import com.example.finalProject.repository.GroupAttendeesRepository;
import com.example.finalProject.repository.GroupRepository;
import com.example.finalProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class GroupAttendeesController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupAttendeesRepository groupAttendeesRepository;

    @Autowired
    GroupRepository groupRepository;


    @PostMapping("/attendGroup/{id}")
    public ResponseEntity<GroupAttendees> attendGroup(@PathVariable Integer id){
        GroupAttendees groupAttendees = new GroupAttendees();
        try{
            if((SecurityContextHolder.getContext().getAuthentication()) != null){
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                groupAttendees =  groupAttendeesRepository.save(new GroupAttendees(userDetails,groupRepository.findById(id).get()));
            }

        }catch (Exception ex){
            return new ResponseEntity(groupAttendees, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/attendGroup/{id}")
    public ResponseEntity deleteAteendGroup(@PathVariable("id") Integer id){

            try{
                groupAttendeesRepository.deleteById(id);
            }catch (IllegalArgumentException argumentException){
              return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(HttpStatus.OK);
    }

}
