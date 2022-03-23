package com.hyb.serviceedu.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectClassification {

    private String id;
    private String title;

    private String videoSourceId;

    List<SubjectClassification> children=new ArrayList<>();
}
