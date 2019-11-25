'use strict';
console.log('CodePipeline sample');

var time = require('time');

exports.handler = (event, context, callback) => {
	var currentTime = new time.Date();
	currentTime.setTimezone("America/Los_Angeles");
    var result = 'The time in Los Angeles is '+ currentTime.toString();
    var response = {
        statusCode: 200,
        body: JSON.stringify(result)
    };
    console.log("response: " + JSON.stringify(response))
	callback(null, response);
};
