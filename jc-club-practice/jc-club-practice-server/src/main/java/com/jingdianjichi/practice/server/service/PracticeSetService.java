package com.jingdianjichi.practice.server.service;

import com.jingdianjichi.practice.server.vo.SpecialPracticeVO;

import java.util.List;

public interface PracticeSetService {
    List<SpecialPracticeVO> getSpecialPracticeContent();
}