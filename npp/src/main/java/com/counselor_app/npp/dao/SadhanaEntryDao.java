package com.counselor_app.npp.dao;

import com.counselor_app.npp.model.SadhanaEntry;
import com.counselor_app.npp.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;
public interface SadhanaEntryDao extends JpaRepository <SadhanaEntry,Integer> {
    SadhanaEntry findByUser_UseridAndDateOfSadhana(int userid, String dateOfSadhana);

    List<SadhanaEntry> findByUser(User user);

    List<SadhanaEntry> findByDateOfSadhana(String date);

    @Query(value = "WITH RECURSIVE date_range AS ( " +
            "    SELECT :startDate AS date " +
            "    UNION ALL " +
            "    SELECT DATE_ADD(date, INTERVAL 1 DAY) " +
            "    FROM date_range " +
            "    WHERE date < :endDate " +
            ") " +
            "SELECT dr.date, " +
            "       se.sadhana_entry_id, " +
            "       se.chanting_completion_time, " +
            "       se.book_reading_duration, " +
            "       se.lecture_hearing_duration, " +
            "       se.chanting_attendance, " +
            "       se.mangal_aarti_attendance, " +
            "       se.sb_class_attendance, " +
            "       se.user_id " +
            "FROM date_range dr " +
            "LEFT JOIN sadhana_entry se ON dr.date = se.date_of_sadhana AND se.user_id = :userId " +
            "ORDER BY dr.date", nativeQuery = true)
    List<Object[]> findAllEntriesByUserIdAndBwTwoDates(@Param("userId") int userId,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);


    @Query(value = "SELECT u.name AS UserName, se.date_of_sadhana AS Date, " +
            "CASE WHEN :event = 'chanting-completion-time' THEN se.chanting_completion_time " +
            "     WHEN :event = 'lecture-hearing-duration' THEN se.lecture_hearing_duration " +
            "     WHEN :event = 'book-reading-duration' THEN se.book_reading_duration " +
            "     WHEN :event = 'mangal-aarti-attendance' THEN se.mangal_aarti_attendance " +
            "     WHEN :event = 'sb-class-attendance' THEN se.sb_class_attendance " +
            "     WHEN :event = 'chanting-attendance' THEN se.chanting_attendance " +

            "     ELSE null " +
            "END AS CompletionTime " +
            "FROM user u " +
            "LEFT JOIN sadhana_entry se ON u.userid = se.user_id " +
            "WHERE u.counselor_id = :counselorId AND se.date_of_sadhana BETWEEN :date1 AND :date2", nativeQuery = true)
    List<Object[]> findEntriesByCounselorIdAndDates(@Param("counselorId") int counselorId, @Param("event") String event, @Param("date1") String date1, @Param("date2") String date2);


}
