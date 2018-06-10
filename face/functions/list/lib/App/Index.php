<?php
namespace Lib\App;

/**
 * 应用层默认类
 */
class Index extends App
{
	function indexAction() {
		$this->assign('title', 'GCR Tech Summit 管理系统');
		$this->display('face_list');
	}
}