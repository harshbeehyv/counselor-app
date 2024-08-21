package com.counselor_app.npp.service;

import com.counselor_app.npp.dao.CounselorDao;
import com.counselor_app.npp.dao.SadhanaEntryDao;
import com.counselor_app.npp.dao.UserDao;
import com.counselor_app.npp.dto.LoginDTO;
import com.counselor_app.npp.model.Counselor;
import com.counselor_app.npp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CounselorService {


    @Autowired
    CounselorDao counselorDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ModelMapper modelMapper;


    public ResponseEntity<Object> signup(Counselor counselor) {
        if (counselorDao.existsByEmail(counselor.getEmail())) {
            System.out.println(counselor.getEmail() + "exists");
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }

        Counselor savedCounselor = counselorDao.save(counselor);
        System.out.println(savedCounselor);

        // Return the generated counselor ID
        return new ResponseEntity<>(savedCounselor.getCounselorid(), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> login(LoginDTO loginDTO) {
        String msg = "";
        Counselor counselor1 = counselorDao.findByEmail(loginDTO.getEmail());
        if (counselor1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = counselor1.getPassword();
            Boolean isPwdRight = password.equals(encodedPassword);
            if (isPwdRight) {
                Optional<Counselor> counselor = counselorDao.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (counselor.isPresent()) {
                    return new ResponseEntity<>("Logged in successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Error while logging in .Please try again", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Incorrect password entered", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Email does not exist", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getCounselorByEmail(String email) {
        try{
            return new ResponseEntity<>(counselorDao.findByEmail(email),HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(new Counselor(),HttpStatus.NOT_FOUND);
        }
    }


    public List<User> getUsersByCounselor(Integer counselorid) {
        try {
            return userDao.findByCounselor_Counselorid(counselorid);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch users for counselor with ID: " + counselorid);
        }
    }

//    public ResponseEntity<Object> getCounselorByEmailPassword(LoginDTO loginDTO) {
//        return new ResponseEntity<>(counselorDao.findOneByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()), HttpStatus.OK);
//    }
//
//    public ResponseEntity<CounselorDTO> getCounselorById(int counselorid) {
//        Counselor counselor = counselorDao.findById(counselorid).orElse(null);
//
//        if (counselor != null) {
//            CounselorDTO counselorDTO = modelMapper.map(counselor, CounselorDTO.class);
//            return new ResponseEntity<>(counselorDTO, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public void updateCounselor(CounselorDTO counselorDTO) throws Exception {
//        // Validate counselor input if necessary
//
//        // Fetch the existing counselor from the database
//        Counselor existingCounselor = counselorDao.findById(counselorDTO.getCounselorid())
//                .orElseThrow(() -> new Exception("Counselor not found"));
//
//        // Update counselor details
//        existingCounselor.setName(counselorDTO.getName());
//        existingCounselor.setEmail(counselorDTO.getEmail());
//        existingCounselor.setMobileno(counselorDTO.getMobileno());
//
//        Address address = counselorDTO.getAddress();
//        if (address != null) {
//            // Update address details
//            Address existingAddress = existingCounselor.getAddress();
//            existingAddress.setStreet(address.getStreet());
//            existingAddress.setCity(address.getCity());
//            existingAddress.setState(address.getState());
//            existingAddress.setPincode(address.getPincode());
//        }
//        counselorDao.save(existingCounselor);
//    }
//
//    public boolean doesEmailExist(String email) {
//        Optional<Counselor> counselor = Optional.ofNullable(counselorDao.findByEmail(email));
//        return counselor.isPresent();
//    }
//
//    public void changeCounselorPassword(String email, String newPassword) throws Exception {
//        Optional<Counselor> counselorOptional = Optional.ofNullable(counselorDao.findByEmail(email));
//
//        if (counselorOptional.isPresent()) {
//            Counselor counselor = counselorOptional.get();
//            counselor.setPassword(newPassword);
//            counselorDao.save(counselor);
//        } else {
//            throw new Exception("Counselor with email " + email + " not found");
//        }
//    }

}




