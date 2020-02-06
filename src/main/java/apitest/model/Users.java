package apitest.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Users {

    private Integer page;

    private Integer per_page;

    private Integer total;

    private Integer total_pages;

    List<UserGet> data;
}
