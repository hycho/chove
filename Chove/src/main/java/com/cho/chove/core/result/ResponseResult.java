package com.cho.chove.core.result;

import lombok.Data;
 
/**
 * Generator Response data via builder pattern
 * @author ChoHoyoung
 * @since 2015.03.20
 * @version 0.1
 * @see
 *
 * <pre>
 * << Modification Information >>
 *   
 *  UpdateDate  Updator     Description
 *  ----------  ----------  ---------------------------
 *  2014.03.20  ChoHoyoung  initialize
 *
 * </pre>
 */

@Data
public class ResponseResult {
	private String resultCd;
	private String resultMsg;
	private Object result;
	
	public ResponseResult(Builder builder) {
		this.resultCd = builder.resultCd;
		this.resultMsg = builder.resultMsg;
		this.result = builder.result;
	}

	public static class Builder {
		private String resultCd;
		private String resultMsg;
		private Object result;

		public Builder() {}

		public Builder addResultCd(String resultCd) {
			this.resultCd = resultCd;
			return this;
		}

		public Builder addResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
			return this;
		}

		public Builder addResult(Object result) {
			this.result = result;
			return this;
		}

		public ResponseResult build() {
			return new ResponseResult(this);
		}

	}
}
