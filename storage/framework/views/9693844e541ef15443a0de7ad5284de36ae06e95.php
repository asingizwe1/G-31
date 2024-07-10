<!DOCTYPE html>
<html>
<head>
    <title>Upload School Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            margin-top: 50px;
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .card-header {
            background-color: #007bff;
            color: #fff;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }
        .card-header h2 {
            margin: 0;
        }
        .btn-primary {
            background-color: #007bff;
            border: none;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .form-group label {
            font-weight: bold;
        }
        .alert-success {
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header text-center">
                <h2>Upload School Details</h2>
            </div>
            <div class="card-body">
                <?php if($message = Session::get('success')): ?>
                    <div class="alert alert-success">
                        <p><?php echo e($message); ?></p>
                    </div>
                <?php endif; ?>

                <form action="<?php echo e(route('school.store')); ?>" method="POST">
                    <?php echo csrf_field(); ?>
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
</body>
</html>
<?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/school/create.blade.php ENDPATH**/ ?>