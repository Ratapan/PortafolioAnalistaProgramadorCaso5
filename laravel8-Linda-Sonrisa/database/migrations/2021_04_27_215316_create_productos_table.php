<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateProductosTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('productos', function (Blueprint $table) {
            $table->id();
            $table->string('nombre_tipop');
            $table->string('desc_tipop');
            $table->string('precio_c_tipop');
            $table->string('precio_v_tipop');
            $table->string('stock_tipop');
            $table->string('stock_c_tipop');
            $table->unsignedBigInteger('familia_producto_id');
            $table->foreign('familia_producto_id')->references('id')->on('familia_productos');
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
        Schema::dropIfExists('productos');
    }
}
