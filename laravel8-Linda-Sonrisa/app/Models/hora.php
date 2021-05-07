<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class hora extends Model
{
    public $timestamps = false;
    protected $primaryKey = 'id_hora';
    use HasFactory;
}
