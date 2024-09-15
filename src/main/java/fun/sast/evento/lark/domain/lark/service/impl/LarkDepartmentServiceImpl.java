package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.contact.v3.model.*;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.value.LarkDepartment;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
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
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public LarkDepartment get(String openId) {
        try {
            GetDepartmentResp resp = oApi.getClient().contact().department().get(GetDepartmentReq.newBuilder()
                    .departmentId(openId)
                    .build());
            if (!resp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
            }
            return new LarkDepartment(
                    resp.getData().getDepartment().getOpenDepartmentId(),
                    resp.getData().getDepartment().getName()
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public List<String> getUserList(String openId) {
        try {
            List<String> users = new ArrayList<>();
            String pageToken = null;
            boolean hasMore;
            do {
                FindByDepartmentUserResp resp = oApi.getClient().contact().user().findByDepartment(FindByDepartmentUserReq.newBuilder()
                        .departmentId(openId)
                        .pageSize(50)
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
                }
                pageToken = resp.getData().getPageToken();
                hasMore = resp.getData().getHasMore();
                if (resp.getData().getItems() != null) {
                    Arrays.stream(resp.getData().getItems()).forEach(user -> users.add(user.getOpenId()));
                }
            } while (hasMore);
            return users;
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
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
