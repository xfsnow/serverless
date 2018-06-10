var mysql      = require('mysql');
var connection = mysql.createConnection({
      host     : process.env.RDS_HOST,
      user     : process.env.RDS_USERNAME,
      password : process.env.RDS_PASSWORD,
      database : process.env.RDS_DATABASE
    });

exports.handler = function(event, context) {
  console.log("connect to db: "+connection);
  var question_id = (undefined === event.question_id ? 1:event.question_id);

  var sql = "SELECT * FROM raise_hand WHERE question_id="+question_id+" ORDER BY id LIMIT 25";
  console.log(sql);
  connection.query(sql, function (error, results, fields) {
  var out = [];
  var index = 1;
  console.log(error);
  console.log(results);
  console.log(fields);
  for (var i=0 in results)
  {
      results[i].id = index++;
  }
  context.succeed(results);
  });
}
