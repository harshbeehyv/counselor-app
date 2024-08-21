package com.counselor_app.npp.service;


import com.counselor_app.npp.dao.SadhanaEntryDao;
import com.counselor_app.npp.dao.UserDao;
import com.counselor_app.npp.dto.SadhanaEntryByDateAndCounselorDTO;
import com.counselor_app.npp.dto.SadhanaEntryDTO;
import com.counselor_app.npp.dto.UserSadhanaResponseDTO;
import com.counselor_app.npp.methods.DateTimeMethods;
import com.counselor_app.npp.model.SadhanaEntry;
import com.counselor_app.npp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SadhanaEntryService {

    @Autowired
    SadhanaEntryDao sadhanaEntryDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CounselorService counselorService;


    public void submitSadhanaEntry(int userId, SadhanaEntryDTO sadhanaEntryDTO) throws Exception {


        Optional<User> userOptional = userDao.findById(userId);
        if (!userOptional.isPresent()) {
            throw new Exception("User not found");
        }

        try {

            SadhanaEntry existingEntry = sadhanaEntryDao.findByUser_UseridAndDateOfSadhana(userId, sadhanaEntryDTO.getDateOfSadhana());

            if (existingEntry != null) {
                sadhanaEntryDao.delete(existingEntry);
            }

            DateTimeMethods dateTimeMethods=new DateTimeMethods();

            SadhanaEntry sadhanaEntry = new SadhanaEntry();
            sadhanaEntry.setUser(userOptional.get());
            sadhanaEntry.setDateOfSadhana(dateTimeMethods.convertToYYYYMMDD(sadhanaEntryDTO.getDateOfSadhana()));
            sadhanaEntry.setChantingCompletionTime(sadhanaEntryDTO.getChantingCompletionTime());
            sadhanaEntry.setBookReadingDuration(sadhanaEntryDTO.getBookReadingDuration());
            sadhanaEntry.setLectureHearingDuration(sadhanaEntryDTO.getLectureHearingDuration());
            sadhanaEntry.setChantingAttendance(sadhanaEntryDTO.getChantingAttendance());
            sadhanaEntry.setMangalAartiAttendance(sadhanaEntryDTO.getMangalAartiAttendance());
            sadhanaEntry.setSbClassAttendance(sadhanaEntryDTO.getSbClassAttendance());

            sadhanaEntryDao.save(sadhanaEntry);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public List<SadhanaEntryDTO> getAllEntriesByUserId(int userId) throws Exception {
        try {
            User user = userDao.findById(userId).orElseThrow(() -> new Exception("User not found"));
            List<SadhanaEntry> sadhanaEntries = sadhanaEntryDao.findByUser(user);
            return convertToSadhanaEntryDTOList(sadhanaEntries);
        } catch (Exception e) {
            throw new Exception("Error retrieving Sadhana entries: " + e.getMessage());
        }
    }

    public List<SadhanaEntryDTO> getAllEntries() throws Exception {
        try {

            List<SadhanaEntry> sadhanaEntries = sadhanaEntryDao.findAll();
            return convertToSadhanaEntryDTOList(sadhanaEntries);
        } catch (Exception e) {
            throw new Exception("Error retrieving Sadhana entries: " + e.getMessage());
        }
    }

    public List<SadhanaEntryDTO> getAllEntriesByUserIdAndBwTwoDates(int userId, String date1, String date2) throws Exception{
        try {

            List<Object[]> sadhanaEntries = sadhanaEntryDao.findAllEntriesByUserIdAndBwTwoDates(userId,date1,date2);

            return convertToDTOList(sadhanaEntries);
        } catch (Exception e) {
            throw new Exception("Error retrieving Sadhana entries: " + e.getMessage());
        }
    }


    public List<SadhanaEntryByDateAndCounselorDTO> getAllEntriesByDate(int counselorId,String date) throws Exception {
        try {
            List<Object[]> sadhanaEntries = userDao.findUsersSadhanaByCounselorAndDate(counselorId, date);
            List<SadhanaEntryByDateAndCounselorDTO> sadhanaEntriesByDateAndCounselorDTO = new ArrayList<>();
            for (Object[] entry : sadhanaEntries) {
                SadhanaEntryByDateAndCounselorDTO entryDTO = convertToDateWiseDTO(entry);
                sadhanaEntriesByDateAndCounselorDTO.add(entryDTO);
            }
            return sadhanaEntriesByDateAndCounselorDTO;
        } catch (Exception e) {
            throw new Exception("Error retrieving Sadhana entries: " + e.getMessage());
        }
    }


    public Map<String, Map<String, String>> getAllEntriesForEventBwTwoDates(int counselorId, String event ,String date1, String date2) {
        List<Object[]> results = sadhanaEntryDao.findEntriesByCounselorIdAndDates(counselorId,event,date1, date2);
        Map<String, Map<String, String>> responseData = new HashMap<>();


        List<User> users = getUsersByCounselorId(counselorId);
        for (User user : users) {
            responseData.put(user.getName(), new HashMap<>());
        }


        for (Object[] result : results) {
            String userName = (String) result[0];
            String entryDate = (String) result[1];
            String completionTime = (String) result[2];


            if (!responseData.containsKey(userName)) {
                continue;
            }


            responseData.get(userName).put(entryDate, completionTime);
        }


        fillMissingDates(responseData, date1, date2);

        return responseData;
    }


    private List<User> getUsersByCounselorId(int counselorId) {

        return counselorService.getUsersByCounselor(counselorId);

    }


    private void fillMissingDates(Map<String, Map<String, String>> responseData, String date1, String date2) {
        LocalDate startDate = LocalDate.parse(date1);
        LocalDate endDate = LocalDate.parse(date2);
        List<String> datesInRange = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            datesInRange.add(date.toString());
        }

        for (Map<String, String> userEntries : responseData.values()) {
            for (String date : datesInRange) {
                userEntries.putIfAbsent(date, "");
            }
        }
    }



    private List<SadhanaEntryDTO> convertToSadhanaEntryDTOList(List<SadhanaEntry> sadhanaEntries) {
        List<SadhanaEntryDTO> sadhanaEntriesDTO = new ArrayList<>();
        for (SadhanaEntry entry : sadhanaEntries) {
            SadhanaEntryDTO entryDTO = new SadhanaEntryDTO();
            entryDTO.setSadhanaEntryId(entry.getSadhanaEntryId());
            entryDTO.setChantingCompletionTime(entry.getChantingCompletionTime());
            entryDTO.setBookReadingDuration(entry.getBookReadingDuration());
            entryDTO.setLectureHearingDuration(entry.getLectureHearingDuration());
            entryDTO.setChantingAttendance(entry.getChantingAttendance());
            entryDTO.setMangalAartiAttendance(entry.getMangalAartiAttendance());
            entryDTO.setSbClassAttendance(entry.getSbClassAttendance());
            entryDTO.setUserId(entry.getUser().getUserid());
            entryDTO.setDateOfSadhana(entry.getDateOfSadhana());
            sadhanaEntriesDTO.add(entryDTO);
        }
        return sadhanaEntriesDTO;
    }

    private List<SadhanaEntryDTO> convertToDTOList(List<Object[]> sadhanaEntries) {
        List<SadhanaEntryDTO> sadhanaEntriesDTO = new ArrayList<>();
        for (Object[] entry : sadhanaEntries) {
            SadhanaEntryDTO entryDTO = new SadhanaEntryDTO();
            entryDTO.setSadhanaEntryId((Integer) entry[1]);
            entryDTO.setChantingCompletionTime((String) entry[2]);
            entryDTO.setBookReadingDuration((String) entry[3]);
            entryDTO.setLectureHearingDuration((String) entry[4]);
            entryDTO.setChantingAttendance((String) entry[5]);
            entryDTO.setMangalAartiAttendance((String) entry[6]);
            entryDTO.setSbClassAttendance((String) entry[7]);
            entryDTO.setUserId((Integer) entry[8]);
            entryDTO.setDateOfSadhana((String) entry[0]);
            sadhanaEntriesDTO.add(entryDTO);
        }
        return sadhanaEntriesDTO;
    }

    private SadhanaEntryByDateAndCounselorDTO convertToDateWiseDTO(Object[] sadhanaEntry) {
        SadhanaEntryByDateAndCounselorDTO sadhanaEntryByDateAndCounselorDTO = new SadhanaEntryByDateAndCounselorDTO();
        sadhanaEntryByDateAndCounselorDTO.setUsername((String) sadhanaEntry[0]);
        sadhanaEntryByDateAndCounselorDTO.setChantingCompletionTime((String) sadhanaEntry[1]);
        sadhanaEntryByDateAndCounselorDTO.setBookReadingDuration((String) sadhanaEntry[2]);
        sadhanaEntryByDateAndCounselorDTO.setLectureHearingDuration((String) sadhanaEntry[3]);
        sadhanaEntryByDateAndCounselorDTO.setChantingAttendance((String) sadhanaEntry[4]);
        sadhanaEntryByDateAndCounselorDTO.setMangalAartiAttendance((String) sadhanaEntry[5]);
        sadhanaEntryByDateAndCounselorDTO.setSbClassAttendance((String) sadhanaEntry[6]);
        return sadhanaEntryByDateAndCounselorDTO;
    }

    public UserSadhanaResponseDTO getUserAndAllEntries(int userId) throws Exception {
        try {
            User user = userDao.findById(userId).orElseThrow(() -> new Exception("User not found"));
            List<SadhanaEntry> sadhanaEntries = sadhanaEntryDao.findByUser(user);
            List<SadhanaEntryDTO> entryDTOs = convertToSadhanaEntryDTOList(sadhanaEntries);

            LocalDate today = LocalDate.now();
            LocalDate startDate = today.minusDays(60);

            // Define the date formatter matching your date string format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Map<LocalDate, SadhanaEntryDTO> entriesMap = entryDTOs.stream()
                    .collect(Collectors.toMap(
                            entry -> LocalDate.parse(entry.getDateOfSadhana(), dateFormatter),
                            entry -> entry
                    ));

            List<SadhanaEntryDTO> filledEntries = new ArrayList<>();
            for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
                SadhanaEntryDTO entry = entriesMap.getOrDefault(date, getDefaultNFEntry(date));
                filledEntries.add(entry);
            }
            return new UserSadhanaResponseDTO(user, filledEntries);
        } catch (Exception e) {
            throw new Exception("Error retrieving Sadhana entries: " + e.getMessage());
        }
    }

    private SadhanaEntryDTO getDefaultNFEntry(LocalDate date) {
        SadhanaEntryDTO entry = new SadhanaEntryDTO();
        entry.setDateOfSadhana(String.valueOf(date));
        entry.setChantingCompletionTime("NF");
        entry.setBookReadingDuration("NF");
        entry.setLectureHearingDuration("NF");
        entry.setChantingAttendance("NF");
        entry.setMangalAartiAttendance("NF");
        entry.setSbClassAttendance("NF");
        return entry;
    }
}

