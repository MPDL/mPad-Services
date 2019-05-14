package de.mpg.mpdl.mpadmanager.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.util.HashSet;
import java.util.Set;

@Entry(objectClasses = {"top", "groupOfUniqueNames"}, base = "cn=groups")
public final class LdapGroup {

    private static final String BASE_DN = "dc=mpadmanager,dc=de";

    @Id
    private Name dn;

    @Attribute(name="cn")
    @DnAttribute("cn")
    private String name;

    @Attribute(name="uniqueMember")
    private Set<Name> members;

    public LdapGroup() {
    }

    public LdapGroup(String name, Set<Name> members) {
        Name dn = LdapNameBuilder.newInstance(BASE_DN)
                .add("ou", "groups")
                .add("cn", name)
                .build();
        this.dn = dn;
        this.name = name;
        this.members = members;
    }

    public LdapGroup(Name dn, String name, Set<Name> members) {
        this.dn = dn;
        this.name = name;
        this.members = members;
    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public Set<Name> getMembers() {
        return members;
    }

    public void setMembers(Set<Name> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(Name member) {
        if (this.members == null){
            this.members = new HashSet<>();
        }
        members.add(member);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }

    @Override
    public String toString() {
        return "Group{" +
                "dn=" + dn +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }
}