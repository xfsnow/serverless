<?php
$data = stream_get_contents(fopen('php://stdin', 'r'));

$json = json_decode($data, true);

$result = json_encode(array('input' =>$json));

echo $result;
