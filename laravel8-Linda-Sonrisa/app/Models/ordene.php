<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class ordene extends Model
{
    public $timestamps = false;
    protected $primaryKey = 'id_orden';
    use HasFactory;
}
