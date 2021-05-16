<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\cita;
use App\Models\hora;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class CitasController extends Controller
{
    public function register(Request $request){
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $fecha = Carbon::now();
        $fecha = $fecha->format('Y-m-d');
        $newCita = new cita();
        $newCita->estado = 'R';
        $newCita->pacientes_id_paciente = $id_paciente;
        $newCita->fecha_solicitacion = $fecha;
        $newCita->horas_id_hora = $request->id_hora;
        $newCita->save();

        $horaEdit = hora::find($request->id_hora);
        $horaEdit->estado = 'T';
        $horaEdit->save();
        return response()->json([$newCita,$horaEdit], 200);
    }
    public function getUs(Request $request){
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $servicios   = cita::orderBy('fecha_solicitacion','asc')
                    ->where('pacientes_id_paciente', $id_paciente)
                    ->paginate(15);
        return response()->json($servicios,200);
    }
    public function getCitasActivas(Request $request){
        $fecha = Carbon::now();
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $citas   = cita::orderBy('fecha_solicitacion','asc')
                    ->where('pacientes_id_paciente', $id_paciente)
                    ->where('fecha_solicitacion','>=',$request->fecha)
                    ->paginate(15);
        return response()->json($citas,200);
    }
    
}
