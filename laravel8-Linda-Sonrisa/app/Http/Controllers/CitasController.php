<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\cita;
use App\Models\hora;
use App\Models\servicio;
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

        //$idcita = $newCita->id_cita;

        $horaEdit = hora::find($request->id_hora);
        $horaEdit->estado_hora = 'T';
        $horaEdit->save();

        /*Para la boleta
        $newServicio = new servicio();
        $newServicio->tipo_servicios_id_t_serv = 'T';
        $newServicio->citas_id_cita = $idcita;
        $newServicio->save();*/

        return response()->json([$newCita,$horaEdit], 200);
    }
    public function cancel(Request $request){
        $citaEdit = cita::where('id_cita', $request->id_cita)->first();
            $citaEdit->estado = 'C';
            $citaEdit->save();
            $horaEdit = hora::find($request->id_hora);
            $horaEdit->estado_hora = 'D';
            $horaEdit->save();
            return response()->json([$citaEdit, $horaEdit], 300);
    }
    public function terminar(Request $request){
        
        $citaEdit = cita::find($request->id_cita);
        $citaEdit->estado = 'T';
        $citaEdit->save();
        return response()->json("Cita terminada", 200);
    }
    public function atrasar(Request $request){
        
        $citaEdit = cita::find($request->id_cita);
        $citaEdit->estado = 'A';
        $citaEdit->save();
        return response()->json("Cita terminada", 200);
    }

    public function getUs(Request $request){
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $servicios   = cita::orderBy('fecha_solicitacion','desc')
                    ->where('pacientes_id_paciente', $id_paciente)
                    ->paginate(15);
        return response()->json($servicios,200);
    }

    public function getCitasActivas(Request $request){
        $fecha = Carbon::now();
        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $citas   = cita::orderBy('fecha_solicitacion','desc')
                    ->join('horas', 'horas.id_hora' , '=' , 'citas.horas_id_hora' )
                    ->join('empleados', 'empleados.id_empleado' , '=', 'horas.empleados_id_empleado')
                    ->join('users', 'users.id_user' , '=', 'empleados.users_id_user')
                    //users_id_user
                    ->where('pacientes_id_paciente', $id_paciente)
                    ->where('fecha_solicitacion','>=',$request->fecha)
                    ->where('estado', 'R')
                    ->paginate(15);
        return response()->json($citas,200);
    }
    
    public function getCitasAntiguas(Request $request){
        $fecha = Carbon::now();

        $id_paciente = DB::table('pacientes')->select('id_paciente')->where('USERS_ID_USER', $request->id_user)->value('id_paciente');
        $citas   = cita::orderBy('fecha_solicitacion','desc')
                    ->join('horas', 'horas.id_hora' , '=' , 'citas.horas_id_hora' )
                    ->join('empleados', 'empleados.id_empleado' , '=', 'horas.empleados_id_empleado')
                    ->join('users', 'users.id_user' , '=', 'empleados.users_id_user')
                    //users_id_user
                    ->where('pacientes_id_paciente', $id_paciente)
                    //->where('fecha_solicitacion','<=',$request->fecha)
                    ->whereIn('estado', ['C', 'T', 'A'])
                    ->paginate(15);
        return response()->json($citas,200);
    }

    public function edit(Request $request){
        $citaEdit = cita::find($request->id);
        $citaEdit->horas_id_hora = $request->id_hora;
        $citaEdit->save();
        return response()->json([$citaEdit], 200);
    }

    public function delete(Request $request){
        $cita = cita::where('horas_id_hora', $request->id);
        $cita->delete();
        return response()->json([$cita], 300);
    }

    public function getHourCitas(Request $request)
    {
        $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $servicios = cita::orderBy('inicio_hora','desc')
                    

                    ->join('horas', 'horas.id_hora' , '=' , 'citas.horas_id_hora' )
                    ->join('empleados', 'empleados.id_empleado', '=', 'horas.empleados_id_empleado')
                    ->join('pacientes', 'pacientes.id_paciente', '=', 'citas.pacientes_id_paciente')
                    ->join('users', 'users.id_user', '=', 'pacientes.users_id_user')
                    
                    ->where('id_empleado' , $id_emp)
                    ->paginate(15);
        return response()->json($servicios,200);
    }
}
