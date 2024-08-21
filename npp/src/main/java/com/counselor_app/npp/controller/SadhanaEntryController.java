package com.counselor_app.npp.controller;
import com.counselor_app.npp.dto.SadhanaEntryByDateAndCounselorDTO;
import com.counselor_app.npp.dto.SadhanaEntryDTO;
import com.counselor_app.npp.dto.UserSadhanaResponseDTO;
import com.counselor_app.npp.service.SadhanaEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("sadhana-entry")
@CrossOrigin
public class SadhanaEntryController {
    @Autowired
    SadhanaEntryService sadhanaEntryService;

    @PostMapping("submit/{userId}")
    public ResponseEntity<String> submitSadhanaEntry(@PathVariable int userId,@RequestBody SadhanaEntryDTO sadhanaEntryDTO) throws Exception{

        try {
          sadhanaEntryService.submitSadhanaEntry(userId,sadhanaEntryDTO);
            return new ResponseEntity<>("Entry submitted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error submitting entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("all-entries")
    public ResponseEntity<List<SadhanaEntryDTO>> getAllEntries() {
        try {
            List<SadhanaEntryDTO> entries = sadhanaEntryService.getAllEntries();
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("all-entries/user/{userId}")
    public ResponseEntity<?> getAllEntriesByUserId(@PathVariable int userId) {
        try {
            UserSadhanaResponseDTO response = sadhanaEntryService.getUserAndAllEntries(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMessage = "Error retrieving Sadhana entries: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("all-entries/user/{userId}/date1/{date1}/date2/{date2}")
    public ResponseEntity<?> getAllEntriesByUserIdAndBwTwoDates(@PathVariable int userId,@PathVariable String date1 ,@PathVariable String date2) {
        try {
            List<SadhanaEntryDTO> entries = sadhanaEntryService.getAllEntriesByUserIdAndBwTwoDates(userId,date1,date2);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            String errorMessage = "Error retrieving Sadhana entries: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("all-entries/counselor-id/{counselorId}/date/{date}")
    public ResponseEntity<List<SadhanaEntryByDateAndCounselorDTO>> getAllEntriesByDate(@PathVariable int counselorId ,@PathVariable String date) {
        try {
            List<SadhanaEntryByDateAndCounselorDTO> entries = sadhanaEntryService.getAllEntriesByDate(counselorId,date);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/all-entries/counselor-id/{counselorId}/event/{event}/date1/{date1}/date2/{date2}")
    public ResponseEntity<?> getAllEntriesForEventBwTwoDates(@PathVariable int counselorId, @PathVariable String event, @PathVariable String date1, @PathVariable String date2) {
        Map<String, Map<String, String>> responseData = sadhanaEntryService.getAllEntriesForEventBwTwoDates(counselorId, event,date1, date2);
        return ResponseEntity.ok(responseData);
    }












}
