@extends('layouts.app', ['activePage' => 'dashboard', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION', 'navName' => 'Dashboard', 'activeButton' => 'laravel'])

@section('content')
<div class="content bgcolor">
   
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-7 col-md-8">
                        <h1 class=" text-center" style = "color:black;">YOU ARE WELCOME TO THE MATHEMATICS WORLD.</h1>
                    </div>
                </div>
            </div>
        </div>
    ,   
@endsection


@push('js')
    <!-- <script type="text/javascript">
        $(document).ready(function() {
            // Javascript method's body can be found in assets/js/demos.js
            demo.initDashboardPageCharts();

            demo.showNotification();

        });
    </script> -->
    <style>
    .bgcolor {
        background-color: neon; /* Set the background color to yellow */
        padding: 2rem; /* Add some padding if needed */
    }
</style>
@endpush