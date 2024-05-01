package com.xiancai.lora.utils;

import io.swagger.models.auth.In;

public class Base64 {
    /**
     * 该函数瑶对时间戳进行Base64加密
     *
     * @param BASE64CODE 编码表
     * @return
     */
    private static final String BASE64CODE = "jA4CfE2GIKL9yMnNRF8STcVXhZ+a-bd6eJglikYmo5qprOsWtuwDBQHxz01v3UP7";

    public static Integer encode(Integer length, int[] input,StringBuilder outPut) {
        Integer i, count = 0;
        for (i = 0; i + 3 < length; i += 3) {
            System.out.println(input[i] >>> 2);
            outPut.append(BASE64CODE.charAt(input[i] >>> 2));
            outPut.append(BASE64CODE.charAt(((input[i] << 4) & 0x30) | (input[i + 1] >>> 4)));
            outPut.append(BASE64CODE.charAt(((input[i + 1] << 2) & 0x3F) | (input[i + 2] >>> 6)));
            outPut.append(BASE64CODE.charAt(input[i + 2] & 0x3F));
            count += 4;
        }
        if (length > 1) {
            outPut.append(BASE64CODE.charAt(input[i] >>> 2));
            count++;
            if (length - i == 1) {
                outPut.append(BASE64CODE.charAt(input[i] << 4) & 0x30);
                count++;
            } else {
                outPut.append(BASE64CODE.charAt(((input[i] << 4) & 0x30) | (input[i + 1] >>> 4)));
                outPut.append(BASE64CODE.charAt((input[i + 1] << 2) & 0x3F));
                count += 2;
            }
        }
        return count;
    }

    public static int bsp_base_64_char_to_bin(char ch, String BASE64CODE) {
        int j = 0;
        for (int i = 0; i < 64; i++) {
            if (ch == BASE64CODE.charAt(j++)) {
                return i;
            }
        }
        return 0xFF;
    }

    public static int[] longToBytesLittle(long n) {
        int[] b = new int[8];
        b[0] = (int) (n & 0xff);
        b[1] = (int) ((n >> 8)  & 0xff);
        b[2] = (int) ((n >> 16) & 0xff);
        b[3] = (int) ((n >> 24) & 0xff);
        b[4] = (int) ((n >> 32) & 0xff);
        b[5] = (int) ((n >> 40) & 0xff);
        b[6] = (int) ((n >> 48) & 0xff);
        b[7] = (int) ((n >> 56) & 0xff);

        return b;
    }
    public static Integer deCode(String input, Integer length, int[] outPut) {
        int pos, count = 0, iter = 0, iterB = 0;
        int code_a, code_b, code_c, code_d;
        for (pos = 0; pos + 4 < length; pos += 4) {
            code_a =  bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
            code_b =  bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
            code_c = bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
            code_d =  bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
            outPut[iterB++] =  ((code_a << 2) | (code_b >>> 4));
            outPut[iterB++] =  ((code_b << 4) | (code_c >>> 2));
            outPut[iterB++] =  ((code_a << 2) | code_d);
            count++;
        }
        if (length > pos) {
            int char_remain = length - pos;
            count++;
            if (char_remain == 2) {// 1 bytes avaliable data remain
                code_a = bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
                code_b = bsp_base_64_char_to_bin( input.charAt(iter++), BASE64CODE);
                outPut[iterB++] =  ((code_a << 2) | (code_b >>> 4));
            }else {
                code_a = bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
                code_b = bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
                code_c = bsp_base_64_char_to_bin(input.charAt(iter++), BASE64CODE);
                outPut[iterB++] =  ((code_a << 2) | (code_b >>> 4));
                outPut[iterB++] =  ((code_b << 4) | (code_c >>> 2));
                count++;
            }
        }
        return count;
    }
}
//    size_t bsp_base_64_decode(string input, size_t length, byte *output, string _BASE64CODE)
//    {
//        size_t pos, count = 0;
//        byte code_a, code_b, code_c, code_d;
//        for (pos = 0; pos + 4 < length; pos += 4)
//        {
//            code_a = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//            code_b = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//            code_c = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//            code_d = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//        *output++ = (code_a << 2) | (code_b >>> 4);
//        *output++ = (code_b << 4) | (code_c >>> 2);
//        *output++ = (code_c << 6) | code_d;
//            count += 3;
//        }
//        if (length > pos)
//        {
//            uint8_t char_remain = length - pos;
//            count++;
//            if (char_remain == 2) // 1 bytes avaliable data remain
//            {
//                code_a = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//                code_b = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//            *output++ = (code_a << 2) | (code_b >>> 4);
//            }
//            else // 2 bytes avaliable data remain
//            {
//                code_a = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//                code_b = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//                code_c = bsp_base_64_char_to_bin(*input++, _BASE64CODE);
//            *output++ = (code_a << 2) | (code_b >>> 4);
//            *output++ = (code_b << 4) | (code_c >>> 2);
//                count++;
//            }
//        }
//        return count;
//    }
//    size_t bsp_base_64_encode(byte *input, size_t length, string output, string _BASE64CODE)
//    {
//        size_t i, count = 0;
//        for (i = 0; i + 3 <= length; i += 3)
//        {
//        *output++ = _BASE64CODE[input[i] >>> 2];
//        *output++ = _BASE64CODE[((input[i] << 4) & 0x30) | (input[i + 1] >>> 4)];
//        *output++ = _BASE64CODE[((input[i + 1] << 2) & 0x3F) | (input[i + 2] >>> 6)];
//        *output++ = _BASE64CODE[input[i + 2] & 0x3F];
//            count += 4;
//        }
//        if (length > i)
//        {
//        *output++ = _BASE64CODE[input[i] >>> 2];
//            count++;
//            if (length - i == 1)
//            {
//            *output++ = _BASE64CODE[(input[i] << 4) & 0x30];
//                count++;
//            }
//            else
//            {
//            *output++ = _BASE64CODE[((input[i] << 4) & 0x30) | (input[i + 1] >>> 4)];
//            *output++ = _BASE64CODE[(input[i + 1] << 2) & 0x3F];
//                count += 2;
//            }
//        }
//        return count;
//    }