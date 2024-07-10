<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class School extends Model
{
    use HasFactory;
    protected $fillable = [
        'reg_no',
        'name',
        'district',
        'school_representative_name',
        'school_representative_email'
    ];
    protected $table = 'school';
}
