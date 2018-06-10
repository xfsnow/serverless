<?php
use Lib\App\App;
use Lib\Util\Env;

define('APP_PATH', realpath(__DIR__));
require(APP_PATH . '/config/inc.php');

App::dispatch(Env::requestPath());