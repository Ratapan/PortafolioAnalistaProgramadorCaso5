<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\producto;
use Illuminate\Support\Facades\DB;

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
    public function getproductosAceptados(Request $request)
    {
        $Ordenes = DB::table('ordenes')
                    ->join('detalle_ordenes', 'detalle_ordenes.ordenes_id_orden' , '=' , 'ordenes.id_orden')
                    ->join('productos', 'productos.id_tipop' , '=' , 'detalle_ordenes.productos_id_tipop')
                    ->join('familia_productos', 'familia_productos.id_t_fam' , '=' , 'productos.familia_productos_id_t_fam')
                    ->where('ordenes.estado' , 'A')
                    ->paginate(30);
        return response()->json($Ordenes,200);
    }
}
