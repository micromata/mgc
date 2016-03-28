function GLogViewerForm() {
	this.logLevel = null;
	this.logCategory = null;
}
function GLogViewer(options) {
	// console.warn("GLogViewer created");
	this.options = options;
	this.logListId = options.logListId;
	this.formId = options.formId;
	this.logFilterCallback = options.logFilterCallback;
	this.logPollCallback = options.logPollCallback;
	this.logPollTimeout = options.logPollTimeout;
	this.logPollIsRunning = false, this.buffer = null;
	this.bufferSize = options.bufferSize;
	this.enableEmbeddedDebugger = options.enableEmbeddedDebugger;
	if (this.bufferSize) {
		buffer = new Array();
	}
	if (!this.logPollTimeout) {
		this.logPollTimeout = 1000;
	}

	this.maxItems = options.maxItems;
	if (!this.maxItems) {
		this.maxItems = 1000;
	}
	this.autoScroll = options.autoScroll;
	if (options.autoScroll == undefined) {
		options.autoScroll = true;
	}
	this.searchAttributes = options.searchAttributes;

	if (!this.searchAttributes) {
		this.searchAttributes = [];
	}
	this.logentries = [];

	var _this = this;
	this.logForm = null;
	runOnLoad(function() {
		if (_this.formId) {
			_this.logForm = new GlogForm(_this);
			_this.logForm._buildForm(_this);
		}
		if (_this.enableEmbeddedDebugger) {
			if (!document.getElementById('FirebugLite')) {

				E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;
				E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');
				E['setAttribute']('id', 'FirebugLite');
				E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened,enableTrace=true');
				E['setAttribute']('FirebugLite', '4');
				(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);
				E = new Image;
				E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');
			}
		}
		if (_this.logPollCallback) {
			_startPoll(_this);
		}
	});
	this.setPollingCallback = function(callback) {
		console.debug("New callback set: " + callback);
		var oldcallback = this.logPollCallback;
		this.logPollCallback = callback;
		_startPoll(this);

	}
	this.clear = function() {
		var ll = document.getElementById(this.logListId);
		// TODO remove not correct:
		for (var i = 0; i < ll.childNodes.length; ++i) {
			ll.removeChild(ll.childNodes[i]);
		}
	}
	this.reload = function() {
		// TODO callback

	}

	this._appendEntries = function(entries) {
		this._appendToBuffer(entries);
		if (this.logForm) {
			entries = this.logForm.filterItems(entries);
		}
		var ll = document.getElementById(this.logListId);
		var htmitem;
		for (var i = 0; i < entries.length; ++i) {
			htmitem = this._buildHtmlItem(entries[i]);
			ll.appendChild(htmitem);
		}
		if (htmitem) {
			htmitem.scrollIntoView(true);
		}
	}
	this._appendToBuffer = function(entries) {
		if (!this.buffer) {
			return;
		}
		if (this.buffer.length + entries.length > this.bufferSize) {
			var rl = this.buffer.length + entries.length - this.bufferSize;
			for (var i = 0; i < rl; ++i) {
				this.buffer.shift();
			}
		}
		this.buffer = this.buffer.concat(entries);
	}
	function truncItems(_this, num) {
		var ll = document.getElementById(_this.logListId);
		for (var i = 0; i < num; ++i) {
			if (ll.childNodes.length > 0) {
				ll.removeChild(ll.childNodes[0]);
			}
		}
	}
	this._buildHtmlItem = function(item) {
		var mel = document.createElement('div');
		var logeclass = 'loge loge' + item.logLevel;
		mel.setAttribute('class', logeclass);

		var el = document.createElement('div');
		el.setAttribute('class', 'logh');

		var m = document.createElement('div');
		m.setAttribute('class', 'logt');
		m.appendChild(document.createTextNode(item.logTime));
		el.appendChild(m);

		m = document.createElement('div');
		m.setAttribute('class', 'logl');
		m.appendChild(document.createTextNode(item.logLevel));
		el.appendChild(m);
		// TODO cat
		m = document.createElement('div');
		m.setAttribute('class', 'logm');
		m.appendChild(document.createTextNode(item.logMessage));
		el.appendChild(m);

		mel.appendChild(el);
		var attrdiv = this._buildHtmlItemAtt(item);
		mel.appendChild(attrdiv);
		el.addEventListener('dblclick', function(event) {
			if (attrdiv.style.display != 'none') {
				attrdiv.style.display = 'none';
			} else {
				attrdiv.style.display = 'block';
			}
		});
		return mel;
	}
	this._buildHtmlItemAtt = function(item) {
		var logattrs = document.createElement("div");
		logattrs.setAttribute("class", "logattrs");
		logattrs.setAttribute("style", "display: none");
		if (item.logAttributes) {
			for (var i = 0; i < item.logAttributes.length; ++i) {
				var la = item.logAttributes[i];
				var attr = document.createElement("div");
				attr.setAttribute("class", "logattr");
				var attrkey = document.createElement("div");
				attrkey.setAttribute("class", "logattrkey");
				attrkey.appendChild(document.createTextNode(la.typeName));
				attr.appendChild(attrkey);
				var attrvalue = document.createElement("div");
				attrvalue.setAttribute("class", "logattrvalue");
				attrvalue.appendChild(document.createTextNode(la.value));
				attr.appendChild(attrvalue);
				logattrs.appendChild(attr);
			}
		}
		return logattrs;
	}
	function _startPoll(_this) {
		if (_this.logPollIsRunning == true) {
			return;
		}
		_this.logPollIsRunning = true;
		setTimeout(function() {
			_doPoll(_this);
		}, _this.logPollTimeout);
	}
	function _doPoll(_this) {
		console.debug('inpoll');
		try {
			if (!_this.logPollCallback) {
				_this.logPollIsRunning = false;
				return;
			}
			var entries = _this.logPollCallback();
			console.debug("poll received: " + entries);
			if (typeof entries == 'string') {
				entries = JSON.parse(entries);
			}
			if (entries && entries.length > 0) {
				_this._appendEntries(entries);
			}
		} catch (e) {
			console.error('logpoll failed: ' + e);
		}
		setTimeout(function() {
			_doPoll(_this);
		}, _this.logPollTimeout);
	}

	function runOnLoad(func) {
		if (window.attachEvent) {
			window.attachEvent('onload', func);
		} else if (window.addEventListener) {
			window.addEventListener('load', func, false);
		} else {
			document.addEventListener('load', func, false);
		}
	}

}