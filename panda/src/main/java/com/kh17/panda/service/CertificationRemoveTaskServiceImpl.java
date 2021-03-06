package com.kh17.panda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kh17.panda.repository.CertDao;
import com.kh17.panda.repository.IdentityVerificationDao;


@Service
public class CertificationRemoveTaskServiceImpl implements CertificationRemoveTaskService{

	@Autowired
	private CertDao certDao;
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
//	목표 : 매 정각마다 남아있는 인증번호 중 5분이 지난 것을 삭제
	@Override
	@Scheduled(cron = "0 0 * * * *")
	public void run() {
		certDao.clear();
		logger.info("비밀번호찾기 인증번호 초기화 작업이 수행되었습니다");
	}
    
	
	@Autowired
	private IdentityVerificationDao identityVerificationDao;
	
    @Override
	@Scheduled(cron = "0 5 * * * *")
	public void identity () {
		identityVerificationDao.clear();
		logger.info("이메일 본인 인증번호 초기화 작업이 수행되었습니다");
	}


}





