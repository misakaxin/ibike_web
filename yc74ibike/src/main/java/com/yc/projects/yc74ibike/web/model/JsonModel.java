package com.yc.projects.yc74ibike.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/*
新知识:   @JsonInclude(JsonInclude.Include.NON_NULL)返回的对象中，有的字段为空，如果想字段为空时，或者字段为默认值时，不返回该字段
将该标记放在属性上，如果该属性为NULL则不参与序列化 
如果放在类上边,那对这个类的全部属性起作用 
Include.Include.ALWAYS 默认 
Include.NON_DEFAULT 属性为默认值不序列化 
Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化 
Include.NON_NULL 属性为NULL 不序列化 
*/
@JsonInclude(  value=JsonInclude.Include.NON_NULL )
@ApiModel(  value="结果信息实体",description="所有的REST调用得到的json结果"   )
public class JsonModel implements Serializable {
	private static final long serialVersionUID = 2880873342154063305L;
	
	@ApiModelProperty(value="操作响应码，一般1表示成功操作,其它为失败.",required=true)
	private Integer code;
	@ApiModelProperty(value="操作的响应信息，如code为0,则为异常信息.")
	private String msg;
	@ApiModelProperty(value="操作的结果，如code为1,则为结果值.")
	private Object obj;
	@ApiModelProperty(value="本操作执行完后，下一步重定向的地址")
	private String url;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "JsonModel [code=" + code + ", msg=" + msg + ", obj=" + obj + ", url=" + url + "]";
	}

}
