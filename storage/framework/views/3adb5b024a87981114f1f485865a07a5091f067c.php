<nav class="navbar navbar-expand-lg navbar-transparent navbar-absolute">
    <div class="container">
        <div class="navbar-wrapper">
            <a class="navbar-brand" href="#pablo"><?php echo e(__('Light Bootstrap Dashboard Laravel')); ?></a>
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-bar burger-lines"></span>
                <span class="navbar-toggler-bar burger-lines"></span>
                <span class="navbar-toggler-bar burger-lines"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navbar">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="<?php echo e(route('dashboard')); ?>" class="nav-link">
                        <i class="nc-icon nc-chart-pie-35"></i> <?php echo e(__('Dashboard')); ?>

                    </a>
                </li>
                <li class="nav-item <?php if($activePage == 'register'): ?> active <?php endif; ?>">
                    <a href="<?php echo e(route('register')); ?>" class="nav-link">
                        <i class="nc-icon nc-badge"></i> <?php echo e(__('Register')); ?>

                    </a>
                </li>
                <li class="nav-item <?php if($activePage == 'login'): ?> active <?php endif; ?>">
                    <a href="<?php echo e(route('login')); ?>" class="nav-link">
                        <i class="nc-icon nc-mobile"></i> <?php echo e(__('Login')); ?>

                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav><?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/layouts/navbars/navs/guest.blade.php ENDPATH**/ ?>