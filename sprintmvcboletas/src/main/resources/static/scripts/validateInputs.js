function validarInput(input, min, max, pattern, required, ispassword){
    let message = "Inválido: \n";
    let invalid = false;
    
    if(isEmpty(input) && required == true){
        message += "*Este campo es obligatorio\n";
        invalid = true;
    }

    if(min > 0 && minimin(input, min)){
        message += "*Tamaño mínimo es de " + min + " caracteres\n";
        invalid = true;
    }

    if(max > 0 && maxlegth(input, max)){
        message += "*Tamaño máximo es de " + max + " caracteres\n";
        invalid = true;
    }

    if(pattern && checkPattern(input, pattern)){
        message += "*No tiene el formato correcto\n";
        invalid = true;
    }

    if(ispassword && ispassword == true && passwordInValid(input)){
        message += "*La contraseña no tiene requerimientos mínimos\n";
        invalid = true;
    }

    if(invalid == true){
        return message;
    }
    
    return null;
}

function isEmpty(input){
    if(!input)
        return true;
    if(input.value.length == 0)
        return true;

    return false;
}   

function minimin(input, min){
    if(input.value.length < min)
        return true;
    return false;
}

function maxlegth(input, max){    
    if(input.value.length > max)
        return true;
    return false;
}

function checkPattern(input, pattern){
    var re = pattern;
    return !re.test(input.value);
}

function passwordInValid(input){

    const mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
    const minisculas = "abcdefghijklmnopqrstuvwxyz".split("");
    const numeros = "1234567890".split("");
    const simbolos = "#!&*".split("");
    const value = input.value;
    
    if(!mayusculas.some((l) => value.includes(l, 0))){
        return true;
    }

    if(!minisculas.some((l) => value.includes(l, 0))){
        return true;
    }

    if(!numeros.some((l) => value.includes(l, 0))){
        return true;
    }

    if(!simbolos.some((l) => value.includes(l, 0))){
        return true;
    }

    return false;
}