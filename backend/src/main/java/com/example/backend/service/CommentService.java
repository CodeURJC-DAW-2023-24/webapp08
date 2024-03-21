package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Comment;
import com.example.backend.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public void save(Comment comment){
        commentRepository.save(comment);
    }

    public Comment findById(long id) {
		return commentRepository.findById(id).orElseThrow();
	}

    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }
}
