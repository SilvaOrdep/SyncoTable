document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById("fileInput");
    const fileUploadArea = document.getElementById("fileUploadArea");
    const uploadPlaceholder = document.getElementById("uploadPlaceholder");
    const fileInfo = document.getElementById("fileInfo");
    const fileName = document.getElementById("fileName");
    const fileSize = document.getElementById("fileSize");
    const removeFileBtn = document.getElementById("removeFile");
    const btnSubmitImport = document.getElementById("btnSubmitImport");
    const browseFileLink = document.getElementById("browseFile");
    const importForm = document.getElementById("importForm");
    const cardTitleInput = document.getElementById("cardTitle");
    const importModal = document.getElementById("importModal");

    // Abrir seletor de arquivo
    browseFileLink.addEventListener("click", function (e) {
        e.preventDefault();
        fileInput.click();
    });

    // Click para abrir seletor
    fileUploadArea.addEventListener("click", function (e) {
        if (e.target === fileUploadArea || e.target.closest("#uploadPlaceholder")) {
            fileInput.click();
        }
    });

    // Drag & drop
    fileUploadArea.addEventListener("dragover", function (e) {
        e.preventDefault();
        fileUploadArea.classList.add("drag-over");
    });

    fileUploadArea.addEventListener("dragleave", function () {
        fileUploadArea.classList.remove("drag-over");
    });

    fileUploadArea.addEventListener("drop", function (e) {
        e.preventDefault();
        fileUploadArea.classList.remove("drag-over");

        const files = e.dataTransfer.files;
        if (files.length > 0) {
            handleFileSelect(files[0]);
        }
    });

    // Mudança do input file
    fileInput.addEventListener("change", function (e) {
        if (e.target.files.length > 0) {
            handleFileSelect(e.target.files[0]);
        }
    });

    // Remover arquivo
    removeFileBtn.addEventListener("click", function () {
        resetFileInput();
    });

    // Reset modal ao fechar
    importModal.addEventListener("hidden.bs.modal", function () {
        resetForm();
    });

    function handleFileSelect(file) {
        const validExtensions = [".xlsx", ".xls", ".csv"];
        const fileExtension = "." + file.name.split(".").pop().toLowerCase();

        if (!validExtensions.includes(fileExtension)) {
            alert("Formato de arquivo inválido. Use .xlsx, .xls ou .csv");
            return;
        }

        // Preenche título com nome do arquivo se estiver vazio
        if (!cardTitleInput.value) {
            cardTitleInput.value = file.name.replace(/\.[^/.]+$/, "");
        }

        // Atualiza UI
        fileName.textContent = file.name;
        fileSize.textContent = formatFileSize(file.size);
        uploadPlaceholder.classList.add("d-none");
        fileInfo.classList.remove("d-none");
        btnSubmitImport.disabled = false;

        // Força o input file a ter o arquivo selecionado
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(file);
        fileInput.files = dataTransfer.files;
    }

    function resetFileInput() {
        fileInput.value = "";
        uploadPlaceholder.classList.remove("d-none");
        fileInfo.classList.add("d-none");
        btnSubmitImport.disabled = true;
    }

    function resetForm() {
        importForm.reset();
        resetFileInput();
        cardTitleInput.value = "";
    }

    function formatFileSize(bytes) {
        if (bytes === 0) return "0 Bytes";
        const k = 1024;
        const sizes = ["Bytes", "KB", "MB", "GB"];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + " " + sizes[i];
    }
});
