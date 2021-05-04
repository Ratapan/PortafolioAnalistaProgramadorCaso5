<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\hora;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;


class HorasController extends Controller
{
    public function register(Request $request){
        $id_emp = DB::table('empleados')->select('id_empleado')->where('users_id_user','=',Auth::id())->value('id_empleado');
        $newHora = new hora();
        $newHora->inicio_hora = $request->inicio;
        $newHora->fin_hora = $request->fin;
        $newHora->empleados_id_empleado = $id_emp;
        $newHora->save();

        return response()->json([$newHora], 200);

    }
}
