<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\detalle_ordene;
use App\Models\ordene;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class OrdenesController extends Controller
{
    public function register(Request $request){
        $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $precio = DB::table('productos')->select('precio_c_tipop')->where('id_tipop', $request->id_producto)->value('id_producto');
        $fecha = Carbon::now();
        $newOrden = new ordene;
        $newOrden->estado                             = 'S';
        $newOrden->fecha_venc_o                            = $fecha;//colocar today
        $newOrden->precio_total                            = '1';
        $newOrden->proveedores_id_proveedor                = $request->id_proveedor;
        $newOrden->empleados_id_empleado                   = $id_emp;
        $newOrden->save();

        $id_orden = $newOrden->id_orden;

        $newDetalle = new detalle_ordene;
        $newDetalle->cant_productos                        = $request->cantidad;
        $newDetalle->precio_productos                      = $precio;
        $newDetalle->productos_id_tipop                    = $request->id_producto;
        $newDetalle->ordenes_id_orden                      = $id_orden;
        $newDetalle->save();

        $newOrden = ordene::find($id_orden);
        $newOrden->precio_total = ($request->cantidad)*$precio;
        $newOrden->save();

        return response()->json([$newOrden,$newDetalle], 200);

    }
}
