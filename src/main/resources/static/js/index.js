let inputLabelOriginalHtml = document.getElementById('file-input-label')?.innerHTML;

const uiElements = () =>{
    let uiElements = {
        uploadButton : document.getElementById('btn-upload'),
        qrButton : document.getElementById('btn-generate-qr'),
        qrTextContainer : document.getElementById("qr-btn-txt"),
        inputLabel : document.getElementById('file-input-label'),
        uploadFileInput : document.getElementById('file-input'),
        baseUrl : document.getElementById("baseUrlInput"),
        linkContainerInput : document.getElementById("link-container-input"),
        copyTextContainer : document.getElementById("copyTextContainer"),
        toast : document.getElementById("toast"),
        qrCode : document.getElementById("qrCode"),
        qrCodeLink : document.getElementById('qrCodeLink'),
        hiddenLinkInput : document.getElementById('link-container-input'),
        downloadFileSizeLabel : document.getElementById('downloadFileSize'),
        uploadButtonTextContainer : document.getElementById('upload-btn-txt'),
        loader : document.getElementById('loader'),
        shutdownModal : document.getElementById('shutdownModal'),
        archiveFileNameInput : document.getElementById('archiveFileNameInput'),
        archiveFileNameModal : document.getElementById('archiveFileNameModal'),
        tooltipText : document.getElementById('tooltip-text'),
        closedAppHtml : '<span>Application Closed </span><i class="fa-regular fa-face-dizzy"></i>',
        reopenAppHtml : '<span class="text-lime-100 drop-shadow">Please Restart The Application </span><i class="fa-regular fa-lightbulb text-lime-100 drop-shadow"></i>'
    }

    return uiElements;
}

const getQrCodeUrl = (urlToEmbed) =>{
    if(urlToEmbed.endsWith('/')){
        urlToEmbed = urlToEmbed.substring(0, urlToEmbed.length-1);
    }
    return "https://quickchart.io/qr?text=" + urlToEmbed + "&light=f8ffe0&dark=202117&margin=0&size=500";
}

const enableButtons = () =>{
    let disabledStyleClasses = ["pointer-events-none","opacity-50"];
    let {uploadButton, qrButton} = uiElements();
    uploadButton.classList.remove(...disabledStyleClasses);
    qrButton.classList.remove(...disabledStyleClasses);
}

const disableButtons = () =>{
    let disabledStyleClasses = ["pointer-events-none","opacity-50"];
    let {uploadButton, qrButton} = uiElements();
    uploadButton.classList.add(...disabledStyleClasses);
    qrButton.classList.add(...disabledStyleClasses);
    resetButtonTexts();
}

const resetButtonTexts = () =>{
    let {uploadButtonTextContainer, qrTextContainer, inputLabel} = uiElements();
    qrTextContainer.textContent = "Generate QR ";
    uploadButtonTextContainer.textContent = "Upload ";
    inputLabel.innerHTML = inputLabelOriginalHtml;
}

const inputFileChangeHandler = () => {
    let{uploadFileInput, inputLabel, qrCode} = uiElements();
    // When user selects a file and then again clicks the button and cancel it
    if (uploadFileInput.files.length == 0) {
        disableButtons();
        return;
    }

    var uploadInputValue = uploadFileInput.value;
    if (uploadFileInput.files.length == 1) {
        let fileName = uploadInputValue.split('\\');
        fileName = fileName[fileName.length-1];
         let fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length);
         fileName.replace(fileExtension,'');
         if(fileName.length >15){
            fileName = fileName.substring(0,15) + '...';
         }
         fileName = fileName + ' ( '+ fileExtension +' )';
          uploadInputValue = fileName;
    }
    else{
        uploadInputValue = uploadFileInput.files.length + " Files Selected";
    }
    inputLabel.innerHTML = uploadInputValue;

    enableButtons();

    let baseUrl = window.location.href;
    if(qrCode.getAttribute('src') !== getQrCodeUrl(baseUrl)){
        let qrLink = getQrCodeUrl(baseUrl);
        let {qrCodeLink, linkContainerInput, downloadFileSizeLabel} = uiElements();
        qrCode.setAttribute('src', qrLink);
        qrCodeLink.textContent = baseUrl;
        linkContainerInput.value = baseUrl;
        downloadFileSizeLabel.textContent = '';
    }

}

const setDownloadDataToUI = (downloadData) => {

    if (!downloadData)
        return;
    console.log('downloadData',downloadData);
    let qrLink = getQrCodeUrl(downloadData.qrUrl);
    let {qrCode, qrCodeLink, linkContainerInput, downloadFileSizeLabel} = uiElements();
    qrCode.setAttribute('src', qrLink);
    qrCodeLink.textContent = downloadData.downloadFileName;
    linkContainerInput.value = downloadData.qrUrl;
    downloadFileSizeLabel.textContent = 'File Size : ' + downloadData.downloadSize;
}


