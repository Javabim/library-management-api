package com.maidscc.libraryManagementSystem.utils;


import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtil {


    public static String getLoggedInUser(){
        Object Principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Principal != null && Principal instanceof UserDetails){
            return ((UserDetails)Principal).getUsername();
        } else if(Principal != null){
            return Principal.toString();
        } else {
            throw new MaidSccLibraryException("no user is currently logged in");
        }
    }
}

