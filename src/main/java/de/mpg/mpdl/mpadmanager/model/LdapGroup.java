package de.mpg.mpdl.mpadmanager.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

@Entry(objectClasses = {"top", "organizationalUnit"})
public final class LdapGroup {

    @Id
    private Name dn;

    @Transient
    @Attribute(name="ou")
    @DnAttribute(value="ou")
    private String name;

    public LdapGroup() {
    }

    public LdapGroup(String name) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "MPG")
                .add("ou", name)
                .build();
        this.dn = dn;
        this.name = name;
    }

    public LdapGroup(Name dn, String name) {
        this.dn = dn;
        this.name = name;
    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LdapGroup{" +
                "dn=" + dn +
                ", ou='" + name + '\'' +
                '}';
    }
}