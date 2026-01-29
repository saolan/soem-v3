'use strict';

/* Aqui se puede declarar las variales globales */
const globalScope = {
    sucursal: '001',
    usuario: 'dessA',
    caja: '001'
};

console.log("globalScope.sucursal: " + globalScope.sucursal);
let cajaMovis = [1,2,3];

function consola (mensaje) {
    console.log("mesaje: " + mensaje);
}

function cambiarMensaje(mensaje) {
    document.getElementById('mensaje').textContent = mensaje;
}

let simbolo = Symbol();

let docuCajaAbrir = {
    descri: 'Abrir Sesion',
    numero: '0',
    simbolo: 'Información Secreta',
    informacion: function(){
        cambiarMensaje(this.descri + ' Número ' + this.numero);
    }
}

let docuCajaEgreso = {
    descri: 'Egreso Caja',
    numero: '0'
}

let docuCajaCierre = {
    descri: 'Cerrar Sesion',
    numero: '0'
}

/* Estas dos lineas son equivalentes */
docuCajaAbrir.numero = 11;
docuCajaAbrir['numero'] = 11;

docuCajaAbrir.informacion();

function cambiarMensajeH2 (mensaje) {
    document.getElementById('cajaListH2').textContent = mensaje;
}

cambiarMensajeH2('Cambiando mensaje H2');

const boton = document.getElementById('idBuscar');
boton.addEventListener('click', function () {
    console.log('Hizo click');
});

const linkOcultar = document.getElementById('idLinkOcultar');
linkOcultar.addEventListener('click', function () {
    const div1 = document.getElementById('idDiv');
    div1.style.display = "none";

    /* 
    if (div1.classList.contains("nombre de la clase")) {
        div1.classList.remove("nombre de la clase a remover");
    } else {
        div1.classList.add("nombre de la clase a remover");
    }; 
    */ 
});

const linkMostrar = document.getElementById('idLinkMostrar');
linkMostrar.addEventListener('click', function () {
    const div1 = document.getElementById('idDiv');
    div1.style.display = "block";
});

const values = ['aa','bb','c'];

/* Nuestra solo el primero que encuentra */
const busqueda = values.find(function(valor) {
    return valor.length > 1;
}
);

const filtro = values.filter(function(valor) {
    return valor > 'b';
}
);

values.forEach(function(valor) {
    console.log('forEach: ' + valor);
});


recorrerConFor();
function recorrerConFor(){
    for (const valor of values) {
        console.log("Recorriendo: " + valor);
    }
};

function errorPersonalizado() {
    let resultado;

    try {
        resultado = valor / 10;
    } catch (error) {
        console.log(error.message);

        throw {
            "message" : "Mensake error",
            "name" : "Nombre Error"
        }
    } finally {
        console.log("Entro en Finally");
    }
};

function manejoError(){
    try {
        errorPersonalizado();
    } catch (error) {
        console.log(error.message);
        console.log(error.name);
    }
}

console.log("Busqueda: " + busqueda);
console.log("Filtro: " + filtro);

function imprimirTodo() {
    for (let index = 0; index < arguments.length; index++) {
        console.log(arguments[index]);
    }
}

imprimirTodo(1,2,3,4);
imprimirTodo("a",9);

(function() {
    console.log("Funcion de ejecucion inmediata sin ser llamada");
}
)();

function ejemploClosure(valor){
    return function contador(){
        return valor++;
    }
}

let contador1 = ejemploClosure(0);
console.log(contador1());
console.log(contador1());

let contador2 = ejemploClosure(10);
console.log(contador2());
console.log(contador2());
console.log(contador2());


let funcionFlecha = () => "Funcion Flecha"
let mensajeFlecha = funcionFlecha();
console.log("Funcion Flecha 1: " + mensajeFlecha);

let funcionFlecha2 = mensajeFlecha2 => "Funcion Flecha2" + mensajeFlecha2;
let mensajeFlecha2 = funcionFlecha2(" inicio mensaje ");
console.log("Funcion Flecha 2: " + mensajeFlecha2);

function sumar(num1, num2) {
    return num1 + num2;
}
let resultado = sumar(1,2);
console.log("Resultado sumar funcion tradicional: " + resultado);

let sumar2 = (num1, num2) => num1 + num2;
let resultado1 = sumar2(1,2);
console.log("Resultado sumar funcion flecha: " + resultado1);

let mensaje = {
    nombre: "Sandro",
    regularFunction: function() {
        console.log(this);
        console.log("Hello " + this.nombre);
    },
    arrowFunction: () => console.log("Hi " + this.name),
}

mensaje.regularFunction();
 /* En Este tipo de funcion no se puede utilizar this*/
mensaje.arrowFunction();

let saludo = {};
saludo.sayHi = function () {
    console.log("this es este objeto");
    console.log(this);
};
saludo.sayHi();

function sayHi2() {
    console.log("this es un objeto nuevo");
    console.log(this);
} 
let saludo2 = new sayHi2();

/* call y apply */
let person1 = {name:"Nombre1", age:21};
let person2 = {name:"Nombre2", age:22};
let sayHi3 = function() {
    console.log("Llamada con call " + this.name);
};
sayHi3.call(person1);
sayHi3.call(person2);

function introduction(name, profesion) {
    console.log('Mi nombre es ' + name + ' y soy ' + profesion + '.');
    console.log(this);
};
introduction('Nombre','Profesion');
introduction.apply(undefined,['apply Arreglo nombre','apply Arreglo profesion']);
introduction.call(undefined,'call nombre', 'call Profesion');
/*fin  call y apply */

/* bind

Copia de la funcion getName de personBind1*/

let personBind1 = {
    name: "namePersonBond1",
    getName: function() {
        return this.name;
    }
};

let personBind2 = {
    name: "namePersonBind2"
};

let getNameBind = personBind1.getName.bind(personBind2);
console.log("Copia de la funcion de persoBind1 en person Bind2 " + getNameBind())
/* Nombre de la funcion anonima*/
console.log("Nombre Bind " + getNameBind)

/* fin bind */

/* Default Parameter */
function defaultParameter(name = "Alice") {
    console.log("Saludos " + name);
}

defaultParameter();
defaultParameter("Joss");

/* Los parametro por defecto siempre tienen que ir luego de los parametros normales
sino da undefinide */
function defaultParameter2(mensaje, name = "Alice") {
    console.log(mensaje + " " + name);
}

defaultParameter2("Hi ");
defaultParameter2("Hola", "Joss");
/* Fin Default Parameter */

/* Rest Parameter */
/* Los rest parametro siempre tienen que ir luego de los parametros normales
sino da undefinide */

function restParameter(...nombres) {
    nombres.forEach(nomb => console.log(nomb))
}

restParameter("aa","bb","cc");
restParameter("Joss","boss");
/* Fin Rest Parameter */

/* spread operator */
function spreadOperator(person1, person2) {
    console.log(person1);
    console.log(person2);
    
}
let names = ["fff","ggg"];
spreadOperator(...names);


/* fin spread operator */