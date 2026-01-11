package com.application.discussion.project.domain.services.topics;

public interface MaintopicDiscussionDuplicateDomainService {
    /**
     * 指定されたメイントピックIDに関連するディスカッションが重複して存在するかを確認する
     * 
     * @param maintopicId メイントピックID
     * @return 重複が存在する場合はtrue、存在しない場合はfalse
     */
    Boolean isDuplicateDiscussionExists(Long maintopicId);
}
