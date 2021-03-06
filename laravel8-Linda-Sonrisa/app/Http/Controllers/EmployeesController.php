<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\empleado;
use App\Models\User;
use Illuminate\Http\Request;

class EmployeesController extends Controller
{
    //
    public function getAll(Request $request)
    {

        $empleados = empleado::where('TIPO_EMPLEADOS_ID_TIPO_EMP','1')
                            ->join('users', 'users.id_user', '=', 'empleados.users_id_user')
                            ->paginate(15);
        return response()->json($empleados,200);
        //return response()->json(mb_convert_encoding($empleados, "UTF-8"),200);
    }
}
