<template>
    <footer class="footer">
      <div class="footer-content">
        <p class="footer-text">© 2024 Petstore.</p>
      </div>
    </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
    position: absolute;
    width: 100%;
    height: 3em;
    bottom: 0;
    background-color: #f4f4f4;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: -1;

}

.footer-text {
    margin: 0;
}
</style>
