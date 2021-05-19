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


//citas
Route::post('/cita',       'App\Http\Controllers\CitasController@register');
Route::post('/cita/cancel', 'App\Http\Controllers\CitasController@cancel');
Route::get('/citas',       'App\Http\Controllers\CitasController@getCitasActivas');
Route::get('/cita/hora',   'App\Http\Controllers\CitasController@getHourCitas');
