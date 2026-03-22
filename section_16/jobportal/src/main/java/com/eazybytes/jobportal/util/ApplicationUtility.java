package com.eazybytes.jobportal.util;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.entity.JobPortalUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationUtility {
    
    public static String getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApplicationConstants.SYSTEM;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof JobPortalUser jobPortalUser) {
            return jobPortalUser.getEmail();
        }

        if (principal instanceof String username && !"anonymousUser".equalsIgnoreCase(username)) {
            return username;
        }

        return ApplicationConstants.SYSTEM;
    }
}
