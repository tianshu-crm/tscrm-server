package com.tianshu.system.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class DropDownUserList {

    String userName;

    @JsonSerialize(using = ToStringSerializer.class)
    Long userId;
}
