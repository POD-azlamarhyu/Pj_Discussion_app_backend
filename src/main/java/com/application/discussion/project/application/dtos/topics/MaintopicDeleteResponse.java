package com.application.discussion.project.application.dtos.topics;

public class MaintopicDeleteResponse {

    private String message;

    /**
    * TODO: メッセージの内容は204ステータスに合わせている．Github Copilotのレビューのように削除が成功したことのメッセージに変更することを検討する．
    */
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
