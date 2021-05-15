<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\empleado;
use Illuminate\Http\Request;

class EmployeesController extends Controller
{
    //
    public function getAll(Request $request)
    {

        $empleados = empleado::where('TIPO_EMPLEADOS_ID_TIPO_EMP','1')
                            ->with(['user'])
                            ->paginate(15);

        return response()->json($empleados,200);
    }
}
