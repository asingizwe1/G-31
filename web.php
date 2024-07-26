<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\SchoolController;






/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
Auth::routes();

Route::get('/home', 'App\Http\Controllers\HomeController@index')->name('dashboard');

Route::group(['middleware' => 'auth'], function () {
	Route::resource('user', 'App\Http\Controllers\UserController', ['except' => ['show']]);
	Route::get('profile', ['as' => 'profile.edit', 'uses' => 'App\Http\Controllers\ProfileController@edit']);
	Route::patch('profile', ['as' => 'profile.update', 'uses' => 'App\Http\Controllers\ProfileController@update']);
	Route::patch('profile/password', ['as' => 'profile.password', 'uses' => 'App\Http\Controllers\ProfileController@password']);
});

Route::group(['middleware' => 'auth'], function () {
	Route::get('{page}', ['as' => 'page.index', 'uses' => 'App\Http\Controllers\PageController@index']);
});




Route::get('school/create', [SchoolController::class, 'create'])->name('school.create');
Route::post('school', [SchoolController::class, 'store'])->name('school.store');









use App\Http\Controllers\ParameterController;

Route::get('/parameters/create', [ParameterController::class, 'create'])->name('parameters.create');
Route::post('/parameters/store', [ParameterController::class, 'store'])->name('parameters.store');

use App\Http\Controllers\QuestionController;

Route::get('question/import', [App\Http\Controllers\Questioncontroller::class, 'index'] )->name('questions.index');
Route::post('question/import',[App\Http\Controllers\Questioncontroller::class, 'importExcelData'])->name('question.import');

//use App\Http\Controllers\AnswerController;

//Route::get('answer/import', [App\Http\Controllers\Answercontroller::class, 'index'] )->name('answers.index');
//Route::post('answer/import',[App\Http\Controllers\Answercontroller::class, 'importExcelData'])->name('answer.import');

use App\Http\Controllers\ChallengeController;

Route::get('/challenge/create', [ChallengeController::class, 'create'])->name('challenge.create');
Route::post('/challenge/store', [ChallengeController::class, 'store'])->name('challenge.store');


use App\Http\Controllers\AnswerController;

Route::get('answer/import', [App\Http\Controllers\Answercontroller::class, 'index'] )->name('answers.index');
Route::post('answer/import',[App\Http\Controllers\Answercontroller::class, 'importExcelData'])->name('answer.import');

