<?php
namespace Lib\Util;
/**
 * 字符串操作类
 */
class String
{
	/**
	 * 检查是否为简体中文。非简体中文都返回 false，也可能是非汉字的其它亚洲语言。
	 * @param string $str
	 * @return boolean
	 */
	static function isSimpCn($str)
	{
		// 原理就是把 UTF8 字符串转为仅包括简体字的 GB2312 再转回UTF8，如果仍相同，则为简体。
		return $str == mb_convert_encoding(mb_convert_encoding($str, 'GB2312', 'UTF8'), 'UTF8', 'GB2312');
	}

	/**
	 * 生成随机字符串。
	 * @param number $num 字符个数
	 * @param string $type number 仅数字，lower仅小写字母, upper仅小写字母, all 包含大小写字母。
	 * @return string
	 */
	static function randomString($num=6, $type='all'){
		switch ($type)
		{
			case 'all':
				$pool = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
			break;
			case 'number':
				$pool = '0123456789';
			break;
			case 'lower':
				$pool = '0123456789abcdefghijklmnopqrstuvwxyz';
			break;
			case 'upper':
				$pool = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
			break;
		}
		// 如果要生成的字符串比成员字符还长，累加出更长的串。如下的累加，其实是每次 $pool 都翻倍了，所以加的次数更少。
		while (strlen($pool)<$num)
		{
			$pool .= $pool;
		}
        $str = substr(str_shuffle($pool), 0, $num);
        return $str;
    }

    /**
     * 查手机号格式
     * @param string $mobile
     * @return boolean
     */
    static function checkMobile($mobile)
    {
        if (preg_match('/^1[34758]{1}\d{9}$/', $mobile))
        {
            return true;
        }
        return false;
    }

    /**
     * 手机号中间4位替换为星号
     * @param string $mobile
     * @return string
     */
    static function maskMobile($mobile)
    {
        $out = substr($mobile, 0, 3).'****'.substr($mobile, -4);
        return $out;
    }

    /**
     * 生成唯一标志客串
     * @return string
     */
    static function uniqueStr()
    {
       // 以服务器 IP 作为前缀，增强唯一性。
        $serverIp = isset($_SERVER['SERVER_ADDR']) ? $_SERVER['SERVER_ADDR']: '127.0.0.1';
        $hash = md5($serverIp.uniqid('',true));
        return $hash;
    }
}