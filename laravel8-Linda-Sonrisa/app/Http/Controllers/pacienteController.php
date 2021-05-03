<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Models\paciente;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class pacienteController extends Controller
{
public function register(Request $request)
{
        if(User::where('email', $request->email)->first()){
                return response()->json("El email ya existe", 500);
        }
        else{
                $newUser = new User;
                $newUser->email = $request->email;
                $newUser->password = Hash::make($request->password, ['rounds' => 12,]);
                $newUser->rut = $request->rut;
                $newUser->nombre_ape = $request->nombre;
                $newUser->direccion = $request->direccion;
                $newUser->fecha_nac = $request->fecha;
                $newUser->eliminado = 1;
                $newUser->rol_id = 1;
                $newUser->save();
                $id_user = $newUser->id;

                $newPaciente = new paciente();
                $newPaciente->num_telefono_pa = $request->telefono;
                $newPaciente->salud_pa = $request->salud;
                $newPaciente->documento = $request->documento;
                $newPaciente->id_user = $id_user;
                $newPaciente->save();
                
                return response()->json([$newUser,$newPaciente], 200);
        }
}
}
