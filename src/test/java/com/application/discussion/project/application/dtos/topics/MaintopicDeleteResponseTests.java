package com.application.discussion.project.application.dtos.topics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("メイントピック削除レスポンスDTOのテスト")
public class MaintopicDeleteResponseTests {

    @Test
    @DisplayName("正常なレスポンスの作成")
    void testSuccessfulResponse() {
        String expectedMessage = "このリソースは存在しません";

        MaintopicDeleteResponse response = new MaintopicDeleteResponse();
        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    @DisplayName("デフォルトコンストラクタのテスト")
    void testDefaultConstructor() {

        MaintopicDeleteResponse response = new MaintopicDeleteResponse();
        assertNotNull(response);
    }

    @Test
    @DisplayName("メッセージの不一致テスト")
    public void testFailureMessageResponse(){
        String successDeleteMessage = "削除に成功しました．";
        String failedDeleteMessage = "削除に失敗しました．";

        MaintopicDeleteResponse response = new MaintopicDeleteResponse();
        assertNotEquals(failedDeleteMessage, response.getMessage());
        assertNotEquals(successDeleteMessage, response.getMessage());
    }
}
