const uploadFile = (e) => {
    var input = document.getElementById('fileInput');
    let inputOrignalHtml = input.innerHTML;
    if (!input.files[0]) {
        alert('Select a file please');
        return;
    }
    var data = new FormData();
    [...input.files].forEach(file => data.append('files', file));

    let baseUrl = document.getElementById('baseUrlInput').value;

    let uploadButton = document.getElementById('uploadButton');
    let uploadButtonOrignalHtml = uploadButton.innerHTML;
    uploadButton.textContent = "Uploading ...";
    uploadButton.classList.add('text-white-50');

    fetch(baseUrl + "/exchangeFiles/uploadMultiple", {
        method: 'POST',
        body: data
    })
        .then(response => Promise.all([response.status]))
        .then(function ([status]) {
            if (status == 200) {               
                input.value = "";
                inputFileChangeHandler();
                uploadButton.textContent = "Uploaded !!!";
                uploadButton.classList.remove('text-white-50');
                uploadButton.classList.add('glowing-text');
                setTimeout(() => {
                   
                    uploadButton.innerHTML = uploadButtonOrignalHtml;
                    console.log(uploadButton.classList);
                    uploadButton.classList.remove('glowing-text');

                }, 5000);

            } 
            else {
                input.value = "";
                inputFileChangeHandler();
                uploadButton.textContent = "Upload Failed !!!";
                uploadButton.classList.add('text-danger');
                setTimeout(() => {
                    uploadButton.innerHTML = uploadButtonOrignalHtml;
                    uploadButton.classList.remove('text-danger');

                }, 5000);
            }
        })
}


const inputFileChangeHandler = () => {
    let inputLabel = document.getElementById('fileInputLabel');
    let uploadFileInput = document.getElementById('fileInput');

    let uploadButton = document.getElementById('uploadButton');
    let downloadLinkGeneratorButton = document.getElementById('downloadLinkGeneratorButton');
    // incase no file is selected
    if (uploadFileInput.files.length == 0) {
        inputLabel.innerHTML ='Choose File <span class="material-symbols-outlined">folder_open</span>';
        uploadButton.setAttribute('disabled', 'true');
        downloadLinkGeneratorButton.setAttribute('disabled', 'true');
        if (document.getElementById('qrCodeLink').textContent == document.getElementById('baseUrlInput').value)
        document.getElementById('downloadFileSize').textContent ='';

        return;
    }

    uploadButton.removeAttribute('disabled');
    downloadLinkGeneratorButton.removeAttribute('disabled');

    var uploadInputValue = uploadFileInput.value;
    if (uploadFileInput.files.length == 1) {
        if (!uploadInputValue)
            return;

        uploadInputValue = uploadInputValue.split('\\');
        // removing path value and keeping file name only
        uploadInputValue = uploadInputValue[uploadInputValue.length - 1];
        uploadInputValue = uploadInputValue.split(' ').map(word => word[0].toUpperCase() + word.slice(1, word.length)).join(' ');
        let lastIndexOfDot = uploadInputValue.lastIndexOf('.');
        uploadInputValue = uploadInputValue.substring(0, lastIndexOfDot) + " ( " + uploadInputValue.substring(lastIndexOfDot + 1) + " )";
    }
    else
        uploadInputValue = uploadFileInput.files.length + " Files Selected";

    inputLabel.textContent = uploadInputValue;

}

const getDownloadDetails = () => {
    let baseUrl = document.getElementById("baseUrlInput").value;
    let detailsUrl = "/exchangeFiles/downloadDetails/";
    let uploadFileInput = document.getElementById('fileInput');
    let fileNames = "";
    [...uploadFileInput.files].forEach(file => fileNames += fileNames == "" ? file.name : "," + file.name);
    const finalUrl = baseUrl + detailsUrl + fileNames;

    fetch(finalUrl, { method: "GET" })
        .then(response => Promise.all([response.status, response.json()]))
        .then(function ([status, myJson]) {
            setDownloadDataToUI(myJson);
        });


}

const setDownloadDataToUI = (downloadData) => {

    if (!downloadData)
        return;
//    let qrLink = "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=" + downloadData.downloadUrl +"&chco=e93f64";
    let qrLink = "https://quickchart.io/qr?text=" + downloadData.downloadUrl + "&size=300";
    let qrCode = document.getElementById("qrCode");
    qrCode.setAttribute('src', qrLink);
    let qrCodeLink = document.getElementById('qrCodeLink');
    qrCodeLink.setAttribute('href', downloadData.downloadUrl);
    qrCodeLink.textContent = downloadData.downloadFileName ;
    document.getElementById('downloadFileSize').textContent = ' (' + downloadData.downloadSize + ')';
}


const generateDownloadDetails = (e) => {
    let input = document.getElementById('fileInput');
    if (!input.files[0]) {
        alert('Select a file please');
        return;
    }
    let data = new FormData();
    [...input.files].forEach(file => data.append('files', file));
    // data.append('file', input.files[0]);
    // document.getElementById('loaderContainer').classList.remove('d-none');

    let baseUrl = document.getElementById('baseUrlInput').value;

    let downloadLinkGeneratorButton = document.getElementById('downloadLinkGeneratorButton');
    let generateQrButtonOrignalHTML = downloadLinkGeneratorButton.innerHTML;
    downloadLinkGeneratorButton.textContent = "Generating QR ...";
    downloadLinkGeneratorButton.classList.add("text-white-50");
    fetch(baseUrl + "/exchangeFiles/generateDownloadLink", {
        method: 'POST',
        body: data
    })
        .then(response => Promise.all([response.status, response.json()]))
        .then(function ([status, downloadData]) {
            if (status == 200) {
                
                input.value = "";
                inputFileChangeHandler();
                downloadLinkGeneratorButton.classList.remove("text-white-50");
                downloadLinkGeneratorButton.textContent = "QR Generated !!!";
                downloadLinkGeneratorButton.classList.add('glowing-text');
                setDownloadDataToUI(downloadData);
                setTimeout(() => {
                    downloadLinkGeneratorButton.innerHTML = generateQrButtonOrignalHTML;
                    downloadLinkGeneratorButton.classList.remove('glowing-text');
                    
                }, 5000);


            } else {
                alert("Something went wrong !!!");                

            }
        })
}
const shutDownApp = () => {
    document.getElementById('closedAppContent').classList.remove("d-none");
    document.getElementById('appContent').remove();
    let baseUrl = document.getElementById("baseUrlInput").value;
    try {
        fetch(baseUrl + "/app/shutdown");
    }
    catch {
        
    }
    
}