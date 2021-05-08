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
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $newCita = new cita();
        $newCita->estado = 'D';
        $newCita->fecha_solicitacion = $request->fecha;
        $newCita->id_paciente = $id_paciente;
        $newCita->id_hora = $request->id_hora;
        $newCita->save();
        return response()->json([$newCita], 200);
    }
}
