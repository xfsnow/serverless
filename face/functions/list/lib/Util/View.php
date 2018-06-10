<?php
namespace Lib\Util;
use Config\Sys;

class View extends \Smarty
{
     /**
     * Customize our Smarty environment
     */
	public function __construct() {
		$this->setCompileDir(Sys::$sys['smarty_templates_c']);
//             $this->setCacheDir(Coco::get('runtime_dir') . '/smarty/cache/');
        $this->setTemplateDir(Sys::$sys['smarty_template']);
		$this->setLeftDelimiter('{%');
		$this->setRightDelimiter('%}');
		parent::__construct();
    }
}
