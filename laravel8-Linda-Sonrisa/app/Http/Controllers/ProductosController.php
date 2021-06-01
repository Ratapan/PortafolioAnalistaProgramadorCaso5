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
        $familia = producto::orderBy('nombre_tipop','desc')
                    ->paginate(30);
        return response()->json($familia,200);
    }
}
