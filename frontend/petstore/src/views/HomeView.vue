<template>
  <div class="home">
    <div class="banner">
      <img :src="require('@/assets/banerxd.png')" alt="Baner z Kotem" class="banner-image" />
    </div>


    <div class="content-container">
      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="index in 4" :key="index">
            <img :src="require('@/assets/kot.avif')" alt="Kot" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="text-content">
        <h2 class="box-title">Słodki kotek</h2>
        <p class="box-text">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed accumsan velit leo, tempor posuere dolor porttitor nec. Proin auctor orci et velit ultrices eleifend. Duis cursus egestas justo, sit amet consectetur ligula malesuada non.
        </p>
        <div class="button-container">
          <router-link to="/animals">
    <el-button type="primary">Zobacz ofertę zwierząt ></el-button>
  </router-link>
        </div>
      </div>
    </div>


    <div class="content-container">
      <div class="text-content reverse-layout">
        <h2 class="box-title" style="">Wyborna karma</h2>
        <p class="box-text">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed accumsan velit leo, tempor posuere dolor porttitor nec. Proin auctor orci et velit ultrices eleifend. Duis cursus egestas justo, sit amet consectetur ligula malesuada non.
        </p>
        <div class="button-container">
          <router-link to="/products">
    <el-button type="primary">Zobacz ofertę produktów ></el-button>
  </router-link>
        </div>
      </div>

      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="index in 4" :key="index">
            <img :src="require('@/assets/kot.avif')" alt="Kot" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
:root {
  --el-color-primary: #67C23A;
  --el-color-primary-light-3: #5DA237;
  --el-color-primary-dark-2: #4CAF50;
}

.home {
  padding: 20px 0px;
}

.banner {
  display: flex;
  justify-content: center;
  overflow: hidden;
  width: 100%;
}

.banner-image {
  width: 100%;
  height: 100%;
  max-height: 400px;
  object-fit: cover;
}

.content-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.carousel {
  flex: 1;
  /* margin: 0 20px; */
}

.carousel-container {
  height: 250px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.text-content {
  flex: 2;
  height: 100%;
  padding: 20px;
  background-color: transparent;
  display: flex;
  flex-direction: column;
  gap: 1em;
}

.box-title {
  margin: 0;
  color: var(--el-color-primary);
  font-size: 40px;
}

.box-text {
  margin: 0;
  font-family: "Quicksand", sans-serif;
}

.reverse-layout{
  text-align: right;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
}
.button-container {
  display: flex;
  justify-content: flex-start;
}
</style>

