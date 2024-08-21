package com.counselor_app.npp.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;


@Data
@Entity

public class SadhanaEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sadhanaEntryId;

    @NotBlank
    private String chantingCompletionTime;

    @NotBlank
    private String bookReadingDuration;

    @NotBlank
    private String lectureHearingDuration;

    @NotBlank
    private String chantingAttendance;

    @NotBlank
    private String mangalAartiAttendance;

    @NotBlank
    private String sbClassAttendance;

    @NotBlank
    private String dateOfSadhana;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
