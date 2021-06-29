<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\ordene;
use App\Models\producto;
use App\Models\recepcione;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class RecepcionController extends Controller
{
    public function aceptarOrden(Request $request)
    {
        $id_emp = DB::table('empleados')->select('id_empleado')->where('USERS_ID_USER', $request->id_user)->value('id_empleado');
        $id_prov = DB::table('ordenes')->select('PROVEEDORES_ID_PROVEEDOR')->where('ID_ORDEN', $request->id_orden)->value('PROVEEDORES_ID_PROVEEDOR');
        $fam_pro = DB::table('detalle_ordenes')->select('productos.FAMILIA_PRODUCTOS_ID_T_FAM')
            ->join('productos', 'productos.id_tipop' , '=' , 'detalle_ordenes.productos_id_tipop')
            ->where('detalle_ordenes.ORDENES_ID_ORDEN', $request->id_orden)
            ->value('productos.FAMILIA_PRODUCTOS_ID_T_FAM');
        $fecha = DB::table('ordenes')->select('FECHA_VENC_O')->where('ID_ORDEN', $request->id_orden)->value('FECHA_VENC_O');
        $id_producto = DB::table('detalle_ordenes')->select('productos_id_tipop')
            ->where('ORDENES_ID_ORDEN', $request->id_orden)
            ->value('productos_id_tipop');
        $stock = DB::table('detalle_ordenes')->select('productos.stock_tipop')
            ->join('productos', 'productos.id_tipop' , '=' , 'detalle_ordenes.productos_id_tipop')
            ->where('detalle_ordenes.ORDENES_ID_ORDEN', $request->id_orden)
            ->value('productos.stock_tipop');
        $cantidad = DB::table('detalle_ordenes')->select('cant_productos')
            ->where('ORDENES_ID_ORDEN', $request->id_orden)
            ->value('cant_productos');
        $fechaa = Carbon::parse($fecha);
        $dfecha = $fechaa->day;
        $mfecha = $fechaa->month;
        $afecha = $fechaa->year;
        $newRecepcion = new recepcione();
        $newRecepcion->codigo = $id_prov.$fam_pro.$dfecha.$mfecha.$afecha;//wea larga XD
        $newRecepcion->comentario = $request->comentario;
        $newRecepcion->estado = 'R';
        $newRecepcion->ordenes_id_orden = $request->id_orden;
        $newRecepcion->empleados_id_empleado = $id_emp;
        $newRecepcion->save();

        $ordenEdit = ordene::find($request->id_orden);
        $ordenEdit->estado = 'E';
        $ordenEdit->save();

        $productoEdit = producto::find($id_producto);
        $productoEdit->stock_tipop = $stock+$cantidad;
        $productoEdit->save();

        return response()->json([$newRecepcion,$ordenEdit,$productoEdit], 200);
    }

    public function getProductosRecibidos(Request $request)
    {
        $Recepciones = DB::table('recepciones')
                    ->join('ordenes', 'ordenes.id_orden' , '=' , 'recepciones.ordenes_id_orden')
                    ->paginate(30);
        return response()->json($Recepciones,200);
    }
}
