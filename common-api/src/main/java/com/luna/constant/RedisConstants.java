package com.luna.constant;

/**
 * @author TontoZhou
 * @since 2021/5/12
 */
public interface RedisConstants {

    // 因为常见key中会使用uuid或者base64版本，所以使用$防止重复
    // 并且key中应避免出现$


    // ip限制
    String IP_LIMIT_PREFIX = "IL$";

    // 每个IP每日登录错误数hash表key前缀
    String HASH_IP_LOGIN_ERROR_COUNT_DAY_PREFIX = "H_IEC$";

    // 手机每日短信发送数hash表key前缀
    String HASH_SMS_PHONE_COUNT_DAY_PREFIX = "H_SPC$";

    // IP每日短信发送数hash表key前缀
    String HASH_SMS_IP_COUNT_DAY_PREFIX = "H_SIC$";

    // 手机短信验证码key前缀（用户登录）
    String PHONE_CODE_USER_LOGIN_PREFIX = "PCL$";

    // 手机短信验证码key前缀（用户注册）
    String PHONE_CODE_USER_REGISTER_PREFIX = "PCR$";

}
