package com.counselor_app.npp.controller;


import com.counselor_app.npp.dto.LoginDTO;
import com.counselor_app.npp.model.Counselor;
import com.counselor_app.npp.model.User;
import com.counselor_app.npp.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController()
@RequestMapping("counselors")
@CrossOrigin
public class CounselorController {

    @Autowired
    CounselorService counselorService;

    @PostMapping("signup")
    public ResponseEntity<Object> signup(@RequestBody Counselor counselor){

        return counselorService.signup(counselor);

    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO){
        return counselorService.login(loginDTO);
    }


    @GetMapping("counselorbyemail/{email}")
    public ResponseEntity<Object> getCounselorByEmail(@PathVariable String email){
        return counselorService.getCounselorByEmail(email);
    }

    @GetMapping("/{counselorid}/users")
    public ResponseEntity<?> getUsersByCounselor(@PathVariable Integer counselorid) {
        try {
            List<User> users = counselorService.getUsersByCounselor(counselorid);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch users for counselor with ID: " + counselorid, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @GetMapping("counselorbyemailpassword")
//    public ResponseEntity<Object> getCounselorByEmailPassword(@RequestBody LoginDTO loginDTO){
//        return counselorService.getCounselorByEmailPassword(loginDTO);
//    }
//
//    @GetMapping("getbyid/{counselorid}")
//    public ResponseEntity<CounselorDTO> getCounselorById(@PathVariable int counselorid){
//        return counselorService.getCounselorById(counselorid);
//    }
//
//    @PostMapping("updateProfile")
//    public ResponseEntity<String> updateCounselor(@RequestBody CounselorDTO counselorDTO) {
//        try {
//            counselorService.updateCounselor(counselorDTO);
//            return ResponseEntity.ok("Counselor updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to update counselor: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/check-email")
//    public ResponseEntity<Boolean> doesEmailExist(@RequestParam String email) {
//        try {
//            if (counselorService.doesEmailExist(email)) {
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
//            counselorService.changeCounselorPassword(changePasswordDTO.getEmail(), changePasswordDTO.getPassword());
//            return ResponseEntity.ok("Password changed successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
//        }
//    }

}
