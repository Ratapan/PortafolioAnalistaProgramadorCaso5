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
        $newUser->email = 'qwe';
        $newUser->password = $request->name;
        $newUser->eliminado = 1;
        $newUser->rol_id = 2;
        $newUser->save();
        return response()->json([$newUser],200);

    }
}
