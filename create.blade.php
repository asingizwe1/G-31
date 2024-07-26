@extends('layouts.app', ['activePage' => 'table', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION', 'navName' => 'Table List', 'activeButton' => 'laravel'])

@section('content')
    <div class="container">
        <div class="card">
            <div class="card-header text-center">
                <h2>Upload School Details</h2>
            </div>
            <div class="card-body">
                @if ($message = Session::get('success'))
                    <div class="alert alert-success">
                        <p>{{ $message }}</p>
                    </div>
                @endif

                <form action="{{ route('school.store') }}" method="POST">
                    @csrf
                    <div class="form-group">
                        <label for="reg_no">Registration Number:</label>
                        <input type="text" class="form-control" id="reg_no" name="reg_no" placeholder="Enter Registration Number" required>
                    </div>
                    <div class="form-group">
                        <label for="name">Name:</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Enter School Name" required>
                    </div>
                    <div class="form-group">
                        <label for="district">District:</label>
                        <input type="text" class="form-control" id="district" name="district" placeholder="Enter District" required>
                    </div>
                    <div class="form-group">
                        <label for="school_representative_name">School Representative Name:</label>
                        <input type="text" class="form-control" id="school_representative_name" name="school_representative_name" placeholder="Enter Representative Name" required>
                    </div>
                    <div class="form-group">
                        <label for="school_representative_email">School Representative Email:</label>
                        <input type="email" class="form-control" id="school_representative_email" name="school_representative_email" placeholder="Enter Representative Email" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Submit</button>
                </form>
            </div>
        </div>
    </div>

    <style>
        .form-group label {
            /* font-weight: bold; */
            color: black; 
        }
    </style>
@endsection
