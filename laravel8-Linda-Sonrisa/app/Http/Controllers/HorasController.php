<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\hora;
use App\Models\empleado;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;


class HorasController extends Controller
{
    public function register(Request $request){
        //$id_emp = DB::table('empleados')->select('id_empleado')->where('users_id_user','=',Auth::id())->value('id_empleado');
        $id_emp = empleado::where('USERS_ID_USER', $request->id_user)->first();
        $newHora = new hora();
        $newHora->inicio_hora             = $request->inicio;
        $newHora->fin_hora                = $request->fin;
        $newHora->empleados_id_empleado   = $id_emp->id_empleado;
        //$newHora->empleados_id_empleado = $id_emp;
        $newHora->save();
        return response()->json([$newHora], 200);

    }
    public function getAll(Request $request)
    {
        $servicios = hora::orderBy('inicio_hora','asc')
                    ->paginate(15);
        return response()->json($servicios,200);
    }

}