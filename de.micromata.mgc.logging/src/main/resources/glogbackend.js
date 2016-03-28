
function LogDummyBackend(logViewer) {
	this.supportsPoll = false;
	this.supportsSearch = false;
	this.init = function(logViewer) {

	}
	this.logPoll = function(lastPollTime, callback) {
		callback([]);
	}
	this.logSelect = function(logFormData, callback) {

	}
}

function GLogBackend() {

	
	var _this = this;
	this.init = function(logViewer) {

		this.logViewer = logViewer;
		this.backendUrl = logViewer.options.backendUrl;
		this.supportsSearch = null;
	}

	this.logPoll = function(lastPollTime, callback) {
		this._ajax("poll", 'lt=' + lastPollTime, callback);
	};
	this.logSelect = function(logFormData, callback) {

	};
	this._initSupportsSearch = function()
	{
		this._ajax("supportsSearch", null, function(text) {
			_this.supportsSearch = "true" == text;
		});
	}
	this._ajax = function(cmd, data, callback)
	{
		var xmlhttp = new XMLHttpRequest();
		var url = this.backendUrl + "?cmd=" + cmd;
		if (data) {
			url += '&' + data;
		}
		xmlhttp.open("POST", url, true);
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				callback(xmlhttp.responseText);
			}
		};
		xmlhttp.send();
	}
	this._initSupportsSearch();
}