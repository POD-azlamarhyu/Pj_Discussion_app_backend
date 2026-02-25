package com.application.discussion.project.application.dtos.discussions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 議論リストのレスポンス
 */

@Schema(description = "議論リストのレスポンス")
public class DiscussionListResponse {

    @Schema(description = "議論のリスト")
    private List<DiscussionResponse> discussions;

    @Schema(description = "総件数", example = "25")
    private Integer totalCount;

    @Schema(description = "現在のページ番号", example = "1")
    private Integer currentPage;

    @Schema(description = "1ページあたりの件数", example = "10")
    private Integer pageSize;

    @Schema(description = "総ページ数", example = "3")
    private Integer totalPages;

    /**
     * コンストラクタ
     * @param discussions
     * @param totalCount
     * @param currentPage
     * @param pageSize
     * @param totalPages
     */
    private DiscussionListResponse(List<DiscussionResponse> discussions, Integer totalCount, Integer currentPage, Integer pageSize, Integer totalPages) {
        this.discussions = discussions;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public DiscussionListResponse() {}

    /**
     * ファクトリーメソッド
     * @param discussions
     * @param totalCount
     * @param currentPage
     * @param pageSize
     * @param totalPages
     * @return
     */
    public static DiscussionListResponse of(List<DiscussionResponse> discussions, Integer totalCount, Integer currentPage, Integer pageSize, Integer totalPages) {
        return new DiscussionListResponse(discussions, totalCount, currentPage, pageSize, totalPages);
    }

    public List<DiscussionResponse> getDiscussions() {
        return discussions;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}
