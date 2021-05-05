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

        $empleados = empleado::where('TIPO_CUENTA','den')
                            ->orderBy('NOMBRE_E','asc')
                            ->paginate(15);

        return response()->json($empleados,200);
    }
}