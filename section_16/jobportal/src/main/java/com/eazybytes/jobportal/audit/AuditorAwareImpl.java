package com.eazybytes.jobportal.audit;

import com.eazybytes.jobportal.util.ApplicationUtility;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @NullMarked
    @Override
    public Optional<String> getCurrentAuditor() {
        String loggedInUser = ApplicationUtility.getLoggedUser();
        return Optional.ofNullable(loggedInUser);
    }
}
