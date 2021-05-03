<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\cita;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class CitasController extends Controller
{
    public function register(Request $request){
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('id_user','=',Auth::id())->value('id_paciente');
        $newCita = new cita();
        $newCita->estado = '1';
        $newCita->fecha_solicitacion = $request->fecha;
        $newCita->id_paciente = $id_paciente;
        $newCita->id_hora = $request->id_hora;
        $newCita->save();
        return response()->json([$newCita], 200);
    }
}
