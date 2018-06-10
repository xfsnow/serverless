<?php
namespace Config;

use Lib\Util\Env;

/**
 * 系统配置
 */
class Sys
{
    public static $sys = array(
        // 网站项目名
        'project'	=> 'Tech Summit',
        // 后台名称
        'adminname'	=> 'Tech Summit管理系统',

        // 分页列表时每页条数
        'page_each'=> 30,
        'page_span'=> 10,
    	'smarty_template' => APP_PATH.'/template',
    );
    
    public static $aws = array(
    	'region'=> 'us-west-2',
    	'version'=> 'latest',
    );
}
switch (_ENV_)
{
    //外网正式配置
	case _ENV_ONLINE_:
    Sys::$sys += array(
    	'asset' => '//rh.simg.cf/face/',
	    // Lambda 只能写临时目录 /tmp
        'smarty_templates_c' => '/tmp/templates_c',
        
    );
    Sys::$aws += [
    	'key' => Env::getEnv('AWSKEY'),
    	'secret' => Env::getEnv('AWSSECRET')
    ];
    break;

    // 开发
	case _ENV_DEV_:
    Sys::$sys += array(
	  'asset' => '//asset:8081/',
    	'smarty_templates_c' => APP_PATH.'/templates_c',
    );
    Sys::$aws += [
    		'key' => getenv('AWSKEY'),
    		'secret' => getenv('AWSSECRET')
    ];
    break;
}