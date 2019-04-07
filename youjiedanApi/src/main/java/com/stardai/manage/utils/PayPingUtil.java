package com.stardai.manage.utils;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Refund;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Component
public class PayPingUtil {
    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret
     * Key
     */
    private final static String apiKey = "sk_live_G88Oa9G088uDbPGyH0WHevT8";
    private final static String apiKeytest = "sk_test_ebTO8CPenDGOunHeX1rX9Sq5";
    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    private final static String appId = "app_8mnD48zPOOKCv5KW";
    private final static String appIdceshi = "app_8y90qPWbXLy5rDKK";

    /**
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)； 然后登录
     * [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证） 将你的公钥复制粘贴进去并且保存->先启用 Test
     * 模式进行测试->测试通过后启用 Live 模式
     */


    private final static String pubKey = "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0pyjbt81nCR+HqEWBkT6\n"
            + "DKz2amX6G/JJ7jX5IzxGmA2CoaEhgwA0IDZU9ft8TyTQK/BbmWasAFoZN2LV7u83\n"
            + "eexWcb6gKgSr/PPblAmERgNrJ4PQqXnn+XFHGqmRrSaH+C2maNOtd4Sx6BYhSDKS\n"
            + "gH7DbRGg2LyiLpc2C8qASj3/1kT8VVqqbIKOTA5N45YFfsAF\n"
            + "dmIyaX6nimqtE2M6pknwQUtt8QtXht4+7Nd/op/GwftW6+80nF3bLGcj3\n"
            + "fcNAM35zSqHptFUTUOjg5VknF5tP7FN6NO+h8GYUHmgwewUWKEUbVKJ6\n" + "gwIDAQAB\n"
            + "-----END PUBLIC KEY-----\n";
    private final static String pubKeyTest = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyoOH0HhHem++dyqiG+FR\n" +
            "v5wYjvILUObcyDUKXeEr9BZbhWCavs9UEJbz5pgt7aBfFVdyPFEBL/fDsjaK5QQV\n" +
            "y1x/8zNhPScNqA2MfKehYOCalolUuRwWY8/59FwpPuYZNf/y+b3TFxoOh/kNcsy2\n" +
            "MS3RrFWDn/Vf+oJBkwMWHKPupdib8H6mQZa9wlSUqSzpth6YPjvHMqCHIY6HH2Eg\n" +
            "ujdOahkecZ0Fn1CwT33KkbHZGh1I0y9O8CzrXgaB+kwqMpYvmhL75RCdDtV2GX2F\n" +
            "tMaAz1ZBnEIvjcUbWxwI1EDeiCFK7dLNEVvrMVhoVDDbBYNneLp2+QzLo0LrvaWn\n" +
            "AQIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";
    private final static String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQC60o4ZWzUrEqFRz7LETgR3FhhTUmyICewhlsFU+WYASJ3zKZHd\n" +
            "j/Vjt5MNcpQMAbnPSdTAv9bqI423sBa5pyWH1yz+1Lo48FDpc7AldKv83p+lQHL6\n" +
            "ePRSWNrJOzp3kKEIcivg1AszVS1rkQj6gHeH5rtr7i6oNuf3uAEv5rLPbwIDAQAB\n" +
            "AoGBAIyf/yxg9kWE0X9Weva+zyegrxDRAzbnWSHza3V+AWSHRLWLfeur+AdmYD+I\n" +
            "Fp5vEiFRLt8jyC8YMg2pc4GtqlYrsuRi7+tYmzTSCm0kWtrVyhJ9BCXKxzTvuV86\n" +
            "kA5ZTZLdGnx7opD3pbN9eLrZS1bX02NSFk9WGZqcyISYsJwhAkEA4ADrdCeA9qfW\n" +
            "Xy6bb2Tob8oAoZElwoRULLzBFyxwsaVLctaOZeXLTk9FbR85tnUq6TM/lsuCBeUG\n" +
            "OBPubzRUUQJBANWCCx3V3P/GovNhbQyURuBIAE1KYilU4Snrqygp0RSnI6VmCLwG\n" +
            "EcWqC92DGBHwYuZ4CIbPbgLXZnpaR1HCt78CQQC4yuly5Ff2o5Us5nUiG1vJRRrq\n" +
            "LqS46JPMaFbeuDGry5IHhcrpcv4adzjuAbGJvPUOdtHBo9d652oO/xpXMRNRAkAq\n" +
            "imiwnyYTiH57Q9Q4h2q/0VjODZ4VYiYi9iP6MqrIFK1TjUr8O7Fa0xDeJ/qpyF3M\n" +
            "UepRAg+HXWe7xTUko0l9AkEAtDOoly4C7c/GkycIkXGfPWRhsJ0DvjJCWvkE4aWt\n" +
            "i0K0D1PRiXfJ3j6/opsuWyFcxcNqHBVMXzCoklyWfTdSyg==\n" +
            "-----END RSA PRIVATE KEY----";

