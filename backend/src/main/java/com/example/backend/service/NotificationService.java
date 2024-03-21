package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Notification;
import com.example.backend.repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
	private NotificationRepository notificationRepository;

    public void save(Notification notification) {
		notificationRepository.save(notification);

	}
}
