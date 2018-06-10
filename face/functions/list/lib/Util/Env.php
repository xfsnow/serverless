<?php
namespace Lib\Util;

/**
 * 获取请求参数
 * 兼容普通 PHP 和 Lambda 环境
 */

//TODO lambda 环境下 POST 合并到 body-json里了，请求 cookie 也只体现成普通请求头，需要自己解释。
class Env {
    private static $_param = null;
    private static $_isLambda = null;
    private static $_stdin = null;

    public static function isCli() {
        return (php_sapi_name() == 'cli');
    }
    
    public static function isLambda() {
    	self::$_isLambda = (_ENV_ == _ENV_ONLINE_);
    	if (self::$_isLambda && is_null(self::$_stdin))
    	{
    		$stdin = stream_get_contents(fopen('php://stdin', 'r'));
    		self::$_stdin = json_decode($stdin, true);
    	}
    	return self::$_isLambda;
    }
    
    public static function getEnv($attr) {
    	$value = null;
    	if (self::isLambda()) {
    		$value = self::$_stdin['env'][$attr];
    	}
    	else {
    		$value = getenv($attr);
    	}
    	return $value;
    }

    public static function requestParam() {
        if (self::$_param === null) {
            if (self::isLambda()) {
            	$querystring = self::$_stdin['querystring'];
            	self::$_param = $querystring;
            }
            elseif (self::isCli()) {
            	self::$_param = array_splice($_SERVER['argv'], 2);
            }
            else {
                self::$_param = $_GET + $_POST;
                $input = json_decode(file_get_contents('php://input'), 1);
                if ($input) {
                    self::$_param = $input + self::$_param;
                }
            }
        }
        return self::$_param;
    }
    
    public static function param($attr) {
    	return isset(self::$_param[$attr]) ? self::$_param[$attr] : null;
    }

    public static function requestPath() {
    	if (self::isLambda()) {
    		$requestPath = self::$_stdin['context']['resource-path'];
    		return $requestPath;
    	}
        elseif (self::isCli()) {
            $requestPath = (isset($_SERVER['argv'][1])) ? $_SERVER['argv'][1] : null;
        } else {
            list($requestPath) = explode('?', $_SERVER['REQUEST_URI']);
        }

        preg_match_all('/[^\/]+/', $requestPath, $matched);
        $pathArray = $matched[0];
        $requestPath = join('/', $pathArray);
        return $requestPath;
    }
}