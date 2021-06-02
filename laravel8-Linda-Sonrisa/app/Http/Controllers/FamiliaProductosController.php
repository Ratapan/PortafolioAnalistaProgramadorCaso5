<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\familia_producto;

class FamiliaProductosController extends Controller
{
    //
    public function getAll(Request $request)
    {
        $familia = familia_producto::orderBy('nombre_tipo_familia','desc')
                    ->paginate(30);
        return response()->json($familia,200);
    }
}