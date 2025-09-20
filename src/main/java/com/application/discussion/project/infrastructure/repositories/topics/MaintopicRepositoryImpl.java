package com.application.discussion.project.infrastructure.repositories.topics;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

@Repository
public class MaintopicRepositoryImpl implements MaintopicRepository {

    private final JpaMaintopicsRepository jpaMaintopicsRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicRepositoryImpl.class);

    public MaintopicRepositoryImpl(JpaMaintopicsRepository jpaMaintopicsRepository) {
        this.jpaMaintopicsRepository = jpaMaintopicsRepository;
    }

    @Override
    public Maintopic findMaintopicById(Long maintopicId) {
        logger.info("Finding maintopic by ID: {}", maintopicId);
        return jpaMaintopicsRepository.findById(maintopicId)
                .map(entity -> new Maintopic(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt(),
                        entity.getIsDeleted(),
                        entity.getIsClosed()
                ))
                .orElseThrow(() -> {
                    logger.error("Maintopic with ID {} not found", maintopicId);
                    throw new ResourceNotFoundException("メイントピックは存在しません", "Not_Found");
                });
    }

    @Override
    public List<Maintopic> findMaintopicList() {
        List<Maintopics> maintopics = jpaMaintopicsRepository.findAll();
        logger.info("Retrieved {} maintopics from the database", maintopics.size());
        if (maintopics.isEmpty()) {
            logger.warn("No maintopics found in the database");
            throw new RuntimeException("No topics found");
            // Return an empty list if no topics are found
        }
        return maintopics.stream()
                .map(entity -> new Maintopic(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt(),
                        entity.getIsDeleted(),
                        entity.getIsClosed()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Maintopic createMaintopic(Maintopic maintopic) {
        logger.info("Creating new maintopic with title: {}", maintopic.getTitle());
        Maintopics entity = new Maintopics();
        entity.setTitle(maintopic.getTitle());
        entity.setDescription(maintopic.getDescription());
        entity.setCreatedAt(maintopic.getCreatedAt());
        entity.setUpdatedAt(maintopic.getUpdatedAt());
        entity.setIsDeleted(maintopic.getIsDeleted());
        entity.setIsClosed(maintopic.getIsClosed());

        Maintopics savedEntity = jpaMaintopicsRepository.save(entity);
        logger.info("Maintopic created with ID: {}", savedEntity.getId());

        return Maintopic.of(
            savedEntity.getId(),
            savedEntity.getTitle(),
            savedEntity.getDescription(),
            savedEntity.getCreatedAt(),
            savedEntity.getUpdatedAt(),
            savedEntity.getIsDeleted(),
            savedEntity.getIsClosed()
        );
    }
    @Override
    public Maintopics findModelById(final Long maintopicId) {
        logger.info("Finding maintopic model by ID: {}", maintopicId);
        return jpaMaintopicsRepository.findById(maintopicId)
                .orElseThrow(() -> {
                    logger.error("Maintopic model with ID {} not found", maintopicId);
                    throw new ResourceNotFoundException("メイントピックは存在しません", "Not_Found");
                });
    }

    /**
     * メイントピックを更新する
     * @param updateMaintopics 更新するMaintopicsエンティティ
     * @return 更新されたMaintopicドメインエンティティ
     * @throws RuntimeException 更新に失敗した場合
     */
    @Override
    public Maintopic updateMaintopic(final Maintopics updateMaintopics) {
        logger.info("Updating maintopic with ID: {}", updateMaintopics.getId());

        Maintopics updatedEntity = jpaMaintopicsRepository.save(updateMaintopics);
        logger.info("Maintopic with ID: {} updated successfully", updatedEntity.getId());

        return Maintopic.of(
            updatedEntity.getId(),
            updatedEntity.getTitle(),
            updatedEntity.getDescription(),
            updatedEntity.getCreatedAt(),
            updatedEntity.getUpdatedAt(),
            updatedEntity.getIsDeleted(),
            updatedEntity.getIsClosed()
        );
    }



    /**
     * 指定されたIDのメイントピックが存在するかをチェックする
     * @param maintopicId チェック対象のメイントピックID
     * @return メイントピックが存在する場合はtrue、存在しない場合はfalse
     */
    @Override
    public Boolean existsMaintopic(final Long maintopicId) {
        logger.info("Checking if maintopic exists with ID: {}", maintopicId);
        
        return jpaMaintopicsRepository.existsById(maintopicId);
    }

    /**
     * 指定されたIDのメイントピックを物理削除する
     * @param maintopicId 削除するメイントピックのID
     * @throws ResourceNotFoundException 指定されたIDのメイントピックが存在しない場合
     */
    @Override
    public void deleteMaintopic(final Long maintopicId) {
        logger.info("Deleting maintopic with ID: {}", maintopicId);
        jpaMaintopicsRepository.deleteById(maintopicId);
        logger.info("Maintopic with ID: {} deleted successfully", maintopicId);
    }
}
