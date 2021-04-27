<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

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
}
