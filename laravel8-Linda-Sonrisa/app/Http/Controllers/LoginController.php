<?php

namespace App\Http\Controllers;

use App\Models\User;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;


class LoginController extends Controller
{
    //
    public function login(Request $request)
    {
        $credentials = $request->only('email', 'password');
        if (Auth::attempt($credentials)) {
            $user = User::where('email', $request->email)->first();
            return response()->json($user, 200);
        } else {
            return response()->json("El usuario y/o la contraseña son incorrectos", 500);
        }
    }
    public function getIndex(request $request)
    {
        session(['url' => $request->url]);
        $url = session('url');
        return $request->session()->all();
    }
}
