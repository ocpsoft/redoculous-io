if (!window.redoculoustags) {
	var redoculoustags = {};
}

if (!redoculoustags.poll) {
	redoculoustags.poll = {};
}

if (!redoculoustags.poll.init) {
	redoculoustags.poll.init = function init(id, inc, to, rend) {
		var componentID = id;
		var increment = inc;
		var timeout = to;
		var elapsed = 0;
		var token = null;
		var render = rend;

		// If increment isn't set, or it's less than some reasonable value (50)
		// default to 200ms
		if (isNaN(increment) || increment <= 50) {
			increment = 200;
		}

		// If timeout isn't set, default to no timeout
		if (isNaN(timeout) || timeout == 0) {
			timeout = -1;
		}

		var poll = function poll() {
			jsf.ajax.request(componentID, null, {
				render : render
			});
			if (timeout != -1) {
				// Not an accurate timeout - but simple to compute
				elapsed += increment;
				if (elapsed > timeout) {
					window.clearInterval(token);
				}
			}
		};

		token = window.setInterval(poll, increment);

		return function cancelPoll(data) {
			if (data.source.id == componentID) {
				window.clearInterval(token);
			}
		};
	}
}