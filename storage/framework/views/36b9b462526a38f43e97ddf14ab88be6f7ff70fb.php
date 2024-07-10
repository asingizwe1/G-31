<!DOCTYPE html>
<html>
<head>
    <title>Validate Representative</title>
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
            background-color: #28a745;
            color: #fff;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            text-align: center;
        }
        .card-header h2 {
            margin: 0;
        }
        .btn-primary {
            background-color: #28a745;
            border: none;
        }
        .btn-primary:hover {
            background-color: #218838;
        }
        .form-group label {
            font-weight: bold;
        }
        .alert-danger {
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header">
                <h2>Validate Representative</h2>
            </div>
            <div class="card-body">
                <?php if($errors->any()): ?>
                    <div class="alert alert-danger">
                        <ul>
                            <?php $__currentLoopData = $errors->all(); $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $error): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?>
                                <li><?php echo e($error); ?></li>
                            <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?>
                        </ul>
                    </div>
                <?php endif; ?>

                <form action="<?php echo e(route('school.validate')); ?>" method="POST">
                    <?php echo csrf_field(); ?>
                    <div class="form-group">
                        <label for="school_representative_name">Representative Name:</label>
                        <input type="text" class="form-control" id="school_representative_name" name="school_representative_name" placeholder="Enter Representative Name" required>
                    </div>
                    <div class="form-group">
                        <label for="school_representative_email">Representative Email:</label>
                        <input type="email" class="form-control" id="school_representative_email" name="school_representative_email" placeholder="Enter Representative Email" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Validate</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
<?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/school/validate.blade.php ENDPATH**/ ?>