const generateDownloadDetails = () => {

    let {uploadFileInput, archiveFileNameInput} = uiElements();
    if (!uploadFileInput.files[0]) {
        alert('Select a file please');
        return;
    }
    // if we don't have the name, we'll return
    let archiveFileName = archiveFileNameInput.value;
    let baseUrl = window.location.href;
    let generateLinkUrl = baseUrl + "exchangeFiles/generateDownloadLink";
    if(uploadFileInput.files.length > 1){
        if(!archiveFileName){
            archiveFileNameModal.showModal();
            archiveFileNameInput.value = 'My Space Download';
            archiveFileNameInput.select();
            return;
        }
        closeModal();
        // user has provided the zip file name
        archiveFileNameInput.value = '';
        archiveFileName += archiveFileName.endsWith('.zip') ? '' : '.zip';
        generateLinkUrl = baseUrl + "exchangeFiles/generateDownloadLinkV2/" + archiveFileName;
    }

    showSpinner();
    let data = new FormData();
    [...uploadFileInput.files].forEach(file => data.append('files', file));


    let qrTextContainer = document.getElementById("qr-btn-txt");
    qrTextContainer.textContent = "Generating QR ...";

    fetch(generateLinkUrl, {
        method: 'POST',
        body: data
    }).then(response => Promise.all([response.status, response.json()]))
        .then(function ([status, downloadData]) {
            if (status == 200) {
                qrTextContainer.textContent = "QR Generated";
                setDownloadDataToUI(downloadData);
                // Clear the input as qr is generated now
                uploadFileInput.value ='';
                inputFileChangeHandler();
            } else {
                alert("Something went wrong !!!");
            }
            hideSpinner();
        })
}

const uploadFile = (e) => {
    showSpinner();
    let {uploadFileInput, uploadButtonTextContainer, uploadButton} = uiElements();
    if (!uploadFileInput.files[0]) {
        alert('Select a file please');
        return;
    }
    var data = new FormData();
    [...uploadFileInput.files].forEach(file => data.append('files', file));

    let baseUrl =  window.location.href;
    uploadButtonTextContainer.textContent = "Uploading ...";

    fetch(baseUrl + "exchangeFiles/uploadMultiple", {
        method: 'POST',
        body: data
    })
    .then(response => Promise.all([response.status]))
    .then(function ([status]) {
        if (status == 200) {
            uploadButtonTextContainer.textContent = "Uploaded";
             uploadFileInput.value ='';
             inputFileChangeHandler();
        }
        else {
            uploadButtonTextContainer.textContent = "Upload Failed ";
            uploadButton.classList.add('border-red-800', 'bg-[#ff939373]', 'hover:bg-[#ff00007a]');
        }
        hideSpinner();
    })
}

const shutDownApp = () => {
    let baseUrl = window.location.href;
    let {shutdownModal, closedAppHtml, reopenAppHtml} = uiElements();
    showSpinner();
     // This api call will shutdown th server and
    // hence we'll never get any response and enter catch block
    fetch(baseUrl + "app/shutdown").then(response=>console.log('response', response)).catch((err)=>{
        shutdownModal.classList.add('text-3xl', 'backdrop:bg-[#161712e0]', 'bg-[#fbfff39c]', 'px-12', 'py-10');
        shutdownModal.innerHTML = closedAppHtml;
        const closedAppEventHandler = (event) =>{
            event.preventDefault();
            shutdownModal.innerHTML = reopenAppHtml;
            setTimeout(()=>{
                shutdownModal.innerHTML = closedAppHtml;
            }, 4000);
        }
        document.addEventListener('keydown', closedAppEventHandler);
        document.addEventListener('click', closedAppEventHandler);
        hideSpinner();
    });
}

const showModal = () => {
    let {shutdownModal} = uiElements();
    shutdownModal.showModal();
}
const closeModal = () => {
    [...document.querySelectorAll('dialog')]?.filter(d=>d.open)[0]?.close();
}
const copyLink = () => {
  let {linkContainerInput, tooltipText, copyTextContainer, qrCode, toast} = uiElements();
  linkContainerInput.select();
  linkContainerInput.setSelectionRange(0, linkContainerInput.value.length); // For mobile devices
  document.execCommand('copy');
  document.activeElement.blur();
  let originalHtml = tooltipText.innerHTML;
  tooltipText.innerHTML = 'Copied <i class="fa-solid fa-check"></i>';
  toast.classList.add('show');
  const postCopyHandler = () =>{
    tooltipText.innerHTML = originalHtml;
    tooltipText.classList.add('hidden');
    toast.classList.remove('show');
  }
  setTimeout(postCopyHandler,2000);
}
const copyLinkMouseExitHandler= () => {
    var {tooltipText, qrButton} = uiElements();
    tooltipText.classList.remove('hidden');
    if(isMobileDevice()){
        qrButton.focus();
    }
}

const showSpinner = () => {
    let {loader}=  uiElements();
    loader.showModal();
}

const hideSpinner = () => {
    let {loader} = uiElements();
    loader.close();
}

function isMobileDevice() {
  return window.matchMedia('(max-width: 768px)').matches;
}