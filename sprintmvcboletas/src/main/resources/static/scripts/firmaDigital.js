function loadFirmD() {
    const canvas = document.getElementById("firmaD");
    const btnLimpiar = document.getElementById("limpiar");

    const contexto = canvas.getContext("2d");
    const colorFondo = "grey";
    const colorPincel = "black";
    const grosor = 2;
    let xB = 0, yB = 0, xN = 0, yN = 0;
    const getXReal = (clientX) => clientX - canvas.getBoundingClientRect().left;
    const getYReal = (clientY) => clientY - canvas.getBoundingClientRect().top;
    let startDraft = false;   


    btnLimpiar.addEventListener("click", evento => {
        contexto.fillStyle = colorFondo;
        contexto.fillRect(0, 0, canvas.width, canvas.height);
    });

    canvas.addEventListener("mousedown", evento => {
        xB = xN;
        yB = yN;
        xN = getXReal(evento.clientX);
        yN = getYReal(evento.clientY);
        contexto.beginPath();
        contexto.fillStyle = colorPincel;
        contexto.fillRect(xN, yN, grosor, grosor);
        contexto.closePath();
        startDraft = true;
    });

    canvas.addEventListener("mousemove", evento => {
        if (!startDraft)
            return;

        xB = xN;
        yB = yN;
        xN = getXReal(evento.clientX);
        yN = getYReal(evento.clientY);
        contexto.beginPath();
        contexto.fillStyle = colorPincel;
        contexto.fillRect(xN, yN, grosor, grosor);
        contexto.strokeStylye = colorPincel;
        contexto.lineWidth = grosor
        contexto.stroke();
        contexto.closePath();
    });

    ["mouseup", "mouseout"].forEach(nameEvent => {
        canvas.addEventListener(nameEvent, () => {
            startDraft = false;
        });
    });
}

async function loadFirmBase(){
    const image = document.getElementById("firmaD");    
    const input = document.getElementById("firmData");
    image.src =  `data:image/png;base64,${input.value}`;       
}

