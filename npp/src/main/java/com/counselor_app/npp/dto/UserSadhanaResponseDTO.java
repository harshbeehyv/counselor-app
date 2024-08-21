package com.counselor_app.npp.dto;
import com.counselor_app.npp.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UserSadhanaResponseDTO {
    private User user;
    private List<SadhanaEntryDTO> sadhanaEntries;

    public UserSadhanaResponseDTO(User user, List<SadhanaEntryDTO> sadhanaEntries) {
        this.user = user;
        this.sadhanaEntries = sadhanaEntries;
    }
}