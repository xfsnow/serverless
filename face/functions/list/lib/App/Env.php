<?php
namespace Lib\App;

use Aws\Lambda\LambdaClient;
use Aws\S3\S3Client;
use Config\Sys;

/**
 * 环境变量类
 */
class Env extends App 
{
	/**
	 * Lambda 管理接口
	 */
	function lambdaAction() {
		$this->assign('title', 'Lambda 调用测试');
		$awsConfig = [
				'region' => Sys::$aws['region'],
				'version' => Sys::$aws['version'],
				'credentials' => [
						'key'    => Sys::$aws['key'],
						'secret' => Sys::$aws['secret'],
				],
		];
		$client = new LambdaClient($awsConfig);
		$result = $client->listFunctions([
				'MaxItems' => 10,
		]);
		$this->assign('functions', $result['Functions']);
		$this->display('env_lambda');
	}
	
	
	
	function phpAction() {
		phpinfo();
	}
	
	function s3listAction() {
		$this->assign('title', 'S3列表测试');
		$awsConfig = [
				'region' => Sys::$aws['region'],
				'version' => Sys::$aws['version'],
				'credentials' => [
						'key'    => Sys::$aws['key'],
						'secret' => Sys::$aws['secret'],
				],
		];
		$client = new S3Client($awsConfig);
		$result = $client->listBuckets();
		print_r($result);
// 		$this->assign('functions', $result['Functions']);
// 		$this->display('env_lambda');
// 		$this->display('env_php');
	}
	
}