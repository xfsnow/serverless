process.env['PATH'] = process.env['PATH'] + ':' + process.env['LAMBDA_TASK_ROOT'];

const spawn = require('child_process').spawn;

exports.handle = function(event, context) {

    //var php = spawn('php',['helloLambda.php']); //local debug only
    var php = spawn('php-7-bin/bin/php',['helloLambda.php']);
    var output = "";

    //send the input event json as string via STDIN to php process
    php.stdin.write(JSON.stringify(event));

    //close the php stream to unblock php process
    php.stdin.end();

    //dynamically collect php output
    php.stdout.on('data', function(data) {
          output+=data;
    });

    //react to potential errors
    php.stderr.on('data', function(data) {
            console.log("STDERR: "+data);
    });

    //finalize when php process is done.
    php.on('close', function(code) {
            context.succeed(JSON.parse(output));
    });
}

//local debug only
//exports.handler(JSON.parse("{"hello":"world"}"));
