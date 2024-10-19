package fun.sast.evento.lark.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    AUTH_ERROR(1000, "Authentication error"),
    PARAM_ERROR(1001, "Parameter error"),
    PERMISSION_DENIED(1002, "Permission denied"),
    CHECKIN_CODE_NOT_EXISTS(1003, "Checkin code not exists or has expired"),
    LARK_ERROR_CREATE_CALENDAR_EVENT(2001, "Failed to create event"),
    LARK_ERROR_SET_ROOM(2002, "Failed to set room"),
    LARK_ERROR_GET_EVENT(2003, "Failed to get event"),
    LARK_ERROR_PATCH_CALENDAR_EVENT(2004, "Failed to patch event"),
    LARK_ERROR_DELETE_ROOM(2005, "Failed to delete room"),
    LARK_ERROR_DELETE_CALENDAR_EVENT(2006, "Failed to delete event"),
    LARK_ERROR_INVITE_USER(2007, "Failed to invite user"),
    LARK_ERROR_REMOVE_INVITEE(2008, "Failed to remove invitee"),
    LARK_ERROR_LIST_INVITEE(2009, "Failed to list invitee"),
    LARK_ERROR_LIST_DEPARTMENT(2010, "Failed to list department"),
    LARK_ERROR_GET_DEPARTMENT(2011, "Failed to get department"),
    LARK_ERROR_GET_DEPARTMENT_USER(2012, "Failed to get user list"),

    FEEDBACK_ALREADY_GIVEN(3001, "You have already given feedback for this event"),
    ;
    private final int code;
    private final String message;
}
