package de.mpg.mpdl.mpadmanager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.model.LdapGroup;

import java.util.List;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapGroupRepository {

    @Autowired
    private LdapTemplate ldapTemplate;

    public LdapGroup create(LdapGroup group) {
        ldapTemplate.create(group);
        return group;
    }

    public List<LdapGroup> findBy(String attr, String value) {
        return ldapTemplate.find(query().where(attr).is(value), LdapGroup.class);
    }

    public void update(LdapGroup group) {
        ldapTemplate.update(group);
    }

    public void delete(LdapGroup group) {
        ldapTemplate.delete(group);
    }

    public List<LdapGroup> findAll() {
        return ldapTemplate.findAll(LdapGroup.class);
    }

}
