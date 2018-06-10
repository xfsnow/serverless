<?php
date_default_timezone_set('Asia/Shanghai');

define('_ENV_ONLINE_', 1);
define('_ENV_QA_', 2);
define('_ENV_DEV_', 3);

// 本地开发环境
if (false !== strpos(APP_PATH, 'serverless')) {
	$online_debug_mode = true; // 是否开启线上debug模式
	define('_ENV_', _ENV_DEV_);
	error_reporting(E_ALL);	
} else {
	$online_debug_mode = false; 
	define('_ENV_', _ENV_ONLINE_);
}
!defined('__DEBUG_MODE__') && define('__DEBUG_MODE__', isset($_REQUEST['__DEBUG_MODE__']) && $online_debug_mode);

// Init composer and namespace
$classLoader = require(APP_PATH . '/vendor/autoload.php');
$classLoader->setPsr4('Lib\\', APP_PATH . '/lib');
$classLoader->setPsr4('Config\\', APP_PATH . '/config');