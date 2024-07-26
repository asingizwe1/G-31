<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Imports\AnswerImport;
use Maatwebsite\Excel\Facades\Excel;

class AnswerController extends Controller
{
    public function index()
    {   
    
      
        return view('answer.index',);
    }
    
    public function importExcelData(Request $request)
    {
        $request->validate([
            'import_file'=>[
              'required', 
              'file',
            ],
        ]);

        Excel::import(new AnswerImport,  $request->file('import_file'));
        return redirect()->back()->with('status', 'Imported Successfully');
    }
}
