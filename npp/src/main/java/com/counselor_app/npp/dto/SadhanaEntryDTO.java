package com.counselor_app.npp.dto;
import lombok.Data;

import java.util.Date;

@Data
public class SadhanaEntryDTO {
    private int sadhanaEntryId;
    private String chantingCompletionTime;
    private String bookReadingDuration;
    private String lectureHearingDuration;
    private String chantingAttendance;
    private String mangalAartiAttendance;
    private String sbClassAttendance;
    private String dateOfSadhana;
    private int userId;
}
