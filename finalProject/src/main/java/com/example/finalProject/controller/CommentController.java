package com.example.finalProject.controller;

import com.example.finalProject.OpenNLP.PipeLine;
import com.example.finalProject.entity.*;
import com.example.finalProject.models.VerificationRequest;
import com.example.finalProject.repository.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins= "*")
public class CommentController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TemporaryCommentRepository temporaryCommentRepository;
    @Autowired
    ParentRepository parentRepository;
    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/addComment/{id}")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id, @RequestBody TemporaryComment temporaryComment) {
        if((SecurityContextHolder.getContext().getAuthentication()) != null &&  this.checkPostContent(temporaryComment.getBody()) ){
            AppUser appUser = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            Post post = postRepository.findById(id).get();
            temporaryComment.setAppUser(appUser);
            temporaryComment.setPost(post);
            temporaryComment = temporaryCommentRepository.save(temporaryComment);
            return new ResponseEntity(temporaryComment, HttpStatus.OK);
        };
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/commentverification/{id}")
    public ResponseEntity<Comment> commentVerification(@PathVariable Integer id){
        Comment comment = new Comment();
        try{
            if((SecurityContextHolder.getContext().getAuthentication()) != null){
                Parent parent = parentRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                TemporaryComment temporaryComment = temporaryCommentRepository.findById(id).get();
                AppUser userDetails = userRepository.findByUserName(temporaryComment.getAppUser().getUserName());
                if (parent != null && userDetails.getParent().getId() == parent.getId()){
                    comment.setBody(temporaryComment.getBody());
                    comment.setAppUser(temporaryComment.getAppUser());
                    comment.setPost(temporaryComment.getPost());
                    comment.setAppUser(temporaryComment.getAppUser());
                    comment = commentRepository.save(comment);
                    temporaryCommentRepository.delete(temporaryComment);
                }else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity(comment, HttpStatus.OK);
    };

    @PutMapping("/comment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id , @RequestBody Comment comment){
        Comment commentUpdate=commentRepository.findById(id).get();
        try{
            if((SecurityContextHolder.getContext().getAuthentication()) != null){
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                System.out.println(userDetails);
                System.out.println(commentUpdate.getAppUser());
                if (commentUpdate != null && commentUpdate.getAppUser().getId() == userDetails.getId()){
                    commentUpdate.setBody(comment.getBody());
                    commentRepository.save(commentUpdate);

                }else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity(commentUpdate, HttpStatus.OK);
    }

    public boolean checkPostContent(String text){
        StanfordCoreNLP stanfordCoreNLP = PipeLine.getPipeLine();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        for (CoreSentence sentence : sentences) {
            String sentiment = sentence.sentiment();
            if("Negative".equals(sentiment)){
                return false;
            }
            System.out.println(sentiment + "\t" + sentence);
        }
        return true;
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity handleDeletePost(@PathVariable Integer id) {
        try{
            if((SecurityContextHolder.getContext().getAuthentication()) != null){
                Comment comment = commentRepository.findById(id).get();
                AppUser userDetails = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
                if (comment != null && userDetails.getId() == comment.getAppUser().getId()){
                    commentRepository.deleteById(id);
                }
            }
            return new ResponseEntity("Deleted",HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
