package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.rowmapper.ContactRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ContactRowMapper contactRowMapper;

    public ContactRepository(JdbcTemplate jdbcTemplate, ContactRowMapper contactRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.contactRowMapper = contactRowMapper;
    }

    public int saveContactMsg(Contact contact) {
        String sql = "INSERT INTO CONTACT_MSG(NAME, MOBILE_NUM, EMAIL, SUBJECT, MESSAGE, STATUS," +
                "CREATED_AT, CREATED_BY) VALUES (?,?,?,?,?,?,?,?)";

        return jdbcTemplate.update(sql,contact.getName(),contact.getMobileNum()
                                      ,contact.getEmail(),contact.getSubject(),contact.getMessage()
                                      ,contact.getStatus(),contact.getCreatedAt(), contact.getCreatedBy());
    }

    public List<Contact> findMessagesWithOpenStatus(String status) {
        String sql = "SELECT * FROM CONTACT_MSG WHERE STATUS=?";

        return jdbcTemplate.query(sql, contactRowMapper,status);


    }
}
