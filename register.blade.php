@extends('layouts.app', ['activePage' => 'register', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION'])
@section('content')
    <div class="full-page register-page section-image" data-color="orange" data-image="{{ asset('light-bootstrap/img/img/bg4.jpg') }}">
        <div class="content">
            <div class="container">
                <div class="card card-register card-plain text-center">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 ml-auto">
                                <h3 class="card-title">{{ __('Create Your Account') }}</h3>
                                <p class="description">{{ __('Fill in the details below to create your account. Ensure all fields are filled out correctly.') }}</p>
                            </div>
                            <div class="col-md-6 mr-auto">
                                <form method="POST" action="{{ route('register') }}">
                                    @csrf
                                    <div class="card card-plain">
                                        <div class="content">
                                            <div class="form-group">
                                                <label for="name" class="custom-label">{{ __('Full Name') }}</label>
                                                <input type="text" name="name" id="name" class="form-control" placeholder="John Doe" value="{{ old('name') }}" required autofocus>
                                            </div>

                                            <div class="form-group">
                                                <label for="email" class="custom-label">{{ __('Email Address') }}</label>
                                                <input type="email" name="email" id="email" class="form-control" placeholder="example@example.com" value="{{ old('email') }}" required>
                                            </div>

                                            <div class="form-group">
                                                <label for="password" class="custom-label">{{ __('Password') }}</label>
                                                <input type="password" name="password" id="password" class="form-control" placeholder="********" required>
                                            </div>

                                            <div class="form-group">
                                                <label for="password_confirmation" class="custom-label">{{ __('Confirm Password') }}</label>
                                                <input type="password" name="password_confirmation" id="password_confirmation" class="form-control" placeholder="********" required>
                                            </div>

                                            <div class="form-group d-flex justify-content-center">
                                                <div class="form-check rounded col-md-10 text-left">
                                                    <label class="form-check-label text-white d-flex align-items-center">
                                                        <input class="form-check-input" name="agree" type="checkbox" required>
                                                        <span class="form-check-sign"></span>
                                                        <b>{{ __('I agree to the terms and conditions') }}</b>
                                                    </label>
                                                </div>
                                            </div>

                                            <div class="footer text-center">
                                                <button type="submit" class="btn btn-fill btn-neutral btn-wd">{{ __('Create Account') }}</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col">
                                @foreach ($errors->all() as $error)
                                    <div class="alert alert-warning alert-dismissible fade show">
                                        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                        {{ $error }}
                                    </div>
                                @endforeach
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection

@push('css')
    <style>
        .custom-label {
            color: green;
            font-weight: bold;
        }
    </style>
@endpush

@push('js')
    <script>
        $(document).ready(function() {
            demo.checkFullPageBackgroundImage();

            setTimeout(function() {
                // after 700 ms we add the class animated to the login/register card
                $('.card').removeClass('card-hidden');
            }, 700);
        });
    </script>
@endpush