<footer class="footer">
    <div class="container <?php if(auth()->guard()->check()): ?>-fluid <?php endif; ?>">
        <nav>
            <ul class="footer-menu">
                <li>
                    <a href="https://www.creative-tim.com" class="nav-link" target="_blank"><?php echo e(__('Creative Tim')); ?></a>
                </li>
                <li>
                    <a href="https://www.updivision.com" class="nav-link" target="_blank"><?php echo e(__('Updivision')); ?></a>
                </li>
                <li>
                    <a href="https://www.creative-tim.com/presentation" class="nav-link" target="_blank"><?php echo e(__('About Us')); ?></a>
                </li>
                <li>
                    <a href="http://blog.creative-tim.com" class="nav-link" target="_blank"><?php echo e(__('Blog')); ?></a>
                </li>
            </ul>
            <p class="copyright text-center">
                ©
                <script>
                    document.write(new Date().getFullYear())
                </script>
                <a href="http://www.creative-tim.com"><?php echo e(__('Creative Tim')); ?></a> &amp; <a href="https://www.updivision.com"><?php echo e(__('Updivision')); ?></a> <?php echo e(__(', made with love for a better web')); ?>

            </p>
        </nav>
    </div>
</footer><?php /**PATH C:\xampp\htdocs\MathsChallenge\resources\views/layouts/footer/nav.blade.php ENDPATH**/ ?>