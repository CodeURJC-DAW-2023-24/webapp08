package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.News;
import com.example.backend.repository.NewsRepository;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;

    public void save(News news){
        newsRepository.save(news);
    }
    public void delete(News news){
        newsRepository.delete(news);
    }
    public void deleteById(Long id){
        newsRepository.deleteById(id);
    }
    public List<News> findByRutineId(Long id){
        return newsRepository.findByRutineId(id);
    }
    public Optional<News> findNewsById(Long id){
        return newsRepository.findById(id);
    }
    
}
