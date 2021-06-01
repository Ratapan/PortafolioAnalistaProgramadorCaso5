<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\boleta;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class BoletasController extends Controller
{
    public function getBoletasCliente(Request $request)
    {
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $servicios = DB::table('boletas')
                    ->join('servicios', 'servicios.id_servicio' , '=' , 'boletas.servicios_id_servicio' )
                    ->join('tipo_servicios', 'tipo_servicios.id_t_serv', '=', 'servicios.tipo_servicios_id_t_serv')
                    ->join('citas', 'citas.id_cita', '=', 'servicios.citas_id_cita')
                    ->join('horas', 'horas.id_hora' , '=' , 'citas.horas_id_hora' )
                    ->where('citas.pacientes_id_paciente' , $id_paciente)
                    ->paginate(15);
        return response()->json($servicios,200);
    }
}
