package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {

}