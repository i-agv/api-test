package apitest.model;

import lombok.Data;

@Data
public class Error {

    public enum CommonError {
        NOT_FOUND("Not Found");

        private final String code;

        CommonError(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private String applicationErrorCode;

    private String error;

    private String message;
}
