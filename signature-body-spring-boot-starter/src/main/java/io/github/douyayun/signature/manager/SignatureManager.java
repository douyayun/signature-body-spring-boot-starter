package io.github.douyayun.signature.manager;

import io.github.douyayun.signature.storage.ConfigStorage;
import lombok.Data;

/**
 * 秘钥管理器
 *
 * @author houp
 * @since 1.0.0
 */
@Data
public class SignatureManager {

    private ConfigStorage configStorage;

}
