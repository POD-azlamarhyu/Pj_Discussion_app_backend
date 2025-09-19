package com.application.discussion.project.application.services.topics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.topics.MaintopicDeleteResponse;
import com.application.discussion.project.domain.repositories.MaintopicRepository;

@Service
public class MaintopicDeleteServiceImpl implements MaintopicDeleteService {
    
    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicDeleteServiceImpl.class);
    
    /**
     * メイントピックを物理削除する
     * @param id 削除対象のメイントピックID
     */
    @Override
    public MaintopicDeleteResponse service(final Long id) {
        if(!maintopicRepository.existsMaintopic(id)){
            logger.error("Maintopic with ID {} not found for deletion", id);
            throw new ApplicationLayerException("メイントピックは存在しません", HttpStatus.NOT_FOUND, HttpStatusCode.valueOf(404));
        }
        maintopicRepository.deleteMaintopic(id);
        return new MaintopicDeleteResponse();
    }

}
