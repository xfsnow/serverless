'use strict';

var AWS = require('aws-sdk');
const im = require('imagemagick');
const fs = require('fs');

const defaultFilePath = 'awslogo_w300.png';
// 样式名配置
const config = {
    'w500':{'width':500, 'height':300},
    'w300':{'width':300, 'height':150},
    'w50':{'width':50, 'height':40}
};
// 默认样式名
const defaultStyle = 'w50';

const postProcessResource = (resource, fn) => {
    let ret = null;
    if (resource) {
        if (fn) {
            ret = fn(resource);
        }
        try {
            fs.unlinkSync(resource);
        } catch (err) {
            // Ignore
        }
    }
    return ret;
};

const resize = (filePathResize, style, data, callback) => {
    // Lambda 本地写文件，必须是 /tmp/ 下
    var filePathResize = '/tmp/'+filePathResize;
    // 直接用 Buffer 操作图片转换，源文件不写到本地磁盘，但是转换成的文件要写盘，所以最后再用 postProcessResource 把临时文件删了。
    var resizeReq = {
      srcData: data.Body,
      dstPath: filePathResize,
      width: style.width,
      height: style.height
    };
    try {
        im.resize(resizeReq, (err) => {
            if (err) {
                throw err;
            } else {
                console.log('Resize ok: '+ filePathResize);
                callback(null, postProcessResource(filePathResize, (file) => new Buffer(fs.readFileSync(file)).toString('base64')));
            }
        });
    } catch (err) {
        console.log('Resize operation failed:', err);
        callback(err);
    }
};

exports.handler = (event, context, callback) => {
    var s3 = new AWS.S3();
    var bucketName = 'image201702';
    // 从文件 URI 中截取出 S3 上的 key 和尺寸信息。
    // 稳妥起见，尺寸信息应该规定成样式名字，而不是直接把宽高参数化，因为后者会被人滥用。
    // 使用样式还有个好处，样式名字如果写错，可以有个默认的样式。
    var filepath = (undefined === event.filepath ? defaultFilePath:event.filepath);
    var tmp = filepath.split('.');
    var fileExt = tmp[1];
    tmp = tmp[0].split('_');
    var fileName = tmp[0];
    var style = tmp.pop();
    console.log(style);
    var validStyle = false;
    for (var i in config)
    {
        if (style == i)
        {
            validStyle = true;
            break;
        }
    }
    style = validStyle ? style : defaultStyle;
    console.log(style);
    var fileKey = fileName+'.'+fileExt;
    var params = {Bucket: bucketName, Key: fileKey};

    // 从 S3 下载文件，成功后再回调缩图
    s3.getObject(params, function(err, data) {
      if (err)
      {
        // TODO 文件如果不存在，如何容错？
          console.log(err, err.stack);
      }
      else
      {
          resize(filepath, config[style], data, callback);
      }
    });
};
