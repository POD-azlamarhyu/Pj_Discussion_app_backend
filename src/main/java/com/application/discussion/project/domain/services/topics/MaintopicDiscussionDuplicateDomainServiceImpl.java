package com.application.discussion.project.domain.services.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.MaintopicRepository;

@Service
public class MaintopicDiscussionDuplicateDomainServiceImpl implements MaintopicDiscussionDuplicateDomainService {

    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicDiscussionDuplicateDomainServiceImpl.class);

    /**
     * 指定されたメイントピックIDに関連するディスカッションが重複して存在するかを確認する
     * 
     * @param maintopicId メイントピックID
     * @return 重複が存在する場合はtrue、存在しない場合はfalse
     */
    @Override
    public Boolean isDuplicateDiscussionExists(Long maintopicId) {
        logger.info("Checking for duplicate discussions for maintopicId: {}", maintopicId);
        Boolean exists = maintopicRepository.existsMaintopic(maintopicId);
        logger.info("Duplicate discussion exists: {}", exists);
        return exists;
    }

}
