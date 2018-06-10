<?php
namespace Lib\App;

/**
 * 人脸
 */
class Face extends App
{
	function listAction() {

	}
	
	function addAction() {
		echo '<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>添加</title>
</head>
<body>
<h1>人脸添加</h1>
</body>
</html>';
		print_r($_POST);
	}
}