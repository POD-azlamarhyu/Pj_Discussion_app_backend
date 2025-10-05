package com.application.discussion.project.application.dtos.discussions;

/**
 * ディスカッション作成時のリクエストDTO
 * クライアントからのディスカッション作成要求に含まれるデータを格納する
 */
public class DiscussionCreateRequest {
    
    private String paragraph;
    
    /**
     * デフォルトコンストラクタ
     */
    public DiscussionCreateRequest() {}

    /**
     * 本文を指定してインスタンスを生成するコンストラクタ
     * 
     * @param paragraph ディスカッションの本文
     */
    public DiscussionCreateRequest(String paragraph) {
        this.paragraph = paragraph;
    }

    /**
     * ディスカッションの本文を取得する
     * 
     * @return ディスカッションの本文
     */
    public String getParagraph() {
        return paragraph;
    }

}
