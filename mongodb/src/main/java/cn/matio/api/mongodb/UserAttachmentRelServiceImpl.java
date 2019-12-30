package cn.matio.api.mongodb;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserAttachmentRelServiceImpl implements UserAttachmentRelService {

    @Resource
    private UserAttachmentRelRepository userAttachmentRelRepository;

    @Override
    public UserAttachmentRel save(UserAttachmentRel userAttachmentRel) {
        return userAttachmentRelRepository.insert(userAttachmentRel);
    }

}