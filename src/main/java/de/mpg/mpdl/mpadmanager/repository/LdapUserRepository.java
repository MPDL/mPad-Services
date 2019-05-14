package de.mpg.mpdl.mpadmanager.repository;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.model.LdapUser;

@Service
public class LdapUserRepository {

    @Autowired
    private LdapTemplate ldapTemplate;
    
    public LdapUser create(LdapUser ldapUser) {
        ldapTemplate.create(ldapUser);
        return ldapUser;
    }
    
    public LdapUser findByUid(String uid) {
        return ldapTemplate.findOne(query().where("uid").is(uid), LdapUser.class);
    }

    public void delete(LdapUser ldapUser) {
        ldapTemplate.delete(ldapUser);
    }
    
    public List<LdapUser> findAll() {
        return ldapTemplate.findAll(LdapUser.class);
    }
    
    public List<LdapUser> findByLastName(String lastName) {
        return ldapTemplate.find(query().where("sn").is(lastName), LdapUser.class);
    }
}

