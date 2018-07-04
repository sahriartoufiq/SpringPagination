package com.sahriar.springPagination.domain;


import com.sahriar.springPagination.validatror.CompanyEmailConstarint;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by toufiq on 4/18/18.
 */
@Entity
@Table(name = "TBL_USERS")
public class User extends Domain {

    @Column(name = "name")
    @Size(max = 20, min = 3, message = "Between 3 to 20")
    private String name;

    @Column(name = "user_name")
    @Size(max = 20, min = 3, message = "Between 3 to 20")
    private String userName;

    @Column(name = "password")
    //  @Size(max = 100, min = 3, message = "Between 3 to 100")
    private String encodedPassword;

    @Transient
    @Size(max = 20, min = 3, message = "Between 3 to 20")
    private String password;

    @Column(name = "user_email", unique = true)
    @Email(message = "Invalid")
    @CompanyEmailConstarint
    private String email;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            //      orphanRemoval = true,
            mappedBy = "user")
    private Set<UserRole> userRoleSet;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            //orphanRemoval = true,
            mappedBy = "author")
    private Set<Post> userPost;

    @Column(name = "pic_location")
    private String picLocation;

    @Transient
    private MultipartFile pic;

    @Transient
    private String[] userRoles;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String[] userRoles) {
        this.userRoles = userRoles;
    }

    public Set<UserRole> getUserRoleSet() {
        return userRoleSet;
    }

    public void setUserRoleSet(Set<UserRole> userRoleSet) {
        this.userRoleSet = userRoleSet;
    }

    public String getPicLocation() {
        return picLocation;
    }

    public void setPicLocation(String picLocation) {
        this.picLocation = picLocation;
    }

    public MultipartFile getPic() {
        return pic;
    }

    public void setPic(MultipartFile pic) {
        this.pic = pic;
    }

    public Set<Post> getUserPost() {
        return userPost;
    }

    public void setUserPost(Set<Post> userPost) {
        this.userPost = userPost;
    }
}
