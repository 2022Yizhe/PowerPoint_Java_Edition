package window.enums;

public enum ReturnCode {
    SUCCESS(0, "Operation completed successfully"),
    ERROR(-1, "An error occurred"),
    FAIL_TO_PARSE(1, "Json Parsing failed"),
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Access forbidden"),
    SERVER_ERROR(500, "Internal server error");

    private final int code;
    private final String message;

    // 构造函数
    ReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 获取返回码
    public int getCode() {
        return code;
    }

    // 获取返回消息
    public String getMessage() {
        return message;
    }

    // 通过代码获取枚举类型
    public static ReturnCode fromCode(int code) {
        for (ReturnCode returnCode : ReturnCode.values()) {
            if (returnCode.getCode() == code) {
                return returnCode;
            }
        }
        return null; // 或者返回一个默认值
    }
}