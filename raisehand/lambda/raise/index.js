var mysql      = require('mysql');
var connection = mysql.createConnection({
      host     : process.env.RDS_HOST,
      user     : process.env.RDS_USERNAME,
      password : process.env.RDS_PASSWORD,
      database : process.env.RDS_DATABASE
    });

exports.handler = function(event, context) {
  var name = (undefined === event.name ? '':event.name);
  var sql = "SELECT question_id FROM question_id";
  connection.query(sql, function (error, results, fields) {
    var question_id = results[0]['question_id'];
    //  console.log(results);
    //  context.succeed(results);
    var sql = "INSERT INTO `raise_hand` (`name`,`question_id`) VALUES ('"+name+"', '"+question_id+"')";
    connection.query(sql, function (error, results, fields) {
        console.log(error);
        console.log(results);
        console.log(fields);

        if (results.affectedRows>0)
        {
            var out = {
              'error' : '20000',
              'msg' : 'Thank you for raising your hand!'
            };
        }
        else
        {
            var out = {
              'error' : '40601',
              'msg' : 'You have already participated.'
             };
        }
        context.succeed(out);
      });
  });
}
