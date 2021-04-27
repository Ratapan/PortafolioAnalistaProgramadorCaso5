<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Models\paciente;
use Illuminate\Http\Request;

class pacienteController extends Controller
{
        public function register(Request $request)
        {
                $newUser = new User;
                $newUser->email = $request->name;
                $newUser->password = '345345';
                $newUser->eliminado = 1;
                $newUser->rol_id = 1;
                $newUser->save();

                return response()->json([$newUser], 200);
        }
}