    private static PayPingUtil instance = new PayPingUtil();

    private PayPingUtil() {
    }

    public static PayPingUtil getInstance() {
        return instance;
    }

    {
        // 设置 API Key
      Pingpp.apiKey = apiKey;
        //Pingpp.apiKey = apiKeytest;
        // 设置私钥路径，用于请求签名
        // Pingpp.privateKeyPath = privateKeyFilePath;
        Pingpp.privateKey = privateKey;
//    Pingpp.privateKey = privateKeytest;
    }

    /**
     * /** 发起支付 map 里面参数的具体说明请参考：https://www.pingxx.com/api#api-c-new
     *
     * @param amount 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
     * @param subject 商品描述
     * @param body 商品详细信息
     * @param orderNo 订单编号
     * @param channel 渠道 alipay wx
     * @param ip 客户端IP
     * @param extra 可扩展字段 渠道参数详情ping++查看
     * @param initialMetadata 可扩展字段 用户回传值定义
     * @return 返回客户端 该订单 请求信息
     */
    public Charge createCharge(int amount, String subject, String body, String channel, String ip, String orderNo,
                               Map<String, Object> extra, Map<String, Object> initialMetadata) throws Exception {
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);// 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", channel);// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);
        if (extra == null)
            extra = new HashMap<String, Object>();
        chargeMap.put("extra", extra);

        if (initialMetadata == null)
            initialMetadata = new HashMap<String, Object>();
        // initialMetadata.put("color", "red");

        chargeMap.put("metadata", initialMetadata);

            // 发起交易请求
            charge = Charge.create(chargeMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            String chargeString = charge.toString();
            System.out.println(chargeString);
            return charge;
    }
    /**
     * 退款
     *
     * 创建退款，需要先获得 charge ,然后调用 charge.getRefunds().create(); 参数具体说明参考：https://www.pingxx.com/api#api-r-new
     * 可以一次退款，也可以分批退款。
     *
     * @param amount 退款的金额, 单位为对应币种的最小货币单位，例如：人民币为分（如退款金额为 1 元，此处请填 100）。必须小于等于可退款金额，默认为全额退款
     * @param chargeId 交易流水号
     * @param description 退款描述
     * @return Refund 退款json信息
     */
    public Refund refund(Integer amount, String chargeId, String description) {
        if (chargeId == null) {
            return null;
        }
        Refund refund = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("description", description);
        params.put("amount", amount);// 退款的金额, 单位为对应币种的最小货币单位，例如：人民币为分（如退款金额为 1 元，此处请填 100）。必须小于等于可退款金额，默认为全额退款

        try {
            refund = Refund.create(chargeId, params);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
        return refund;
    }

    /**
     * 查询退款
     *
     * 根据 Id 查询退款记录。需要传递 charge。
     *
     * @param refundid 退款ID
     * @param chargeId 交易流水号
     * @return 退款信息json
     */
    public String retrieve(String refundid, String chargeId) {
        if (chargeId == null) {
            return null;
        }
        Refund refund = null;
        try {
            refund = Refund.retrieve(chargeId, refundid);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
        return refund.toString();
    }
    /**
     * 查询支付状态
     *
     * 根据 Id 查询支付状态。需要传递 charge。 参考文档：https://www.pingxx.com/api#api-r-inquiry
     *@param chargeId 交易流水号
     * @return 支付状态信息json
     */
    public String queryPaymentStatus(String chargeId){
        try {
            Charge charge= Charge.retrieve(chargeId);
            return charge.toString();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据条件查询支付订单信息
     *
     * 根据 集合 查询支付状态。需要传递 charge。 参考文档：https://www.pingxx.com/api#api-r-inquiry
     *@param chargeParams 支付状态参数集合
     * @return 支付订单信息json
     */
    public String queryPaymentInfoList(Map<String, Object> chargeParams){
        try {
            ChargeCollection  charges= Charge.list(chargeParams);
            return charges.toString();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
      return null;
    }
    /**
     * 获得公钥
     *
     * @return
     * @throws Exception
     */
    public PublicKey getPubKey() throws Exception {
//    String pubKeyString = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        String pubKeyString = pubKeyTest.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    /**
     * 验证签名
     *
     * @param dataString
     * @param signatureString
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

    private static SecureRandom random = new SecureRandom();

    public static String randomString(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length);
    }

    public static int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
    }
}
