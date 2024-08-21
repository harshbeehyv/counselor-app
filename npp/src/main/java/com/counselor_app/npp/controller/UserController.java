package com.counselor_app.npp.controller;

import com.counselor_app.npp.dto.LoginDTO;
import com.counselor_app.npp.model.User;
import com.counselor_app.npp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("signup")
    public ResponseEntity<Object> signup(@RequestBody User user){

        return userService.signup(user);

    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }


    @GetMapping("userbyemail/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }
//
//    @GetMapping("userbyemailpassword")
//    public ResponseEntity<Object> getUserByEmailPassword(@RequestBody LoginDTO loginDTO){
//        return userService.getUserByEmailPassword(loginDTO);
//    }
//
//    @GetMapping("getbyid/{userid}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable int userid){
//        return userService.getUserById(userid);
//    }
//
//    @PostMapping("updateProfile")
//    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
//        try {
//            userService.updateUser(userDTO);
//            return ResponseEntity.ok("User updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to update user: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/check-email")
//    public ResponseEntity<Boolean> doesEmailExist(@RequestParam String email) {
//        try {
//            if (userService.doesEmailExist(email)) {
//                return ResponseEntity.ok(true);
//            } else {
//                return ResponseEntity.ok(false);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.ok(false);
//        }
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
//        try {
//            userService.changeUserPassword(changePasswordDTO.getEmail(), changePasswordDTO.getPassword());
//            return ResponseEntity.ok("Password changed successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
//        }
//    }

}
