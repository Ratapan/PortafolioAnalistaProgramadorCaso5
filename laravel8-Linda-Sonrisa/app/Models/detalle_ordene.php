<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class detalle_ordene extends Model
{
    public $timestamps = false;
    protected $primaryKey = 'id_producto_orden';
    use HasFactory;
}
