<template>
  <div class="home">
    <div class="banner">
      <img :src="require('@/assets/banerxd.png')" alt="Baner z Kotem" class="banner-image" />
    </div>

    <div class="carousels-container">
      <div class="left-carousel">
        <el-carousel :interval="5000" arrow="always">
          <el-carousel-item v-for="index in 3" :key="index">
            <img :src="require('@/assets/kot.avif')" alt="Kot" class="small-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  padding: 20px;
}

.banner {
  display: flex;
  justify-content: center;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: auto;
  max-height: 400px;
  object-fit: cover;
}

.carousels-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.small-image {
  width: 400px;
  height: 200px;
  object-fit: cover;
}

.left-carousel, .right-carousel {
  flex: 1;
  margin: 0 10px;
}
</style>
