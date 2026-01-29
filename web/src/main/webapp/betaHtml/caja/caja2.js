// JavaScript 2018 Variables and Types

// Template Literal
var cedula = 1234567890;
var nombre = 'abc';
var sueldo = 1690.00;

var texto = `Cédula: ${cedula},
    nombre: ${nombre},
    sueldo: ${"$" + sueldo}`;

console.log(sueldo);