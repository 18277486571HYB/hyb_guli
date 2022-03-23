package com.hyb.CommonUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Msg{
    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回信息")
    private String message;

    @ApiModelProperty("返回数据")
    private Map<String,Object> data=new HashMap<>();

    private Msg(){}

    public static Msg success(){
        Msg msg = new Msg();
        msg.setSuccess(true);
        msg.setCode(ResultCode.success);
        msg.setMessage("成功");
        return msg;
    }

    public static Msg fail(){
        Msg msg=new Msg();
        msg.setCode(ResultCode.fail);
        msg.setSuccess(false);
        msg.setMessage("失败");
        return msg;
    }

    public Msg success(Boolean b){
        this.setSuccess(false);
        return this;
    }
    public Msg code(Integer code){
        this.setCode(code);
        return this;
    }
    public Msg message(String message){
        this.setMessage(message);
        return this;
    }
    public Msg data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public Msg data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

}
