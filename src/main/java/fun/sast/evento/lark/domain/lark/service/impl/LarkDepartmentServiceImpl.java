package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.contact.v3.model.ChildrenDepartmentReq;
import com.lark.oapi.service.contact.v3.model.ChildrenDepartmentResp;
import com.lark.oapi.service.contact.v3.model.GetDepartmentReq;
import com.lark.oapi.service.contact.v3.model.GetDepartmentResp;
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

    public List<LarkDepartment> list() {
        try {
            List<LarkDepartment> larkDepartments = new ArrayList<>();
            String pageToken = null;
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
                Arrays.stream(resp.getData().getItems()).forEach(department -> larkDepartments.add(new LarkDepartment(
                        department.getOpenDepartmentId(),
                        department.getChatId(),
                        department.getName()
                )));
                pageToken = resp.getData().getPageToken();
            } while (pageToken != null && !pageToken.isEmpty());
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
                    resp.getData().getDepartment().getChatId(),
                    resp.getData().getDepartment().getName()
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }
}
