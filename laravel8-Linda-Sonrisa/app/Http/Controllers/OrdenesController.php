<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\detalle_orden;
use App\Models\orden;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class OrdenesController extends Controller
{
    public function register(Request $request){
        $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $precio = DB::table('productos')->select('precio_v_tipop')->where('id_tipop', $request->id_producto)->value('id_producto');
        $newOrden = new orden();
        $newOrden->cancelada_o                             = 'N';
        $newOrden->fecha_venc_o                            = $request->fecha;
        $newOrden->precio_total                            = '1';
        $newOrden->proveedores_id_proveedor                = $request->id_proveedor;
        $newOrden->empleados_id_empleado                   = $id_emp;
        $newOrden->save();

        $id_orden = $newOrden->id_orden;

        $newDetalle = new detalle_orden();
        $newDetalle->cant_productos                        = $request->cantidad;
        $newDetalle->precio_productos                      = ($request->cantidad)*$precio;
        $newDetalle->productos_id_tipop                    = $request->id_producto;
        $newDetalle->ordenes_id_orden                      = $id_orden;
        $newDetalle->save();

        return response()->json([$newOrden,$newDetalle], 200);

    }
}
