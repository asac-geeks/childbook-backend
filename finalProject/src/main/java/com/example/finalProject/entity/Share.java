package com.example.finalProject.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedAt")
    private Date modifiedAt;

    public Share(AppUser appUser, Post post) {
        this.appUser = appUser;
        this.post = post;
    }

    public Share() {
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    AppUser appUser;


    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;
    // ................................. Yazan added by ......................................
    @ManyToOne
    GroupPost groupPost;

    public Share(AppUser appUser, GroupPost groupPost) {
        this.appUser = appUser;
        this.groupPost = groupPost;
    }


    public GroupPost getGroupPost() {
        return groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }


    // ................................. Yazan added by ......................................



    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
