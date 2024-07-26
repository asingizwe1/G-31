<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Challenge;

class ChallengeController extends Controller
{
    public function create()
    {
        return view('challenge.create');
    }

    public function store(Request $request)
    {
        $request->validate([
            'opening_date' => 'required|date',
            'closing_date' => 'required|date',
            'duration' => 'required|integer',
            'number_of_questions' => 'required|integer',
            'Challengename'=>'required|string'
        ]);

        Challenge::create([
            'opening_date' => $request->opening_date,
            'closing_date' => $request->closing_date,
            'duration' => $request->duration,
            'number_of_questions' => $request->number_of_questions,
            'Challengename'=> $request->Challengename
        ]);

        return redirect()->back()->with('success', 'Challenge set successfully.');
    }
}
