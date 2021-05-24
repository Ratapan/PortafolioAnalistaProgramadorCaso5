<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\cita;
use Illuminate\Http\Request;
use App\Models\hora;
use App\Models\empleado;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;


class HorasController extends Controller
{
    public function register(Request $request){
        $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $newHora = new hora();
        $newHora->inicio_hora             = $request->inicio;
        $newHora->fin_hora                = $request->fin;
        $newHora->estado_hora                = 'D';
        $newHora->empleados_id_empleado   = $id_emp;
        $newHora->save();
        return response()->json([$newHora], 200);

    }
    public function edit(Request $request){
        $horaEdit = hora::find($request->id);
        $horaEdit->inicio_hora = $request->inicio;
        $horaEdit->fin_hora = $request->fin;
        $horaEdit->save();
        return response()->json([$horaEdit], 200);
    }

    public function delete(Request $request){
        if ($cita = cita::where('horas_id_hora', $request->id)){
            $cita->delete();
            $hora = hora::find($request->id);
            $hora->delete();
            return response()->json([$cita, $hora], 300);
        }
        else{
            $hora = hora::find($request->id);
            $hora->delete();
            return response()->json([$hora], 200);
        }
    }

    public function getAll(Request $request)
    {
        $servicios = hora::orderBy('inicio_hora','desc')
                    ->paginate(15);
        return response()->json($servicios,200);
    }
    //public function getHourCitas(Request $request)
    //{
    //    $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
    //    $servicios = hora::orderBy('inicio_hora','asc')
    //                ->where('estado', 'T')
    //                ->join('empleados', 'empleados.id_empleado', '=', 'horas.empleados_id_empleado')
    //                ->join('citas', 'citas.horas_id_hora', '=', 'horas.id_hora')
    //                ->where('id_empleado' , $id_emp)
    //                ->paginate(15);
    //    return response()->json($servicios,200);
    //}

    public function getDent(Request $request){
        $servicios = hora::orderBy('inicio_hora','desc')
                    ->where('empleados_id_empleado', $request->id_emp)
                    ->where('inicio_hora','>=',$request->fini) 
                    ->where('inicio_hora','<=',$request->ffin) 
                    ->where('estado_hora', 'D')
                    ->paginate(15);
        return response()->json($servicios,200);
    }

}
