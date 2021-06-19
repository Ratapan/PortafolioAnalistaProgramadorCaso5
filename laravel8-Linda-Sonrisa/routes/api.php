<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});


//Login
Route::post('/login', 'App\Http\Controllers\LoginController@login');
Route::post('/session', 'App\Http\Controllers\LoginController@getIndex');

//usuarios
Route::post('/usuarios', 'App\Http\Controllers\pacienteController@register');

//empleados
Route::get('/employee', 'App\Http\Controllers\EmployeesController@getAll');

//horas
Route::post( '/hora',  'App\Http\Controllers\HorasController@register');
Route::post( '/horad', 'App\Http\Controllers\HorasController@delete');
Route::get(  '/hora',  'App\Http\Controllers\HorasController@getAll');
Route::get(  '/hora-d','App\Http\Controllers\HorasController@getDent');

//servicios
Route::get('/servicio', 'App\Http\Controllers\ServiceController@getAll');
Route::get('/servicioEmpleado', 'App\Http\Controllers\ServiceController@getServiciosEmpleados');

//citas
Route::post('/cita',       'App\Http\Controllers\CitasController@register');
Route::put('/cita/end',    'App\Http\Controllers\CitasController@terminar');
Route::get('/citas',       'App\Http\Controllers\CitasController@getCitasActivas');
Route::get('/citas/ant',   'App\Http\Controllers\CitasController@getCitasAntiguas');
Route::get('/cita/hora',   'App\Http\Controllers\CitasController@getHourCitas');

Route::post('/cita/cancel', 'App\Http\Controllers\CitasController@cancel');
Route::post('/cita/atrasar', 'App\Http\Controllers\CitasController@atrasar');

//familia y productos
Route::get('/familia',   'App\Http\Controllers\FamiliaProductosController@getAll');
Route::get('/productos',   'App\Http\Controllers\ProductosController@getAll');
Route::get('/producto',   'App\Http\Controllers\ProductosController@getProd');

//boletas
Route::get('/boletas', 'App\Http\Controllers\BoletasController@getBoletasCliente');

//proveedores
Route::get('/proveedores','App\Http\Controllers\ProveedorController@getAll');

//ordenes
Route::post('/ordenes','App\Http\Controllers\OrdenesController@register');
Route::get('/ordenes','App\Http\Controllers\ProveedorController@getAllOrdenes');
Route::post('/ordenes/aceptar','App\Http\Controllers\ProveedorController@aceptarOrden');
Route::post('/ordenes/rechazar','App\Http\Controllers\ProveedorController@RechazarOrden');

//recepcion
Route::get('/productosRecibidos','App\Http\Controllers\RecepcionController@getProductosRecibidos');
Route::get('/recepciones','App\Http\Controllers\ProductosController@getproductosAceptados');
Route::post('/recepciones/aceptar','App\Http\Controllers\RecepcionController@aceptarOrden');