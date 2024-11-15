package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.PostDateEntity;
import com.javaweb.jobconnectionsystem.repository.PostDateRepository;
import com.javaweb.jobconnectionsystem.service.PostDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostDateServiceImpl implements PostDateService {

    @Autowired
    private PostDateRepository postDateRepository;

    @Override
    public PostDateEntity addPostDate(PostDateEntity postDate) {
        if (postDate == null) {
            return null;
        }
        return postDateRepository.save(postDate);
    }

    @Override
    public List<PostDateEntity> getAllPostDates() {
        return postDateRepository.findAll();
    }

    @Override
    public Optional<PostDateEntity> getPostDateById(Long id) {
        return postDateRepository.findById(id);
    }

    @Override
    public PostDateEntity updatePostDate(Long id, PostDateEntity postDateDetails) {
        PostDateEntity postDate = postDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostDate not found"));

        postDate.setDatetime(postDateDetails.getDatetime());

        return postDateRepository.save(postDate);
    }

    @Override
    public void deletePostDateById(Long id) {
        PostDateEntity postDate = postDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostDate not found"));
        postDateRepository.delete(postDate);
    }
}
