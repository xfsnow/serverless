console.log('starting function')
exports.handle = function(e, ctx, cb) {
  // console.log('processing event: %j', e);
  // cb(null, { face : 'list' });
  var html = '<html><head><title>HTML from API Gateway/Lambda</title></head>' +
        '<body><h1>HTML from API Gateway/Lambda</h1></body></html>';

    ctx.succeed(html);
}
