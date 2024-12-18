package com.habitlab.backend.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class NoteDTO {
    private UUID id;
    private String username;
    private String habitTitle;
    private Date occurrenceId;
    private Date createdAt;
    private String content;
}
