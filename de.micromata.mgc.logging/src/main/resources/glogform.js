function GlogForm(gLogViewer) {
	this.logViewer = gLogViewer;
	this.formId = gLogViewer.formId;
	this.logLevels = [ 'Debug', 'Info', 'Note', 'Warn', 'Error', 'Trace' ];
	var _this = this;
	this._buildForm = function() {
		var form = document.getElementById(this.formId);
		if (form == null) {
			console.warn("Form ID not found: " + this.formId);
			return;
		}
		form.setAttribute('name', 'logform');
		var clearButton = document.createElement('button');
		clearButton.appendChild(document.createTextNode('Clear'));
		clearButton.addEventListener('click', function(event) {
			logViewer.clear();
			event.stopPropagation();
			event.preventDefault();
		});
		form.appendChild(clearButton);
		var loglevelselect = document.createElement('select');
		loglevelselect.setAttribute('name', 'logform_loglevel');

		for ( var i in this.logLevels) {
			var ll = this.logLevels[i];
			var leveloption = document.createElement('option');
			leveloption.appendChild(document.createTextNode(ll));
			loglevelselect.appendChild(leveloption);

		}
		form.appendChild(loglevelselect);
		
		var reloadButton = document.createElement('button');
		reloadButton.appendChild(document.createTextNode('Reload'));
		reloadButton.addEventListener('click', function(event) {
			logViewer.reload();
			event.stopPropagation();
			event.preventDefault();
		});
		form.appendChild(reloadButton);
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
		return true;
	}
	this.filterLogLevel = function(item) {
		var form = document.getElementById(this.formId);
		var idx = form.logform_loglevel.selectedIndex;
		if (idx == -1) {
			return true;
		}
		var value = form.logform_loglevel[form.logform_loglevel.selectedIndex].value;
		var minLevel = this.logLevels.indexOf(value);
		var entryLevel = this.logLevels.indexOf(item.logLevel);
		if (minLevel > entryLevel) {
			return false;
		}
		return true;
	}
}