'use strict';
console.log('Loading LAMBDA_PROXY sample');
// var AWS = require('aws-sdk');

exports.handler = function(event, context, callback) {
    var bucketName = process.env.S3_BUCKET;
    var responseCode = 200;
    var result = {bucketName: bucketName};
    var response = {
        statusCode: responseCode,
        headers: {
            "x-custom-header" : "LAMBDA_PROXY sample"
        },
        body: JSON.stringify(result)
    };
    console.log("response: " + JSON.stringify(response))
    callback(null, response);
};
