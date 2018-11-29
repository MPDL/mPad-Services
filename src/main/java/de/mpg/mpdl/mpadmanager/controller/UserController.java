package de.mpg.mpdl.mpadmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.service.IUserService;

@RestController
public class UserController {

	@Autowired
	IUserService userService;
	
    @RequestMapping(value = "/users",  method = {RequestMethod.GET})
    public ResponseEntity<List<User>> getUser() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
}
