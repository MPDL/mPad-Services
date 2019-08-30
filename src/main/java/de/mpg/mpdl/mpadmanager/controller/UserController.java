package de.mpg.mpdl.mpadmanager.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.web.util.CommonResult;

@RestController
public class UserController {

	@Autowired
    IUserService userService;

    @Autowired
    private MessageSource messages;
	
    @RequestMapping(value = "/users",  method = {RequestMethod.GET})
    @ResponseBody
    public CommonResult<List<User>> getUser() {
        List<User> users = userService.findAllUsers();
        return CommonResult.success(users);
    }

    @RequestMapping(value = "/user/delete", method = {RequestMethod.POST})
    @ResponseBody
    public CommonResult delete(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            userService.deleteUser(user);
            return CommonResult.success(email);
        } 
        return CommonResult.failed(email + " not found");
    }

    @RequestMapping(value = "/validEmail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult emailNotUsed(final Locale locale,  String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return CommonResult.success(messages.getMessage("message.userNotExist", null, locale));
        }
        return CommonResult.failed(messages.getMessage("message.regError", null, locale));
    }
}
