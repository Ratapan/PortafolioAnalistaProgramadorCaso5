<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Models\paciente;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class pacienteController extends Controller
{
    public function register(Request $request){
        $newUser = new User;
        $newUser->email = $request->names;
        $newUser->password = '24';
        $newUser->eliminado = 1;
        $newUser->rol_id = 2;
        $newUser->save();

        $id_user = $newUser->id;

        $newPaciente = new paciente;
        $newPaciente = $id_user;
        $newPaciente->save();
        return response()->json([$newUser,$newPaciente],200);

    }
}
