package io.github.douyayun.signature.manager;

import io.github.douyayun.signature.storage.NonceConfigStorage;
import lombok.Data;

/**
 * 秘钥管理器
 *
 * @author houp
 * @version 1.0.0
 * @date 2023/3/17 20:16
 */
@Data
public class SignatureManager {

    private NonceConfigStorage nonceConfigStorage;

}
