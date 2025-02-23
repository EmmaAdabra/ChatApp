package com.adb.chat_app.dao.commentDao;

public enum CommentSqlScriptsPath {

    INSERT_COMMENT(getUserScriptDir() + "insert_comment.sql"),
    GET_POST_COMMENTS(getUserScriptDir() + "get_post_comments.sql");

    private String path;

    private final static String sqrScriptsDir = "sql_scripts/commentscripts/";
    CommentSqlScriptsPath(String path) {
        this.path = path;
    }

    public static String  getUserScriptDir() {
        return sqrScriptsDir;
    }

    public String getPath() {
        return path;
    }
}
