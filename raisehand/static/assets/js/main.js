/*
	Alpha by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
*/

(function($) {

	skel.breakpoints({
		wide: '(max-width: 1680px)',
		normal: '(max-width: 1280px)',
		narrow: '(max-width: 980px)',
		narrower: '(max-width: 840px)',
		mobile: '(max-width: 736px)',
		mobilep: '(max-width: 480px)'
	});

	$(function() {

		var	$window = $(window),
			$body = $('body'),
			$header = $('#header'),
			$banner = $('#banner');

		// Fix: Placeholder polyfill.
			$('form').placeholder();

		// Prioritize "important" elements on narrower.
			skel.on('+narrower -narrower', function() {
				$.prioritize(
					'.important\\28 narrower\\29',
					skel.breakpoint('narrower').active
				);
			});

		// Dropdowns.
			$('#nav > ul').dropotron({
				alignment: 'right'
			});

		// Off-Canvas Navigation.

			// Navigation Button.
				$(
					'<div id="navButton">' +
						'<a href="#navPanel" class="toggle"></a>' +
					'</div>'
				)
					.appendTo($body);

			// Navigation Panel.
				$(
					'<div id="navPanel">' +
						'<nav>' +
							$('#nav').navList() +
						'</nav>' +
					'</div>'
				)
					.appendTo($body)
					.panel({
						delay: 500,
						hideOnClick: true,
						hideOnSwipe: true,
						resetScroll: true,
						resetForms: true,
						side: 'left',
						target: $body,
						visibleClass: 'navPanel-visible'
					});

			// Fix: Remove navPanel transitions on WP<10 (poor/buggy performance).
				if (skel.vars.os == 'wp' && skel.vars.osVersion < 10)
					$('#navButton, #navPanel, #page-wrapper')
						.css('transition', 'none');

		// Header.
		// If the header is using "alt" styling and #banner is present, use scrollwatch
		// to revert it back to normal styling once the user scrolls past the banner.
		// Note: This is disabled on mobile devices.
			if (!skel.vars.mobile
			&&	$header.hasClass('alt')
			&&	$banner.length > 0) {
				$window.on('load', function() {
					$banner.scrollwatch({
						delay:		0,
						range:		0.5,
						anchor:		'top',
						on:			function() { $header.addClass('alt reveal'); },
						off:		function() { $header.removeClass('alt'); }
					});

				});
			}
	});
	var answer = $('#answer');
	answer.on("click",function(){
		var user_name = $('#user_name').val();
		if (''==user_name)
		{
			alert("Please input your name.");
			return;
		}
		var params = {
		    name: user_name
		};
		var apigClient = apigClientFactory.newClient();
		apigClient.raiseGet(params).then(function(result){
		var data = result.data;
			console.log(data);
        	if (data.msg)
		    {
		    	alert(data.msg);
			}
			else
			{
				alert('You have already raised you hand.');
			}
    	}).catch( function(data){
        	//This is where you would put an error callback
    	});
	});
	// 如果是结果页
	var pageResult = $('#result');
	if (pageResult) {
		var question_id = $.query.get('question_id');
		$('#question_id').text(question_id);
		console.log("result load"+question_id);
		var apigClient = apigClientFactory.newClient();
		console.log(apigClient);
		var params = {
		    question_id: question_id
		};
		apigClient.resultGet(params).then(function(result){
			var data = result.data;
			var list = '';
			for (var i in data)
			{
				var item = data[i];
				list += '<div class="row uniform 50%"><div class="4u 12u(mobilep)">'+item.id+'</div> <div class="8u 12u(mobilep)">'+item.name+'</div></div>';
			}
			$('#list').html(list);
    	}).catch( function(data){
        	//This is where you would put an error callback
    	});
	};
})(jQuery);
