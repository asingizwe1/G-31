@extends('layouts.app', ['activePage' => 'table', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION', 'navName' => 'Table List', 'activeButton' => 'laravel'])
@section('content')

<!-- <!DOCTYPE html>
<html>
<head>
    <title>Set Parameters</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body> -->
    <div class="container mt-5">
        <h2>Set Challenge</h2>
        @if (session('success'))
            <div class="alert alert-success">
                {{ session('success') }}
            </div>
        @endif
        <form action="{{ route('challenge.store') }}" method="POST">
            @csrf
            <div class="form-group">
                <label for="opening_date">Opening Date</label>
                <input type="date" name="opening_date" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="closing_date">Closing Date</label>
                <input type="date" name="closing_date" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="duration">Duration of Challenge(minutes)</label>
                <input type="number" name="duration" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="number_of_questions">Number of Questions</label>
                <input type="number" name="number_of_questions" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="Challengename">Challengename</label>
                <input type="number" name="Challengename" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
<!-- </body>
</html> -->
@endsection
