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
        Schema::create('_challenge', function (Blueprint $table) {
            $table->id();
            $table->string('opening_date');
            $table->date('closing_date');
            $table->integer('duration'); // Duration in minutes
            $table->integer('number_of_questions');
            $table->string('Challengename');
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
        Schema::dropIfExists('_challenge');
    }
};
