<?php
namespace Lib\App;
use Exception;
use Lib\Util\View;
use Config\Sys;

/**
 * 应用层基类,所有应用接口的基础方法,无验证.
 */
class App
{
	public static $request_uri;
	
	public static $control;
	
	public static $action;
	
	public static $api_name;
	
	public static $language;
	
	private $view;
	
	function __construct()
	{
		$this->view = new View();
		$this->assign('sys', Sys::$sys);
	}
	
	function __destruct()
	{}
	
	public function __get($name)
	{
		if (__DEBUG_MODE__)
		{
			echo __CLASS__ . " __get:" . $name . "<br>";
		}
		if ('model' == substr($name, 0, 5))
		{
			$className = 'Model_' . ucfirst(substr($name, 5));
		}
		if (! empty($className))
		{
			$this->$name = new $className();
			return $this->$name;
		}
		return false;
	}
	
	function assign($name, $value)
	{
		$this->view->assign($name, $value);
	}
	
	function error($msg)
	{
		$this->view->assign('pagetitle', array(
				array(
						'hint' => '错误提示',
						'url' => ''
				)
		));
		$this->view->assign('msg', $msg);
		$this->view->display('error');
	}
	
	function display($template)
	{
		$template_file = $template.'.html';
		$this->view->display($template_file);
	}
	
	/**
	 * 经过模板组织，不输出而是返回组装好的内容。
	 */
	function fetch($template)
	{}
	
	/**
	 * 不经过模板，直接输出数据。
	 * @param array $data 输出的数据
	 */
	function json($data)
	{
		header('Content-Type:application/json; charset=UTF-8');
		echo json_encode($data);
		exit;
	}
	
	/**
	 * 以 JSONP 输出 JS 代码
	 * @param string $str
	 */
	function jsonp($str)
	{
		header('Content-Type:application/json; charset=UTF-8');
		echo $str;
		exit;
	}
	
	function redirect($url)
	{
		header('location: ' . $url);
	}
	
	/**
	 * URI 解析及分发
	 */
	static function dispatch($uri)
	{
		// 平台接口的语言通过参数来指定，所以 URI 形如 :///controllerName/actionName
		$uriArray = parse_url($uri);
		self::$request_uri = trim($uriArray['path'], '/');
		$parseArray = explode('/', self::$request_uri, 5);
		self::$control = (isset($parseArray[0]) && $parseArray[0]) ? ucfirst($parseArray[0]) : 'Index';
		self::$action = (isset($parseArray[1]) && $parseArray[1]) ? strtolower($parseArray[1]) : 'index';
		$className = 'Lib\\App\\'.self::$control;
		self::$api_name = '/' . self::$control . '/' . self::$action . '/';
		// 不同应用的不存在页需要区别处理一下。
		if (class_exists($className))
		{
			$application = new $className();
			$action = self::$action . 'Action';
			if (__DEBUG_MODE__)
			{
				echo "Class Instance:" . $className . "<BR>";
			}
			if (method_exists($application, $action))
			{
				if (__DEBUG_MODE__)
				{
					echo "Excute Action:" . $className . "->" . $action . "()<BR>";
				}
				$application->$action();
			}
			else
			{
				throw new Exception($action.' Action not found!', - 3);
			}
		}
		else
		{
			throw new Exception($className.' Controller not found!', - 2);
		}
	}
}