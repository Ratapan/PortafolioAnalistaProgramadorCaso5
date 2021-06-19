<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\ordene;
use App\Models\proveedor;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class ProveedorController extends Controller
{
    public function getAll(Request $request)
    {
        $proveedor = DB::table('proveedores')
                    ->join('users', 'users.id_user' , '=' , 'proveedores.users_id_user')
                    ->paginate(30);
        return response()->json($proveedor,200);
    }

    public function getAllOrdenes(Request $request)
    {
        $id_proveedor = DB::table('proveedores')->select('id_proveedor')->where('USERS_ID_USER', $request->id_user)->value('id_proveedor');
        $Ordenes = DB::table('ordenes')
                    ->join('detalle_ordenes', 'detalle_ordenes.ordenes_id_orden' , '=' , 'ordenes.id_orden')
                    ->join('productos', 'productos.id_tipop' , '=' , 'detalle_ordenes.productos_id_tipop')
                    ->join('familia_productos', 'familia_productos.id_t_fam' , '=' , 'productos.familia_productos_id_t_fam')
                    ->where('ordenes.proveedores_id_proveedor' , $id_proveedor)
                    ->where('ordenes.estado' , 'S')
                    ->paginate(30);
        return response()->json($Ordenes,200);
    }

    public function aceptarOrden(Request $request)
    {
        $ordenEdit = ordene::find($request->id_orden);
        $ordenEdit->estado = 'A';
        $ordenEdit->fecha_venc_o = $request->fecha_v;
        $ordenEdit->save();
        return response()->json([$ordenEdit], 200);
    }

    public function RechazarOrden(Request $request)
    {
        $ordenEdit = ordene::find($request->id_orden);
        $ordenEdit->estado = 'R';
        $ordenEdit->save();
        return response()->json([$ordenEdit], 200);
    }
}
