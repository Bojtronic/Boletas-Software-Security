//Tablas
//-------------------------------------------------------------------------------------------------------
//redirecciona entre detalle o el editar
function redirect(id, op, module) {
    switch (module) {
        case "client":
            redirectClient(id, op);
            break;
        case "boleta":
            redirectBoletas(id, op);
            break;
        case "usuario":
            redirectUsuario(id, op);
            break;
        case "service":
            redirectServicios(id, op);
            break;
        case "product":
            redirectProductos(id, op);
            break;
    }
}

//emite un confirm y si se acepta
//utiliza el api dependiendo del modulo
async function deleteRow(id, module) {
    let text = "¿Está seguro que desea eliminar el registro?";
    //console.log(id);
    if (confirm(text) == true) {
        switch (module) {
            case "client":
                deleteClient(id);
                break;
            case "boleta":
                deleteBoleta(id);
                break;
            case "usuario":
                deleteUsuario(id);
                break;
            case "service":
                deleteServicio(id);
                break;
             case "product":
                deleteProducto(id);
                break;
        }
    }
}

async function showHiddePassword() {
    var pass = document.getElementById('passwordInput');
    var svg = document.getElementById('useSVGpass');
    if (pass.type == "text") {
        pass.type = "password";
        svg.setAttribute("href", "#showEve")
    }

    else if (pass.type == "password") {
        pass.type = "text";
        svg.setAttribute("href", "#hiddeEve")
    }
}


function reOrdenar(array) {
    let posicionActual = array.length;

    while (0 !== posicionActual) {
        const posicionAleatoria = Math.floor(Math.random() * posicionActual);
        posicionActual--;
        [array[posicionActual], array[posicionAleatoria]] = [
            array[posicionAleatoria], array[posicionActual]];
    }
    return array;
}

function generarPasswordText() {
    const caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
    const numeros = "1234567890".split("");
    const simbolos = "#!&*".split("");
    reOrdenar(caracteres);
    reOrdenar(numeros);
    reOrdenar(simbolos);
    const lote1 = caracteres.slice(0, Math.floor(Math.random() * (10 - 5) + 5)).join("");
    const lote2 = numeros.slice(0, 2).join("");
    const lote3 = simbolos.slice(0, 3).join("");
    const lote4 = (lote1 + lote2 + lote3).split("");
    reOrdenar(lote4);
    return lote4.join("");
}

//generar Password
async function generarPassword() {
    var pass1 = document.getElementById('passwordInput');
    var pass2 = document.getElementById('password2Input');

    const password = generarPasswordText();
    pass1.value = password;
    pass2.value = password;
}

