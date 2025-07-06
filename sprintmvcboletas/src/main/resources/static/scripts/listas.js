async function loadList(module) {
    switch (module) {
        case "client":
            await listClient();
        break;
        case "boleta":
            await listBoleta();
        break;
        case "usuario":
            await listUsuarios();
        break;
        case "service":
            await listServicios();
        break;
    }
}

async function listClient() {
    const nombre = document.getElementById('nombreInput');
    const apellidos = document.getElementById('apellidosInput');
    const cedula = document.getElementById('cedulaInput');
    const correo = document.getElementById('correoInput');
    const direccion = document.getElementById('direccionInput');
    const contacto = document.getElementById('contactoInput');

    let url = `${urlBase}/clientes/paging?nombre=${nombre ? nombre.value : ''}&apellidos=${apellidos ? apellidos.value : ''}&cedula=${cedula ? cedula.value : ''}&correo=${correo ? correo.value : ''}&direccion=${direccion ? direccion.value : ''}&contacto=${contacto ? contacto.value : ''}`;
    window.location = url;
}

async function listBoleta() {
    const id = document.getElementById('idInput');
    const tenico = document.getElementById('TecnicoInput');
    const cliente = document.getElementById('clienteInput');
    const descripcion = document.getElementById('descripcionInput');
    const productos = document.getElementById('productosInput');
    const finicial = document.getElementById('FInicioInput');
    const ffinal = document.getElementById('FFinalInput');

    let url = `${urlBase}/boletas/paging?id=${id ? id.value : ''}&tecnico=${tenico ? tenico.value : ''}&cliente=${cliente ? cliente.value : ''}&descripcion=${descripcion ? descripcion.value : ''}&productos=${productos ? productos.value : ''}&finial=${finicial ? finicial.value : ''}&ffinal=${ffinal ? ffinal.value : ''}`;
    window.location = url;
}

async function listUsuarios() {
    const nombre = document.getElementById('nombreInput');
    const apellidos = document.getElementById('apellidosInput');
    const cedula = document.getElementById('cedulaInput');
    const correo = document.getElementById('correoInput');
    const role = document.getElementById('roleInput');
    const user = document.getElementById('userInput');

    let url = `${urlBase}/usuarios/paging?nombre=${nombre ? nombre.value : ''}&apellidos=${apellidos ? apellidos.value : ''}&cedula=${cedula ? cedula.value : ''}&correo=${correo ? correo.value : ''}&user=${user ? user.value : ''}&role=${role ? role.value : ''}`;
    window.location = url;
}

async function listServicios() {
    const descripcion = document.getElementById('descripcionInput');
    let url = `${urlBase}/servicio/paging?descripcion=${descripcion ? descripcion.value : ''}`;
    window.location = url;
}

