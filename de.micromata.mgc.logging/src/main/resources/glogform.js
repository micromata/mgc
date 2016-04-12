function glogFormDateToIso(date) {
	return new Date(date).toISOString();
}
function glogIsoDateToForm(date) {
	if (date.length == "2016-02-04T11:30:55.000Z".length) {
		date = date.substring(0, date.length - 1);
		date = date.replace('T', ' ');
	}
	return date;
}

function GlogForm(gLogViewer) {
	this.logViewer = gLogViewer;
	this.formId = gLogViewer.formId;
	this.logLevels = [ 'Debug', 'Info', 'Note', 'Warn', 'Error', 'Trace' ];
	this.startRow = 0;
	this.formDateToIso = glogFormDateToIso;
	this.isoDateToForm = glogIsoDateToForm;
	var _this = this;
	this._attachForm = function() {
		var form = document.getElementById(this.formId);
		form.pageSize.value = gLogViewer.pageSize;
		form.pageSize.addEventListener('change', function(event) {
			// console.debug('Page size changed');
			gLogViewer.pageSize = parseInt(form.pageSize.value);
			_this.refresh();
		});
		form.bufferSize.value = gLogViewer.bufferSize;
		form.toDate.value = this.isoDateToForm(new Date().toISOString());
		var ts = Date.parse(new Date().toISOString());
		ts -= (1000 * 60 * 60 * 3);
		form.fromDate.value = this.isoDateToForm(new Date(ts).toISOString());
		form.bufferSize.addEventListener('change', function(event) {
			gLogViewer.bufferSize = parseInt(form.bufferSize.value);
		});
		form.clearLogListButton.addEventListener('click', function(event) {
			logViewer.clear();
			_this.startRow = 0;
			event.stopPropagation();
			event.preventDefault();

		});
		form.liveViewCheckbox.addEventListener('click', function(event) {
			// logViewer.reset();
		});
		form.filterResetButton.addEventListener('click', function(event) {
			form.filterLogLevel.selectedIndex = 0;
			form.filterMessage.value = '';
			form.filterCategory.value = '';
			form.logAttribute1Type.value = '';
			form.logAttribute2Type.value = '';
			form.logAttribute1Value.value = '';
			form.logAttribute2Value.value = '';
			form.fromDateEnabled.checked = false;
			form.toDateEnabled.checked = false;
			
			_this.startRow = 0;
			event.stopPropagation();
			event.preventDefault();
		});
		form.filterSearchButton.addEventListener('click', function(event) {
			event.stopPropagation();
			event.preventDefault();
			_this.startRow = 0;
			form.liveViewCheckbox.checked = false;
			// console.debug('filterSearch');
			var formData = _this.getFormData();
			_this.logViewer.filterItems(formData);

		});
		this.fillSelects(form)
	}
	this.refresh = function() {
		var formData = _this.getFormData();
		_this.logViewer.filterItems(formData);
	}

	this._refreshSelects = function() {
		// console.debug("_refreshSelects");
		var form = document.getElementById(this.formId);
		var catsSelect = form.filterCategory;
		while (catsSelect.hasChildNodes()) {
			catsSelect.removeChild(catsSelect.lastChild);
		}
		while (form.logAttribute1Type.hasChildNodes()) {
			form.logAttribute1Type.removeChild(form.logAttribute1Type.lastChild);
		}
		while (form.logAttribute2Type.hasChildNodes()) {
			form.logAttribute2Type.removeChild(form.logAttribute2Type.lastChild);
		}
		this.fillSelects(form);
	}
	this.fillSelects = function(form) {
		var logConfig = this.logViewer.logBackend.getLoggingConfiguration();
		var logCats = logConfig.loggingCategories;
		var catsSelect = form.filterCategory;
		var opts = document.createElement('option');
		opts.setAttribute('value', '');
		catsSelect.appendChild(opts);
		for ( var i in logCats) {
			var cat = logConfig.loggingCategories[i];
			opts = document.createElement('option');
			opts.setAttribute('value', cat);
			opts.appendChild(document.createTextNode(cat));
			catsSelect.appendChild(opts);
		}
		fillLogSearchAttributes(form.logAttribute1Type, logConfig.searchAttributes);
		fillLogSearchAttributes(form.logAttribute2Type, logConfig.searchAttributes);
	}

	function fillLogSearchAttributes(formSelect, elements) {
		var opts = document.createElement('option');
		opts.setAttribute('value', '');
		formSelect.appendChild(opts);
		for ( var i in elements) {
			var cat = elements[i];
			opts = document.createElement('option');
			opts.setAttribute('value', cat);
			opts.appendChild(document.createTextNode(cat));
			formSelect.appendChild(opts);
		}
	}
	this.isLiveUpdate = function() {
		var form = document.getElementById(this.formId);
		var ret = form.liveViewCheckbox.checked;
		return ret;
	}
	this.filterItems = function(items) {
		var ret = [];
		for (var i = 0; i < items.length; ++i) {
			if (this.filterItem(items[i]) == true) {
				ret[ret.length] = items[i];
			}
		}
		return ret;
	}

	this.filterItem = function(item) {
		if (this.filterLogLevel(item) == false) {
			return false;
		}
		if (this.filterMessage(item) == false) {
			return false;
		}
		return true;
	}
	this.filterLogLevel = function(item) {
		var form = document.getElementById(this.formId);
		var idx = form.filterLogLevel.selectedIndex;
		if (idx == -1) {
			return true;
		}
		var value = form.filterLogLevel[form.filterLogLevel.selectedIndex].value;
		var minLevel = this.logLevels.indexOf(value);
		var entryLevel = this.logLevels.indexOf(item.logLevel);
		if (minLevel > entryLevel) {
			return false;
		}
		return true;
	}
	this.filterMessage = function(item) {
		var form = document.getElementById(this.formId);
		if (!form.filterMessage) {
			return true;
		}
		var msg = form.filterMessage.value;
		if (!msg) {
			return true;
		}
		var fidx = item.logMessage.indexOf(msg);
		if (fidx == -1) {
			return false;
		}
		return true;
	}
	this.getFormData = function() {

		var ret = new GLogFormData();
		ret.startRow = this.startRow;
		ret.maxRow = this.logViewer.pageSize + 1;
		var form = document.getElementById(this.formId);
		var idx = form.filterLogLevel.selectedIndex;
		if (idx != -1) {
			ret.logLevel = form.filterLogLevel[form.filterLogLevel.selectedIndex].value;
		}
		ret.logMessage = form.filterMessage.value;
		if (form.filterCategory.value != '') {
			ret.logCategory = form.filterCategory.value;
		}
		if (form.logAttribute1Type.value && form.logAttribute1Value.value) {
			ret.logAttribute1Type = form.logAttribute1Type.value;
			ret.logAttribute1Value = form.logAttribute1Value.value;
		}
		if (form.logAttribute2Type.value && form.logAttribute2Value.value) {
			ret.logAttribute2Type = form.logAttribute2Type.value;
			ret.logAttribute2Value = form.logAttribute2Value.value;
		}
		try {
			if (form.toDateEnabled.checked) {
				ret.toDate = this.formDateToIso(form.toDate.value);
			}
		} catch (e) {
			// TODO error message
		}
		try {
			if (form.fromDateEnabled.checked) {
				ret.fromDate = this.formDateToIso(form.fromDate.value);
			}
		} catch (e) {
		// TODO error message
		}

		return ret;
	}

}