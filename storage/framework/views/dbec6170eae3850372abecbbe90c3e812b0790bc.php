<?php $__env->startSection('content'); ?>
    <div class="full-page register-page section-image" data-color="orange" data-image="<?php echo e(asset('light-bootstrap/img/bg5.jpg')); ?>">
        <div class="content">
            <div class="container">
                <div class="card card-register card-plain text-center">
                    <div class="card-body ">
                        <div class="row">
                            <div class="col-md-5 ml-auto">
                                <div class="media">
                                    <div class="media-left">
                                        <div class="icon">
                                            <i class="nc-icon nc-circle-09"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">
                                        <h4><?php echo e(__('Free Account')); ?></h4>
                                        <p><?php echo e(__('Here you can write a feature description for your dashboard, let the users know what is the value that you give them.')); ?></p>
                                    </div>
                                </div>
                                <div class="media">
                                    <div class="media-left">
                                        <div class="icon">
                                            <i class="nc-icon nc-preferences-circle-rotate"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">
                                        <h4><?php echo e(__('Awesome Performances')); ?></h4>
                                        <p><?php echo e(__('Here you can write a feature description for your dashboard, let the users know what is the value that you give them.')); ?></p>
                                    </div>
                                </div>
                                <div class="media">
                                    <div class="media-left">
                                        <div class="icon">
                                            <i class="nc-icon nc-planet"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">
                                        <h4><?php echo e(__('Global Support')); ?></h4>
                                        <p><?php echo e(__('Here you can write a feature description for your dashboard, let the users know what is the value that you give them.')); ?></p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mr-auto">
                                <form method="POST" action="<?php echo e(route('register')); ?>">
                                    <?php echo csrf_field(); ?>
                                    <div class="card card-plain">
                                        <div class="content">
                                            <div class="form-group">
                                                <input type="text" name="name" id="name" class="form-control" placeholder="<?php echo e(__('Name')); ?>" value="<?php echo e(old('name')); ?>" required autofocus>
                                            </div>

                                            <div class="form-group">   
                                                <input type="email" name="email" value="<?php echo e(old('email')); ?>" placeholder="Enter email" class="form-control" required>
                                            </div>

                                            <div class="form-group">
                                                <input type="password" name="password" class="form-control" required >
                                            </div>
                                            <div class="form-group">
                                                <input type="password" name="password_confirmation" placeholder="Password Confirmation" class="form-control" required autofocus>
                                            </div>
                                            <div class="form-group d-flex justify-content-center">
                                                <div class="form-check rounded col-md-10 text-left">
                                                    <label class="form-check-label text-white d-flex align-items-center">
                                                        <input class="form-check-input" name="agree" type="checkbox" required >
                                                        <span class="form-check-sign"></span>
                                                        <b><?php echo e(__('Agree with terms and conditions')); ?></b>
                                                    </label>
                                                </div>
                                            </div>

                                            <div class="footer text-center">
                                                <button type="submit" class="btn btn-fill btn-neutral btn-wd"><?php echo e(__('Create Free Account')); ?></button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col">
                                <?php $__currentLoopData = $errors->all(); $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $error): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?>
                                    <div class="alert alert-warning alert-dismissible fade show" >
                                        <a href="#" class="close" data-dismiss="alert" aria-label="close"> &times;</a>
                                        <?php echo e($error); ?>

                                    </div>
                                <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<?php $__env->stopSection(); ?>

<?php $__env->startPush('js'); ?>
    <script>
        $(document).ready(function() {
            demo.checkFullPageBackgroundImage();

            setTimeout(function() {
                // after 1000 ms we add the class animated to the login/register card
                $('.card').removeClass('card-hidden');
            }, 700)
        });
    </script>
<?php $__env->stopPush(); ?>
<?php echo $__env->make('layouts.app', ['activePage' => 'register', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION'], \Illuminate\Support\Arr::except(get_defined_vars(), ['__data', '__path']))->render(); ?><?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/auth/register.blade.php ENDPATH**/ ?>