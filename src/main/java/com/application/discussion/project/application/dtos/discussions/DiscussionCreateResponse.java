package com.application.discussion.project.application.dtos.discussions;

/**
 * ディスカッション作成時のレスポンスDTO
 * ディスカッション作成後のクライアントへの応答データを格納する
 */
public class DiscussionCreateResponse {
    
    /**
     * 作成されたディスカッションのID
     */
    private Long discussionId;
    
    /**
     * ディスカッションの本文
     */
    private String paragraph;
    
    /**
     * 関連するメイントピックのID
     */
    private Long maintopicId;

    /**
     * デフォルトコンストラクタ
     */
    public DiscussionCreateResponse() {}

    /**
     * 全フィールドを初期化するコンストラクタ
     * 
     * @param discussionId 作成されたディスカッションのID
     * @param paragraph ディスカッションの本文
     * @param maintopicId 関連するメイントピックのID
     */
    public DiscussionCreateResponse(Long discussionId, String paragraph, Long maintopicId) {
        this.discussionId = discussionId;
        this.paragraph = paragraph;
        this.maintopicId = maintopicId;
    }

    /**
     * ディスカッションIDを取得する
     * 
     * @return ディスカッションID
     */
    public Long getDiscussionId() {
        return discussionId;
    }

    /**
     * ディスカッションの本文を取得する
     * 
     * @return ディスカッションの本文
     */
    public String getParagraph() {
        return paragraph;
    }

    /**
     * メイントピックIDを取得する
     * 
     * @return メイントピックID
     */
    public Long getMaintopicId() {
        return maintopicId;
    }
}
