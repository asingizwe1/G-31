<div class="sidebar" data-image="<?php echo e(asset('light-bootstrap/img/sidebar-5.jpg')); ?>">
    <!--
Tip 1: You can change the color of the sidebar using: data-color="purple | blue | green | orange | red"

Tip 2: you can also add an image using data-image tag
-->
    <div class="sidebar-wrapper">
        <div class="logo">
            <a href="http://www.creative-tim.com" class="simple-text">
                <?php echo e(__("Creative Tim")); ?>

            </a>
        </div>
        <ul class="nav">
            <li class="nav-item <?php if($activePage == 'dashboard'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('dashboard')); ?>">
                    <i class="nc-icon nc-chart-pie-35"></i>
                    <p><?php echo e(__("Dashboard")); ?></p>
                </a>
            </li>
           
            <li class="nav-item">
                <a class="nav-link" data-toggle="collapse" href="#laravelExamples" <?php if($activeButton =='laravel'): ?> aria-expanded="true" <?php endif; ?>>
                    <i>
                        <img src="<?php echo e(asset('light-bootstrap/img/laravel.svg')); ?>" style="width:25px">
                    </i>
                    <p>
                        <?php echo e(__('Laravel example')); ?>

                        <b class="caret"></b>
                    </p>
                </a>
                <div class="collapse <?php if($activeButton =='laravel'): ?> show <?php endif; ?>" id="laravelExamples">
                    <ul class="nav">
                        <li class="nav-item <?php if($activePage == 'user'): ?> active <?php endif; ?>">
                            <a class="nav-link" href="<?php echo e(route('school.create')); ?>">
                                <i class="nc-icon nc-single-02"></i>
                                <p><?php echo e(__("Register School")); ?></p>
                            </a>
                        </li>
                        <li class="nav-item <?php if($activePage == 'user-management'): ?> active <?php endif; ?>">
                            <a class="nav-link" href="<?php echo e(route('school.validate')); ?>">
                                <i class="nc-icon nc-circle-09"></i>
                                <p><?php echo e(__("validate Representative")); ?></p>
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <li class="nav-item <?php if($activePage == 'table'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('page.index', 'table')); ?>">
                    <i class="nc-icon nc-notes"></i>
                    <p><?php echo e(__("Table List")); ?></p>
                </a>
            </li>
            <li class="nav-item <?php if($activePage == 'typography'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('page.index', 'typography')); ?>">
                    <i class="nc-icon nc-paper-2"></i>
                    <p><?php echo e(__("Typography")); ?></p>
                </a>
            </li>
            <li class="nav-item <?php if($activePage == 'icons'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('page.index', 'icons')); ?>">
                    <i class="nc-icon nc-atom"></i>
                    <p><?php echo e(__("Icons")); ?></p>
                </a>
            </li>
            <li class="nav-item <?php if($activePage == 'maps'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('page.index', 'maps')); ?>">
                    <i class="nc-icon nc-pin-3"></i>
                    <p><?php echo e(__("Maps")); ?></p>
                </a>
            </li>
            <li class="nav-item <?php if($activePage == 'notifications'): ?> active <?php endif; ?>">
                <a class="nav-link" href="<?php echo e(route('page.index', 'notifications')); ?>">
                    <i class="nc-icon nc-bell-55"></i>
                    <p><?php echo e(__("Notifications")); ?></p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="<?php echo e(route('page.index', 'upgrade')); ?>">
                    <i class="nc-icon nc-alien-33"></i>
                    <p><?php echo e(__("Upgrade to PRO")); ?></p>
                </a>
            </li>
        </ul>
    </div>
</div>
<?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/layouts/navbars/sidebar.blade.php ENDPATH**/ ?>