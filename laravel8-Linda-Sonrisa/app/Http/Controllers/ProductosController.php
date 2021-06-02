<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\producto;


class ProductosController extends Controller
{
    //
    public function getAll(Request $request)
    {
        $familia = producto::orderBy('nombre_tipop','asc')
                    ->paginate(30);
        return response()->json($familia,200);
    }
    public function getProd(Request $request)
    {
        $familia = producto::orderBy('nombre_tipop','desc')
                    ->where('FAMILIA_PRODUCTOS_ID_T_FAM', $request->id_familia)
                    ->paginate(30);
        return response()->json($familia,200);
    }
}
