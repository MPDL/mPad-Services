package de.mpg.mpdl.mpadmanager.repository;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.model.LdapUser;
import de.mpg.mpdl.mpadmanager.model.User;

import javax.naming.Name;
import javax.naming.directory.*;

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

    public void updatePassword(User user, String password) {
        Attribute attr = new BasicAttribute("userPassword", password);
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(user), new ModificationItem[] {item});
    }

    private Name buildDn(User user) {
        return LdapNameBuilder.newInstance("")
                .add("ou", "mpg")
                .add("ou", user.getOrganization())
                .add("uid", user.getEmail())
                .build();
    }
}

