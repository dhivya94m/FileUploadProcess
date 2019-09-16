'use strict';

var fileUploadForm = document.querySelector('#fileUploadForm');
var fileUploadInput = document.querySelector('#fileUploadInput');
var fileUploadError = document.querySelector('#fileUploadError');
var fileUploadSuccess = document.querySelector('#fileUploadSuccess');

var getPagedDataFrm = document.querySelector('#getPagedDataFrm');

function uploadFile(file) {
	var formData = new FormData();
	formData.append("file", file);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/uploadFile");
	xhr.onload = function() {
		console.log(xhr.responseText);
		var response = JSON.parse(xhr.responseText);
		if (xhr.status == 200) {
			fileUploadError.style.display = "none";
			fileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p>";
			fileUploadSuccess.style.display = "block";
			getPagedDataFrm.submit();
		} else {
			fileUploadSuccess.style.display = "none";
			fileUploadError.innerHTML = (response && response.message)
					|| "<p>Problem Occoured. Please Try Again.</p>";
		}
	}

	xhr.send(formData);
}

fileUploadForm.addEventListener('submit', function(event) {
	var files = fileUploadInput.files;
	if (files.length === 0) {
		fileUploadError.innerHTML = "Please select a file";
		fileUploadError.style.display = "block";
		return;
	}
	uploadFile(files[0]);
	event.preventDefault();
}, true);

function refreshPage() {
	getPagedDataFrm.submit();
}