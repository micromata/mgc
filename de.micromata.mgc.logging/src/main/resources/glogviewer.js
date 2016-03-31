function GLogFormData() {
	this.logLevel = null;
	this.logCategory = null;
	this.logMessage = null;
	this.startRow = null;
	this.maxRow = null;
	this.masterOnly = false;
	this.logAttribute1Type = null;
	this.logAttribute1Value = null;
	this.logAttribute2Type = null;
	this.logAttribute2Value = null;

	this.asMap = function() {
		var ret = {};
		if (this.level != null) {
			ret.level = this.level;
		}
	}
}

function GLogViewer(options) {
	// console.warn("GLogViewer created");
	this.lastPollTime = 0;
	this.options = options;
	this.logListId = options.logListId;
	this.formId = options.formId;
	this.logPollTimeout = options.logPollTimeout;
	this.logPollIsRunning = false, this.buffer = new Array();
	this.bufferSize = 2000;
	this.showInreverseOrder = true;
	if (options.bufferSize) {
		this.bufferSize = options.bufferSize;
	}

	if (options.logBackend) {
		this.logBackend = options.logBackend;
		this.logBackend.init(this);
	} else if (options.backendUrl) {
		this.backendUrl = options.backendUrl;
		this.logBackend = new GLogBackend();
		this.logBackend.init(this);
	} else {
		this.logBackend = new LogDummyBackend();
		this.logBackend.init(this);
	}

	this.enableEmbeddedDebugger = options.enableEmbeddedDebugger;

	if (!this.logPollTimeout) {
		this.logPollTimeout = 1000;
	}

	this.maxItems = options.maxItems;
	if (!this.maxItems) {
		this.maxItems = 1000;
	}
	this.autoScroll = this.showInreverseOrder == false;
	if (this.showInreverseOrder == false && options.autoScroll) {
		this.autoScroll = options.autoScroll;
	}
	this.searchAttributes = options.searchAttributes;

	if (!this.searchAttributes) {
		this.searchAttributes = [];
	}
	this.pageSize = 30;
	if (options.pageSize) {
		this.pageSize = options.pageSize;
	}

	var _this = this;
	this.logForm = null;
	runOnLoad(function() {
		if (_this.formId) {
			_this.logForm = new GlogForm(_this);
			_this.logForm._attachForm();
			// _this.logForm._buildForm();
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
		_startPoll(_this);
	});
	this.setBackend = function(backend) {
		this.logBackend = backend;
		this.logBackend.init(this);
		console.debug("new logBackend initialized: " + this.logBackend);
		_startPoll(this);
	}

	this.clear = function() {
		var ll = document.getElementById(this.logListId);
		while (ll.hasChildNodes()) {
			ll.removeChild(ll.lastChild);
		}
	}
	this.reload = function() {
		// TODO callback

	}

	this._appendEntries = function(entries) {
		this._appendToBuffer(entries);
		this._appendToGui(entries);
	}
	this._appendToGui = function(entries) {
		var liveUpdate = true;
		if (this.logForm) {
			liveUpdate = this.logForm.isLiveUpdate();
			if (liveUpdate) {
				entries = this.logForm.filterItems(entries);
			}
		}
		if (!liveUpdate) {
			return;
		}
		var ll = document.getElementById(this.logListId);
		var htmitem;
		for (var i = 0; i < entries.length; ++i) {
			htmitem = this._buildHtmlItem(entries[i]);
			if (this.showInreverseOrder) {
				if (ll.hasChildNodes()) {
					ll.insertBefore(htmitem, ll.firstChild);
				} else {
					ll.appendChild(htmitem);
				}
			} else {
				ll.appendChild(htmitem);
			}
		}
		if (this.showInreverseOrder == false && this.autoScroll) {
			htmitem.scrollIntoView(true);
		}
	}
	this._appendToBuffer = function(entries) {
		if (!this.buffer) {
			console.debug('buffer not set');
			return;
		}

		if (this.buffer.length + entries.length > this.bufferSize) {
			var rl = this.buffer.length + entries.length - this.bufferSize;
			for (var i = 0; i < rl; ++i) {
				this.buffer.shift();
			}
		}

		for ( var i in entries) {
			var e = entries[i];
			if (!e.logTimestamp) {
				console.warn("No logTimestamp given: " + e);
				continue;
			}
			var lt = e.logTimestamp;

			console.debug('compare: ' + this.logPollTimeout + "; " + lt);
			if (typeof (lt) === 'string' || lt instanceof String) {
				lt = parseLong(lt);
			}
			if (this.lastPollTime < lt) {
				this.lastPollTime = lt;
				console.debug('update polltimestamp: ' + this.lastPollTime);
			}
		}
		this.buffer = this.buffer.concat(entries);
	}
	this.filterItems = function(formData) {
		if (this.logBackend.getLoggingConfiguration().supportsSearch) {
			this.logBackend.logSelect(formData, function(items) {
				console.debug('got from select: ' + items);
				try {
					if (typeof (items) === 'string' || items instanceof String) {
						items = JSON.parse(items);
					}
				} catch (e) {
					console.error("error while parsing: " + e);
				}
				_this.clear();
				_this._appendToGui(items);
			});
		} else {
			console.debug('backend does not support search');
			_this.clear();
			_this._appendToGui(this.buffer);
		}
	}
	this.refreshForm = function() {
		this.logForm._refreshSelects();
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

		m = document.createElement('div');
		m.setAttribute('class', 'logc');
		m.appendChild(document.createTextNode(item.logCategory));
		el.appendChild(m);

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
			var atable = document.createElement('table');
			atable.setAttribute("class", "logattr");
			logattrs.appendChild(atable);

			for (var i = 0; i < item.logAttributes.length; ++i) {

				var la = item.logAttributes[i];
				var tr = document.createElement('tr');
				var td = document.createElement('td');
				td.setAttribute("class", "logattrkey");
				td.appendChild(document.createTextNode(la.typeName));
				tr.appendChild(td);
				td = document.createElement('td');
				td.setAttribute("class", "logattrvalue");
				td.appendChild(document.createTextNode(la.value));
				tr.appendChild(td);
				atable.appendChild(tr);
			}
		}
		return logattrs;
	}
	function _startPoll(_this) {

		if (_this.logPollIsRunning == true) {
			// console.debug('logPollIsRunning');
			return;
		}
		// console.debug('_startPoll');

		_this.logPollIsRunning = true;
		setTimeout(function() {
			_doPoll(_this);
		}, _this.logPollTimeout);
	}
	function _doPoll(_this) {
		// console.debug('inpoll');
		try {
			_this.logBackend.logPoll(_this.lastPollTime, function(entries) {
				try {
					// console.debug("poll received: " + entries);
					if (typeof entries == 'string') {
						entries = JSON.parse(entries);
					}
					if (entries && entries.length > 0) {
						_this._appendEntries(entries);
					}
				} catch (e) {
					console.error('logpoll failed: ' + e);
				}
				_this.logPollIsRunning = false;
				setTimeout(function() {
					_doPoll(_this);
				}, _this.logPollTimeout);
			});
		} catch (e) {
			console.error("logBackend call for poll failed:  " + e);
			_this.logPollIsRunning = false;
			setTimeout(function() {
				_doPoll(_this);
			}, _this.logPollTimeout);
		}
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