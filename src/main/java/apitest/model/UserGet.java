package apitest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGet {

    private String id;

    private String email;

    private String first_name;

    private String last_name;

    private String avatar;
}
