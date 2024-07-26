<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('verified_representative', function (Blueprint $table) {
            $table->id();
            $table->string('reg_no');
            $table->string('Firstname');
            $table->string('Lastname');
            $table->string('Username');
            $table->date('DOB');
            $table->string('Email');
            $table->string('Password');

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
        Schema::dropIfExists('verified_representative');
    }
};
