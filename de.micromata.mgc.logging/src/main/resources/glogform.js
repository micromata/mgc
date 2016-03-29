function GlogForm(gLogViewer) {
	this.logViewer = gLogViewer;
	this.formId = gLogViewer.formId;
	this.logLevels = [ 'Debug', 'Info', 'Note', 'Warn', 'Error', 'Trace' ];
	var _this = this;
	this._attachForm = function() {
		var form = document.getElementById(this.formId);

		form.clearLogListButton.addEventListener('click', function(event) {
			logViewer.clear();
			event.stopPropagation();
			event.preventDefault();
		});
		form.liveViewCheckbox.addEventListener('click', function(event) {
//			logViewer.reset();
		});
		form.filterResetButton.addEventListener('click', function(event) {
			form.filterLogLevel.selectedIndex = 0;
			form.filterMessage.value = '';
			form.filterCategory.value = '';
			form.logAttribute1Type.value = '';
			form.logAttribute2Type.value = '';
			form.logAttribute1Value.value = '';
			form.logAttribute2Value.value = '';
			
			event.stopPropagation();
			event.preventDefault();
		});
		form.filterSearchButton.addEventListener('click', function(event) {
			event.stopPropagation();
			event.preventDefault();
			var formData = _this.getFormData();
			_this.logViewer.filterItems(formData);
			
		});
		var logConfig = logViewer.logBackend.loggingConfiguration;
		var logCats = logConfig.loggingCategories;
		var catsSelect = form.filterCategory;
		var opts = document.createElement('option');
		opts.setAttribute('value', '');
		catsSelect.appendChild(opts);
		for (var i in logCats) {
			var cat = logConfig.loggingCategories[i];
			opts = document.createElement('option');
			opts.setAttribute('value', cat);
			opts.appendChild(document.createTextNode(cat));
			catsSelect.appendChild(opts);
		} 
		fillLogSearchAttributes(form.logAttribute1Type, logConfig.searchAttributes);
		fillLogSearchAttributes(form.logAttribute2Type, logConfig.searchAttributes);
	}
	function fillLogSearchAttributes(formSelect, elements)
	{
		var opts = document.createElement('option');
		opts.setAttribute('value', '');
		formSelect.appendChild(opts);
		for (var i in elements) {
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
	this.getFormData = function()
	{
		
		var ret = new GLogFormData();
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
			ret.logAttribute1Type =form.logAttribute1Type.value;
			ret.logAttribute1Value =form.logAttribute1Value.value;
		}
		if (form.logAttribute2Type.value && form.logAttribute2Value.value) {
			ret.logAttribute2Type =form.logAttribute2Type.value;
			ret.logAttribute2Value =form.logAttribute2Value.value;
		}
		return ret;
	}
}