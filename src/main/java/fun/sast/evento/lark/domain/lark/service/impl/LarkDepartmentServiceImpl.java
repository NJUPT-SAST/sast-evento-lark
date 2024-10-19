package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.contact.v3.model.*;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.value.LarkDepartment;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LarkDepartmentServiceImpl implements LarkDepartmentService {

    @Resource
    private OApi oApi;

    @Override
    public List<LarkDepartment> list() {
        try {
            List<LarkDepartment> larkDepartments = new ArrayList<>();
            String pageToken = null;
            boolean hasMore;
            do {
                ChildrenDepartmentResp resp = oApi.getClient().contact().department().children(ChildrenDepartmentReq.newBuilder()
                        .departmentId("0") // root department id
                        .fetchChild(true)
                        .pageSize(50) // max page size
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    log.error("failed to list department: {}", resp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR_LIST_DEPARTMENT, resp.getMsg());
                }
                if (resp.getData().getItems() != null) {
                    Arrays.stream(resp.getData().getItems()).forEach(department -> larkDepartments.add(new LarkDepartment(
                            department.getOpenDepartmentId(),
                            department.getName()
                    )));
                }
                pageToken = resp.getData().getPageToken();
                hasMore = resp.getData().getHasMore();
            } while (hasMore);
            return larkDepartments;
        } catch (Exception e) {
            log.error("list department error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public LarkDepartment get(String openId) {
        try {
            GetDepartmentResp resp = oApi.getClient().contact().department().get(GetDepartmentReq.newBuilder()
                    .departmentId(openId)
                    .build());
            if (!resp.success()) {
                log.error("failed to get department: {}", resp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR_GET_DEPARTMENT, resp.getMsg());
            }
            return new LarkDepartment(
                    resp.getData().getDepartment().getOpenDepartmentId(),
                    resp.getData().getDepartment().getName()
            );
        } catch (Exception e) {
            log.error("get department error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<V2.LarkUser> getUserList(String openId) {
        try {
            List<V2.LarkUser> users = new ArrayList<>();
            String pageToken = null;
            boolean hasMore;
            do {
                FindByDepartmentUserResp resp = oApi.getClient().contact().user().findByDepartment(FindByDepartmentUserReq.newBuilder()
                        .departmentId(openId)
                        .pageSize(50)
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    log.error("failed to get user list of department: {}", resp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR_GET_DEPARTMENT_USER, resp.getMsg());
                }
                pageToken = resp.getData().getPageToken();
                hasMore = resp.getData().getHasMore();
                if (resp.getData().getItems() != null) {
                    Arrays.stream(resp.getData().getItems()).forEach(user -> {
                        AvatarInfo avatarInfo = user.getAvatar();
                        users.add(new V2.LarkUser(
                                user.getOpenId(),
                                user.getName(),
                                avatarInfo == null ? null : avatarInfo.getAvatar240()
                        ));
                    });
                }
            } while (hasMore);
            return users;
        } catch (Exception e) {
            log.error("get user list of department error", e);
            throw new RuntimeException( e.getMessage(), e);
        }
    }

    @Override
    public V2.Department mapToV2(LarkDepartment larkDepartment) {
        return new V2.Department(
                larkDepartment.openId(),
                larkDepartment.name()
        );
    }
}
