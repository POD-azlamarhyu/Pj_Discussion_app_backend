package com.application.discussion.project.application.dtos.topics;

public class MaintopicDeleteResponse {

    private String message;

    /**
     * デフォルトコンストラクタ
     */
    public MaintopicDeleteResponse(){
        this.message = "このリソースは存在しません";
    }

    /**
     * メッセージを取得する
     * @return メッセージ
     */
    public String getMessage(){
        return this.message;
    }
}
