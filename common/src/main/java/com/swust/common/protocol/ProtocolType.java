package com.swust.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hz20035009-逍遥
 * date   2020/9/30 14:08
 * 协议类型
 * <pre>
 *     1. proxy 代理
 *     2. file_sync 文件同步
 *     3. ...
 * </pre>
 */
@AllArgsConstructor
@Getter
public enum ProtocolType {
    PROXY("proxy"), FILE_SYNC("file_sync");
    private final String type;
}