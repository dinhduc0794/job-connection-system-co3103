package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InterestedPostsResponse {
    private Long id;
    private List<JobPostingSearchResponse> jobPostings;
}
