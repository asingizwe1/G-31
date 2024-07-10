<?php

namespace App\Http\Controllers;
use App\Models\School;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Routing\Controller;

class SchoolController extends Controller
{
    public function create()
    {
        return view('school.create');
    }

    public function store(Request $request)
    {
        $request->validate([
            'reg_no' => 'required',
            'name' => 'required',
            'district' => 'required',
            'school_representative_name' => 'required',
            'school_representative_email' => 'required|email|unique:school',
        ]);

        School::create($request->all());

        return redirect()->route('school.create')
            ->with('success', 'School details have been successfully added.');
    }

    public function showValidateForm()
    {
        return view('school.validate');
    }

    public function validateRepresentative(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'school_representative_name' => 'required|string|max:255',
            'school_representative_email' => 'required|email|unique:school,school_representative_email',
        ]);

        if ($validator->fails()) {
            return redirect()->route('school.validate')
                ->withErrors($validator)
                ->withInput();
        }

        return redirect()->route('school.validate')
            ->with('success', 'Representative is valid.');
    }
}