//DropDowns
//-------------------------------------------------------------------------------------------------------
async function selectClient(id) {
    const response = await fetch(`${urlBase}/api/cliente/${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });

    const json = await response.json();
    if (json.success) {
        var _id = document.getElementById('clienteInput');
        var nombre = document.getElementById('NombreClienteInput');
        var ncontacto = document.getElementById('nContactoClienteInput');
        var direccion = document.getElementById('DireccionClienteInput');
        var correo = document.getElementById('CorreoClienteInput');

        _id.value = json.data.id;
        nombre.value = `${json.data.nombre + ' ' + json.data.apellidos}`;
        direccion.value = json.data.direccion;
        ncontacto.value = json.data.numContacto;
        correo.value = json.data.correo;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function selectTecnico(id) {
    const response = await fetch(`${urlBase}/api/usuario/${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });

    const json = await response.json();
    if (json.success) {
        var _id = document.getElementById('tecnicoInput');
        var nombre = document.getElementById('NombreTecnicoInput');

        _id.value = json.data.id;
        nombre.value = `${json.data.nombre + ' ' + json.data.apellidos}`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function selectRole(roleName) {
    var nombre = document.getElementById('roleInput');
    var dRole = document.getElementById('dropRole');
    nombre.value = roleName == "None" ? "" : roleName;
    dRole.innerText = roleName;
}

function selectServicio(id, nombre) {
    var _id = document.getElementById('servicioInput');
    var _nombre = document.getElementById('servicioDescrInput');

    _id.value = id;
    _nombre.value = nombre;

}

function selectProductos(id, nombre, precio) {
    const exist = productos.findIndex((p) => p.id == id) != -1;
    if (!exist) {
        productos.push({ id: id, nombre: nombre, precio: precio });
        rechargeProductos();
    }

}

function deleleProducto(id) {
    if (id == -1)
        return;

    productos = productos.filter((p) => p.id != id);

    rechargeProductos();
}

function rechargeProductos() {
    var list = document.getElementById('listProd');
    var items = "";
    productos.map((p) => {
        var template = `<li id="${'itemProd' + p.id}" class="list-group-item d-flex justify-content-between align-items-center">${p.nombre}<span class="badge bg-primary rounded-pill">${p.precio}</span><button class="btnSVG" onclick="deleleProducto(${p.id})"><svg class="bi mx-2" viewBox="0 0 18 18" width="2em" title="Eliminar"><use xlink:href="#eliminar" /></svg></button></li>`;
        items += template;

    });
    if (productos.length == 0) {
        list.innerHTML = `<li id="itemProd" class="list-group-item d-flex justify-content-between align-items-center">-- sin productos --<span class="badge bg-primary rounded-pill">$0.00</span></li>`;;
    }
    else if (productos.length > 0) {
        list.innerHTML = items;
    }
}
//-------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------

//Clientes
//-------------------------------------------------------------------------------------------------------
function redirectClient(id, op) {
    switch (op) {
        case "list":
            window.location = `${urlBase}/clientes/paging?nombre=&apellidos=&cedula=&correo=&direccion=&contacto=`;
            break;
        case "detail":
            window.location = `${urlBase}/cliente/detalle/${id}`;
            break
        case "nuevo":
            window.location = `${urlBase}/cliente/nuevo`;
            break;
        case "edit":
            window.location = `${urlBase}/cliente/update/${id}`;
            break;
    }
}

async function nuevoClient() {
    var nombreIn = document.getElementById('nombreInput');
    var apellidosIn = document.getElementById('apellidosInput');
    var cedulaIn = document.getElementById('cedulaInput');
    var correoIn = document.getElementById('correoInput');
    var direccionIn = document.getElementById('direccionTextarea');
    var contactoIn = document.getElementById('nContactoInput');

    let valid = validateFormularioCliente(nombreIn, apellidosIn, cedulaIn, correoIn, direccionIn, contactoIn);

    if (valid) {
        await newClient({
            nombre: nombreIn.value, apellidos: apellidosIn.value,
            cedula: cedulaIn.value, correo: correoIn.value, direccion: direccionIn.value,
            numContacto: contactoIn.value
        })
    }
}

async function updateCliente(id) {
    var nombreIn = document.getElementById('nombreInput');
    var apellidosIn = document.getElementById('apellidosInput');
    var cedulaIn = document.getElementById('cedulaInput');
    var correoIn = document.getElementById('correoInput');
    var direccionIn = document.getElementById('direccionTextarea');
    var contactoIn = document.getElementById('nContactoInput');

    let valid = validateFormularioCliente(nombreIn, apellidosIn, cedulaIn, correoIn, direccionIn, contactoIn);

    if (valid) {
        await updateClient(id, {
            nombre: nombreIn.value, apellidos: apellidosIn.value,
            cedula: cedulaIn.value, correo: correoIn.value, direccion: direccionIn.value,
            numContacto: contactoIn.value
        })
    }
}

function validateFormularioCliente(nombreIn, apellidosIn, cedulaIn, correoIn, direccionIn, contactoIn) {
    let valid = true;
    //nombre
    //--------------------------------------------------------------------
    let error = validarInput(nombreIn, 3, 25, null, true);
    let nombreError = document.getElementById('nombreError');
    nombreError.innerText = "";
    if (error) {
        nombreError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    //apellidos
    //--------------------------------------------------------------------
    error = validarInput(apellidosIn, 3, 25, null, true);
    let apellidoError = document.getElementById('apellidoError');
    apellidoError.innerText = "";
    if (error) {
        apellidoError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    //cedula
    //--------------------------------------------------------------------
    error = validarInput(cedulaIn, 11, -1, /^[1-9]-\d{4}-\d{4}$/, true);
    let cedulaError = document.getElementById('cedulaError');
    cedulaError.innerText = "";
    if (error) {
        cedulaError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    //correo
    //--------------------------------------------------------------------
    error = validarInput(correoIn, -1, 100, /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/, true);
    let correoError = document.getElementById('correoError');
    correoError.innerText = "";
    if (error) {
        correoError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    //direccion
    //--------------------------------------------------------------------
    error = validarInput(direccionIn, 10, 100, null, true);
    let direccionError = document.getElementById('direccionError');
    direccionError.innerText = "";
    if (error) {
        direccionError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    //contacto
    //--------------------------------------------------------------------
    error = validarInput(contactoIn, 8, 15, null, true);
    let contactoError = document.getElementById('contactoError');
    contactoError.innerText = "";
    if (error) {
        contactoError.innerText = error;
        valid = false;
    }
    //--------------------------------------------------------------------

    return valid;
}

async function newClient(cliente) {
    const response = await fetch(`${urlBase}/api/cliente/nuevo`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cliente)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/clientes/paging?nombre=&apellidos=&cedula=&correo=&direccion=&contacto=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function updateClient(id, cliente) {
    const response = await fetch(`${urlBase}/api/cliente/update/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cliente)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/clientes/paging?nombre=&apellidos=&cedula=&correo=&direccion=&contacto=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function deleteClient(id) {
    const response = await fetch(`${urlBase}/api/cliente/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });

    const json = await response.json();
    console.log(json);
    if (json.success) {
        listClient();
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}
//-------------------------------------------------------------------------------------------------------

//Boletas
//-------------------------------------------------------------------------------------------------------
async function nuevoBoleta() {
    var clienteInput = document.getElementById('clienteInput');
    var servicioInput = document.getElementById('servicioInput');
    var FInicioInput = document.getElementById('FInicioInput');
    var FFinalInput = document.getElementById('FFinalInput');
    var tecnicoInput = document.getElementById('tecnicoInput');
    var canvas = document.getElementById('firmaD');

    let valid = validateFormularioBoletas(clienteInput, servicioInput, FInicioInput, FFinalInput, tecnicoInput);

    if (valid) {

        const firma = canvas.toDataURL("image/png").split(';base64,')[1];
        await newBoleta({
            idTecnico: tecnicoInput.value, idCliente: clienteInput.value,
            servicio: {
                id: servicioInput.value, fechaInicio: FInicioInput.value,
                fechaFinal: FFinalInput ? FFinalInput.value : null, productos: productos
            },
            firma: firma
        });
    }

}

async function actualizarBoleta(id) {
    var clienteInput = document.getElementById('clienteInput');
    var servicioInput = document.getElementById('servicioInput');
    var FInicioInput = document.getElementById('FInicioInput');
    var FFinalInput = document.getElementById('FFinalInput');
    var tecnicoInput = document.getElementById('tecnicoInput');


    let valid = validateFormularioBoletas(clienteInput, servicioInput, FInicioInput, FFinalInput, tecnicoInput);

    if (valid) {        
        await updateBoleta(id, {
            idTecnico: tecnicoInput.value, idCliente: clienteInput.value,
            servicio: { id: servicioInput.value, fechaInicio: FInicioInput.value, fechaFinal: FFinalInput ? FFinalInput.value : null, 
                productos:  productos.map(x => ({ id: x.id, nombre: x.nombre, precio: x.precio }))}
        });
    }
}

function validateFormularioBoletas(clienteInput, servicioInput, FInicioInput, FFinalInput, tecnicoInput) {
    let valid = true;

    let error = validarInput(clienteInput, -1, -1, null, true);
    let clienteError = document.getElementById('clienteError');
    if (error) {
        clienteError.innerText = error;
        valid = false;
    }

    error = validarInput(servicioInput, -1, -1, null, true);
    let ServicioError = document.getElementById('ServicioError');
    if (error) {
        ServicioError.innerText = error;
        valid = false;
    }

    error = validarInput(FInicioInput, -1, -1, null, true);
    let fInicioError = document.getElementById('fInicioError');
    if (error) {
        fInicioError.innerText = error;
        valid = false;
    }

    error = validarInput(tecnicoInput, -1, -1, null, true);
    let tecnicoError = document.getElementById('tecnicoError');
    if (error) {
        tecnicoError.innerText = error;
        valid = false;
    }

    return valid;

}

async function newBoleta(boleta) {
    const response = await fetch(`${urlBase}/api/boleta/nuevo`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(boleta)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/boletas/paging?id=&tecnico=&cliente=&descripcion=&productos=&finial=&ffinal=`;
    } else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function updateBoleta(id, boleta) {
    const response = await fetch(`${urlBase}/api/boleta/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(boleta)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/boletas/paging?id=&tecnico=&cliente=&descripcion=&productos=&finial=&ffinal=`;
    } else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

function redirectBoletas(id, op) {
    switch (op) {
        case "list":
            window.location = `${urlBase}/boletas/paging?id=&tecnico=&cliente=&descripcion=&productos=&finial=&ffinal`;
            break;
        case "detail":
            window.location = `${urlBase}/boleta/detalle/${id}`;
            break
        case "nuevo":
            window.location = `${urlBase}/boleta/nuevo`;
            break;
        case "edit":
            window.location = `${urlBase}/boleta/update/${id}`;
            break;
    }
}

async function deleteBoleta(id) {
    const response = await fetch(`${urlBase}/api/boleta/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });

    const json = await response.json();
    console.log(json);
    if (json.success) {
        listBoleta();
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}
//-------------------------------------------------------------------------------------------------------

//Usuario
//-------------------------------------------------------------------------------------------------------
function redirectUsuario(id, op) {
    switch (op) {
        case "list":
            window.location = `${urlBase}/usuarios/paging?nombre=&apellidos=&cedula=&correo=&user=&role=`;
            break;
        case "detail":
            window.location = `${urlBase}/usuario/detalle/${id}`;
            break
        case "nuevo":
            window.location = `${urlBase}/usuario/nuevo`;
            break;
        case "edit":
            window.location = `${urlBase}/usuario/update/${id}`;
            break;
        case "resetPass":
            resetPassword(id);
            break;
    }
}

async function nuevoUsuario() {
    var nombre = document.getElementById('nombreInput');
    var apellidos = document.getElementById('apellidosInput');
    var cedula = document.getElementById('cedulaInput');
    var correo = document.getElementById('correoInput');
    var user = document.getElementById('usuarioInput');
    var password1 = document.getElementById('passwordInput');
    var password2 = document.getElementById('password2Input');
    var role = document.getElementById('roleInput');

    let valid = validateFormularioUsuario(nombre, apellidos, cedula, correo, user, password1, password2, role);

    if (valid) {
        await newUsuario({
            nombre: nombre.value, apellidos: apellidos.value,
            cedula: cedula.value, correo: correo.value, user: user.value,
            password: password1.value, role: role.value
        });
    }
}

async function actualizarUsuario(id) {
    var nombre = document.getElementById('nombreInput');
    var apellidos = document.getElementById('apellidosInput');
    var cedula = document.getElementById('cedulaInput');
    var correo = document.getElementById('correoInput');
    var user = document.getElementById('usuarioInput');
    var password1 = document.getElementById('passwordInput');
    var password2 = document.getElementById('password2Input');
    var role = document.getElementById('roleInput');

    if (!password1.value) {
        let valid = validateFormularioUsuario(nombre, apellidos, cedula, correo, user, null, null, role);

        if (valid) {
            await updateUsuario(id, {
                nombre: nombre.value, apellidos: apellidos.value,
                cedula: cedula.value, correo: correo.value, user: user.value,
                password: null, role: role.value
            });
        }
    }
    else if (password1.value) {
        let valid = validateFormularioUsuario(nombre, apellidos, cedula, correo, user, password1, password2, role);

        if (valid) {
            await updateUsuario(id, {
                nombre: nombre.value, apellidos: apellidos.value,
                cedula: cedula.value, correo: correo.value, user: user.value,
                password: password1.value, role: role.value
            });
        }
    }

}

function validateFormularioUsuario(nombre, apellidos, cedula, correo, user, password1, password2, role) {
    let valid = true;

    let error = validarInput(nombre, 3, 100, null, true);
    let nombreError = document.getElementById('nombreError');
    if (error) {
        nombreError.innerText = error;
        valid = false;
    }

    error = validarInput(apellidos, 3, 200, null, true);
    let apellidosError = document.getElementById('apellidosError');
    if (error) {
        apellidosError.innerText = error;
        valid = false;
    }


    error = validarInput(cedula, 10, -1, /^[1-9]-\d{4}-\d{4}$/, true);
    let cedulaError = document.getElementById('cedulaError');
    if (error) {
        cedulaError.innerText = error;
        valid = false;
    }

    error = validarInput(correo, -1, 100, /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/, true);
    let correoError = document.getElementById('correoError');
    if (error) {
        correoError.innerText = error;
        valid = false;
    }

    error = validarInput(user, 3, 15, null, true);
    let userError = document.getElementById('usuarioError');
    userError.innerText = "";
    if (error) {
        userError.innerText = error;
        valid = false;
    }

    if (password1) {
        error = validarInput(password1, 8, -1, null, true, true);
        let password1Error = document.getElementById('passwordError');
        password1Error.innerText = "";
        if (error) {
            password1Error.innerText = error;
            valid = false;
        }

        error = validarInput(password2, 8, -1, null, true, true);
        let password2Error = document.getElementById('password2Error');
        password2Error.innerText = "";
        if (error) {
            password2Error.innerText = error;
            valid = false;
        }

        if (password1.value !== password2.value) {
            password2Error.innerText = error + (password2Error.innerText == "" ? "" : " / " + "Contraseña de confirmacion no coincide con la contraseña");
        }
    }


    error = validarInput(role, -1, -1, null, true);
    let roleError = document.getElementById('RoleError');
    roleError.innerText = "";
    if (error) {
        roleError.innerText = error;
        valid = false;
    }

    return valid;

}

async function newUsuario(usuario) {
    const response = await fetch(`${urlBase}/api/usuario/nuevo`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/usuarios/paging?nombre=&apellidos=&cedula=&correo=&user=&role=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function updateUsuario(id, usuario) {
    const response = await fetch(`${urlBase}/api/usuario/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/usuarios/paging?nombre=&apellidos=&cedula=&correo=&user=&role=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function deleteUsuario(id) {
    const response = await fetch(`${urlBase}/api/usuario/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/usuarios/paging?nombre=&apellidos=&cedula=&correo=&user=&role=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function resetPassword(id) {

    var newpass = { password: generarPasswordText() }

    const response = await fetch(`${urlBase}/api/usuario/resetPassword/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newpass)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        var dialogo = document.getElementById('dialogo2');
        var toask = document.getElementById('toastSuccess');
        dialogo.innerText = json.data;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}
//-------------------------------------------------------------------------------------------------------

//Servicios
//-------------------------------------------------------------------------------------------------------
function redirectServicios(id, op) {
    switch (op) {
        case "list":
            window.location = `${urlBase}/servicio/paging?descripcion=`;
            break;
        case "detail":
            window.location = `${urlBase}/servicio/detalle/${id}`;
            break
        case "nuevo":
            window.location = `${urlBase}/servicio/nuevo`;
            break;
        case "edit":
            window.location = `${urlBase}/servicio/update/${id}`;
            break;
    }
}

async function nuevoServicio() {
    var descripcion = document.getElementById('descripcionInput');

    let valid = validateFormularioServicio(descripcion);

    if (valid) {
        await newServicio({
            descripcion: descripcion.value
        });
    }
}

async function ActualizarServicio(id) {
    var descripcion = document.getElementById('descripcionInput');

    let valid = validateFormularioServicio(descripcion);

    if (valid) {
        await updateServicio(id, {
            descripcion: descripcion.value
        });
    }
}

function validateFormularioServicio(descripcion) {
    let valid = true;

    let error = validarInput(descripcion, 3, 100, null, true);
    let descripcionError = document.getElementById('descripcionError');
    if (error) {
        descripcionError.innerText = error;
        valid = false;
    }   

    return valid;

}

async function newServicio(servicio) {
    const response = await fetch(`${urlBase}/api/servicio/nuevo`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(servicio)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/servicio/paging?descripcion=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function updateServicio(id, servicio) {
    const response = await fetch(`${urlBase}/api/servicio/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(servicio)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/servicio/paging?descripcion=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function deleteServicio(id) {
    const response = await fetch(`${urlBase}/api/servicio/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/servicio/paging?descripcion=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}
//-------------------------------------------------------------------------------------------------------

//Productos
//-------------------------------------------------------------------------------------------------------
function redirectProductos(id, op) {
    switch (op) {
        case "list":
            window.location = `${urlBase}/producto/paging?nombre=&precio=`;
            break;
        case "detail":
            window.location = `${urlBase}/producto/detalle/${id}`;
            break
        case "nuevo":
            window.location = `${urlBase}/producto/nuevo`;
            break;
        case "edit":
            window.location = `${urlBase}/producto/update/${id}`;
            break;
    }
}

async function nuevoProducto() {
    var nombre = document.getElementById('nombreInput');
    var precio = document.getElementById('precioInput');

    let valid = validateFormularioProducto(nombre, precio);

    if (valid) {
        await newProducto({
            nombre: nombre.value,
            precio: precio.value
        });
    }
}

async function ActualizarProducto(id) {
    var nombre = document.getElementById('nombreInput');
    var precio = document.getElementById('precioInput');

    let valid = validateFormularioProducto(nombre, precio);

    if (valid) {
        await updateProducto(id, {
            nombre: nombre.value,
            precio: precio.value
        });
    }
}

function validateFormularioProducto(nombre, precio) {
    let valid = true;

    let error = validarInput(nombre, 3, 100, null, true);
    let nombrenError = document.getElementById('nombreError');
    if (error) {
        nombrenError.innerText = error;
        valid = false;
    }   

    error = validarInput(precio, 1, null, null, true);
    let precioError = document.getElementById('precioError');
    if (error) {
        precioError.innerText = error;
        valid = false;
    }   

    return valid;

}

async function newProducto(producto) {
    const response = await fetch(`${urlBase}/api/producto/nuevo`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(producto)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/producto/paging?nombre=&precio=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function updateProducto(id, producto) {
    const response = await fetch(`${urlBase}/api/producto/${id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(producto)
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/producto/paging?nombre=&precio=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}

async function deleteProducto(id) {
    const response = await fetch(`${urlBase}/api/producto/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const json = await response.json();
    console.log(json);
    if (json.success) {
        window.location = `${urlBase}/producto/paging?nombre=&precio=`;
    }
    else if (!json.success) {
        var dialogo = document.getElementById('dialogo');
        var toask = document.getElementById('toastError');
        dialogo.innerText = json.errorMessage;
        var t = bootstrap.Toast.getOrCreateInstance(toask)
        t.show();
    }
}
//-------------------------------------------------------------------------------------------------------

//Reporte
//-------------------------------------------------------------------------------------------------------
function generateReport(tipo) {
    if (tipo == "serviciosR") {
        var FInicioInput = document.getElementById('FInicioInput1');
        var FFinalInput = document.getElementById('FFinalInput1');

        if (validateReporteForm(FInicioInput, FFinalInput, 1))
            window.open(`${urlBase}/reporte/ServiciosRealizados?finial=${FInicioInput.value}&ffinal=${FFinalInput.value}`, '_blank').focus();
    }
    else if (tipo == "usuariosR") {
        var FInicioInput = document.getElementById('FInicioInput2');
        var FFinalInput = document.getElementById('FFinalInput2');

        if (validateReporteForm(FInicioInput, FFinalInput, 2))
            window.open(`${urlBase}/reporte/UsuarioRecurrentes?finial=${FInicioInput.value}&ffinal=${FFinalInput.value}`, '_blank').focus();
    }
    else if (tipo == "productosU") {
        var FInicioInput = document.getElementById('FInicioInput3');
        var FFinalInput = document.getElementById('FFinalInput3');

        if (validateReporteForm(FInicioInput, FFinalInput, 3))
            window.open(`${urlBase}/reporte/ProductosUtilizados?finial=${FInicioInput.value}&ffinal=${FFinalInput.value}`, '_blank').focus();
    }
    else if (tipo == "rendimientoT") {
        var FInicioInput = document.getElementById('FInicioInput4');
        var FFinalInput = document.getElementById('FFinalInput4');

        if (validateReporteForm(FInicioInput, FFinalInput, 4))
            window.open(`${urlBase}/reporte/RendimientoTecnicos?finial=${FInicioInput.value}&ffinal=${FFinalInput.value}`, '_blank').focus();
    }
    else if (tipo == "tiempoR") {
        var FInicioInput = document.getElementById('FInicioInput5');
        var FFinalInput = document.getElementById('FFinalInput5');

        if (validateReporteForm(FInicioInput, FFinalInput, 5))
            window.open(`${urlBase}/reporte/TiempoRespuesta?finial=${FInicioInput.value}&ffinal=${FFinalInput.value}`, '_blank').focus();
    }
}

function validateReporteForm(FInicioInput, FFinalInput, indice) {
    let valid = true;
    console.log(FInicioInput);
    console.log(FFinalInput);
    let error = validarInput(FInicioInput, -1, -1, null, true);
    let fInicioError = document.getElementById(`fInicioError${indice}`);
    if (error) {
        fInicioError.innerText = error;
        valid = false;
    }

    error = validarInput(FFinalInput, -1, -1, null, true);
    let fFinalError = document.getElementById(`fFinalError${indice}`);
    if (error) {
        fFinalError.innerText = error;
        valid = false;
    }

    return valid;
}

function _base64ToArrayBuffer(base64) {
    var binary_string =  window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array( len );
    for (var i = 0; i < len; i++)        {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes;
}

function descargarArchivo(nombre, tipo){

    var data = "";
    if(tipo == 'txt')
        data = document.getElementById("txtData").value;
    if(tipo == 'csv')
        data = document.getElementById("csvData").value;

    var arrBuffer = _base64ToArrayBuffer(data );

    var newBlob = new Blob([arrBuffer], { type: "text/plain" });

    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(newBlob);
        return;
    }

    var data = window.URL.createObjectURL(newBlob);

    var link = document.createElement('a');
    document.body.appendChild(link); 
    link.href = data;
    link.download = nombre;
    link.click();
    window.URL.revokeObjectURL(data);
    link.remove();
}