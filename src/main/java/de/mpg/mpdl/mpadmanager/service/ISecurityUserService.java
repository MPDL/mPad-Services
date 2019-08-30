package de.mpg.mpdl.mpadmanager.service;

public interface ISecurityUserService {

  String validatePasswordResetToken(long id, String token);

}