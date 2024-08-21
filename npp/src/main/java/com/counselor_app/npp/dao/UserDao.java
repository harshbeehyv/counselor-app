package com.counselor_app.npp.dao;

import com.counselor_app.npp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
public interface UserDao extends JpaRepository <User,Integer> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    Optional<User> findOneByEmailAndPassword(String email, String encodedPassword);

    List<User> findByCounselor_Counselorid(Integer counselorId);

    @Query(value = "SELECT u.name AS username, " +
            "COALESCE(se.chanting_completion_time, '') AS chantingCompletionTime, " +
            "COALESCE(se.book_reading_duration, '') AS bookReadingDuration, " +
            "COALESCE(se.lecture_hearing_duration, '') AS lectureHearingDuration, " +
            "COALESCE(se.chanting_attendance, '') AS chantingAttendance, " +
            "COALESCE(se.mangal_aarti_attendance, '') AS mangalAartiAttendance, " +
            "COALESCE(se.sb_class_attendance, '') AS sbClassAttendance " +
            "FROM user u " +
            "LEFT JOIN sadhana_entry se ON u.userid = se.user_id AND se.date_of_sadhana = :dateOfSadhana " +
            "WHERE u.counselor_id = :counselorId", nativeQuery = true)


    List<Object[]> findUsersSadhanaByCounselorAndDate(@Param("counselorId") Integer counselorId, @Param("dateOfSadhana") String dateOfSadhana);


}


