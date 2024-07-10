<?php $__env->startSection('content'); ?>
    <div class="full-page section-image" data-color="black" data-image="<?php echo e(asset('light-bootstrap/img/full-screen-image-2.jpg')); ?>">
        <div class="content">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-7 col-md-8">
                        <h1 class="text-white text-center"><?php echo e(__('Welcome to Light Bootstrap Dashboard FREE Laravel Live Preview.')); ?></h1>
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
<?php echo $__env->make('layouts/app', ['activePage' => 'welcome', 'title' => 'Light Bootstrap Dashboard Laravel by Creative Tim & UPDIVISION'], \Illuminate\Support\Arr::except(get_defined_vars(), ['__data', '__path']))->render(); ?><?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/welcome.blade.php ENDPATH**/ ?>