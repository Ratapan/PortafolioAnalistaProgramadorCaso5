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
        $newUser = new User;
        $newUser->email    = $request->email;
        $newUser->password = Hash::make($request->password, ['rounds' => 12,]);
        $newUser->eliminado = 1;
        $newUser->rol_id = 1;
        $newUser->save();

        return response()->json([$newUser], 200);
}
}
