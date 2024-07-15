package com.example.testdpi.service;

import com.example.testdpi.dao.FormDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FormDataService {
    @Resource
    private FormDao formDao;
}
