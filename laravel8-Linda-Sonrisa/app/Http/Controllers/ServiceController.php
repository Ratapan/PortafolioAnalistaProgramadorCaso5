<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

use App\Models\tipo_servicio;

class ServiceController extends Controller
{
    //
    public function getAll(Request $request)
    {

        $servicios = tipo_servicio::orderBy('nombre_servicio','asc')
                            ->paginate(15);

        return response()->json($servicios,200);
    }

    public function getServiciosEmpleados(Request $request)
    {
        $id_empleado = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $servicios = tipo_servicio::orderBy('nombre_servicio','asc')
                            ->join('emp_tserv', 'emp_tserv.tipo_servicios_id_t_serv' , '=' , 'tipo_servicios.id_t_serv' )
                            ->where('emp_tserv.empleados_id_empleado', $id_empleado)
                            ->paginate(15);

        return response()->json($servicios,200);
    }

    public function register(Request $request){
        $newTipoServicio = new tipo_servicio();
        $newTipoServicio->nombre_servicio = $request->nombre;
        $newTipoServicio->precio = $request->precio;
        $newTipoServicio->descripcion_servicio = $request->descripcion;
        $newTipoServicio->save();

        return response()->json([$newTipoServicio], 200);

    }
}
