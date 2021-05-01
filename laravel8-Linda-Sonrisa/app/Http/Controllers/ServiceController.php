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

    public function register(Request $request){
        $newTipoServicio = new tipo_servicio();
        $newTipoServicio->nombre_servicio = $request->nombre;
        $newTipoServicio->precio = $request->precio;
        $newTipoServicio->descripcion_servicio = $request->descripcion;
        $newTipoServicio->save();

        return response()->json([$newTipoServicio], 200);

    }
}
