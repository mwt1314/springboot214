package cn.matio.api.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserAttachmentRel {

    @Id
    private String id;

    private String userId;

    private String fileName;

}