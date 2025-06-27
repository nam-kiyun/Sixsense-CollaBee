package com.demo.proworks.cmmn.tcp;

import com.inswave.elfw.core.CommVO;
import com.inswave.elfw.core.ElResDataVO;
import com.inswave.elfw.core.UserHeader;
import com.inswave.elfw.util.ElFldConverter;
import com.inswave.elfw.util.ElTcpUtil;

public class ProworksTcpLengthBaseSendUtil {

	public static ElResDataVO sendFld(String ip, int port, UserHeader userHeader, CommVO inVo, CommVO outVo,
			int lengthFieldLength, int iReadBlockLength, String encode) throws Exception {
			
		return sendFld(ip, port, userHeader, inVo, outVo, lengthFieldLength, iReadBlockLength, false, encode);
	}

	public static ElResDataVO sendFld(String ip, int port, UserHeader userHeader, CommVO inVo, CommVO outVo,
			int lengthFieldLength, String encode) throws Exception {

		return sendFld(ip, port, userHeader, inVo, outVo, lengthFieldLength, false, encode);
	}

	public static ElResDataVO sendFld(String ip, int port, UserHeader userHeader, CommVO inVo, CommVO outVo,
			int lengthFieldLength, int iReadBlockLength, boolean bTotLenReadFiledLenInclude, String encode)
			throws Exception {

		byte reqBytes[] = ElFldConverter.voToFld(userHeader, inVo);
		byte resBytes[] = ElTcpUtil.sendLengthTcp(ip, port, reqBytes, lengthFieldLength, iReadBlockLength,
				bTotLenReadFiledLenInclude, encode);
		ElResDataVO resVo = ElFldConverter.fldToVo(resBytes, userHeader.getClass(), outVo.getClass());

		return resVo;
	}

	public static ElResDataVO sendFld(String ip, int port, UserHeader userHeader, CommVO inVo, CommVO outVo,
			int lengthFieldLength, boolean bTotLenReadFiledLenInclude, String encode) throws Exception {

		byte reqBytes[] = ElFldConverter.voToFld(userHeader, inVo);
		byte resBytes[] = ElTcpUtil.sendLengthTcp(ip, port, reqBytes, lengthFieldLength, 1024,
				bTotLenReadFiledLenInclude, encode);
		ElResDataVO resVo = ElFldConverter.fldToVo(resBytes, userHeader.getClass(), outVo.getClass());

		return resVo;
	}

}
