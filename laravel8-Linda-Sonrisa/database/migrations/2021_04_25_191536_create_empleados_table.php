<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateEmpleadosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('empleados', function (Blueprint $table) {
            $table->id();
            $table->string('rut_e');
            $table->string('nombre_e');
            $table->string('direccion_e');
            $table->string('num_telefono_e');
            $table->date('fecha_nac_e');
            $table->boolean('salud_e');
            $table->string('salario');
            $table->date('inicio_contrato');
            $table->date('fin_contrato');
            $table->string('afp');
            $table->string('nombre_banco');
            $table->string('numero_banco');
            $table->string('tipo_cuenta');
            $table->string('imagen');
            $table->unsignedBigInteger('sucursal_id');
            $table->foreign('sucursal_id')->references('id')->on('sucursals');
            $table->unsignedBigInteger('user_id');
            $table->foreign('user_id')->references('id')->on('users');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('empleados');
    }
}
