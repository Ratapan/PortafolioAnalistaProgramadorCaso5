<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Models\paciente;
use Illuminate\Http\Request;

class pacienteController extends Controller
{
    public function register(Request $request){
        $newUser = new User;
        $newUser->email = $request->email;
        $newUser->password = $request->password;
        $newUser->eliminado = 1;
        $newUser->rol_id = 2;
        $newUser->save();

        $newPaciente = new paciente;
        $newPaciente->rut_pa = $request->rut;
        $newPaciente->nombre_pa = $request->nombre;
        $newPaciente->correo_pa = $request->email;
        $newPaciente->save();
    }
}
