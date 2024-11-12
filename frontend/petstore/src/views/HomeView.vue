<template>
  <div class="home">
    <div class="banner">
      <img :src="require('@/assets/banerxd.jpg')" alt="Baner z Kotem" class="banner-image" />
      <div class="banner-text">
        <h1 class="banner-title">Witamy w Petstore</h1>
        <p class="banner-subtext">
          Oferujemy szeroki wybór produktów dla zwierząt domowych — od najwyższej jakości karmy po stylowe akcesoria.
        </p>
      </div>
    </div>

    <div class="content-container">
      <div class="paw-watermark paw-watermark-first"></div>

      <div class="carousel">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="(img, index) in catImages" :key="index">
            <img :src="require(`@/assets/${img}`)" :alt="`Kot ${index + 1}`" class="carousel-image" />
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
      <div class="paw-watermark paw-watermark-second"></div>

      <div class="text-content reverse-layout">
        <h2 class="box-title">Wyborna karma</h2>
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
          <el-carousel-item v-for="(img, index) in foodImages" :key="index">
            <img :src="require(`@/assets/${img}`)" :alt="`Karma ${index + 1}`" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>





<script setup>
const catImages = [
  'kot.avif',
  'img4.jpg',
  'img5.jpg',
  'img6.jpg'
];

const foodImages = [
  'img1.jpg',
  'img2.jpg',
  'img3.jpg',
  'img7.jpg'
];
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
  position: relative;
  display: flex;
  justify-content: flex-start;
  overflow: hidden;
  width: 100%;
}

.banner-image {
  width: 100%;
  height: 100%;
  max-height: 400px;
  object-fit: cover;
}

.banner-text {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 50%;
  color: black;
  text-align: left;
  margin-left: 20px;
  margin-top: 40px;
}

.banner-title {
  margin: 0;
  color: var(--el-color-primary);
  font-size: 48px;
}

.banner-subtext {
  margin: 5px 0 0;
  font-size: 24px;
  font-family: "Quicksand", sans-serif;
  color: black;
}

.content-container {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 50px;
  margin-bottom: 50px;
}

.carousel {
  flex: 1;
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
  font-size: 20px;
  margin-bottom: 20px;
}

.reverse-layout {
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

.paw-watermark {
  position: absolute;
  background-image: url("https://api.iconify.design/mdi:paw.svg?color=%2367C23A");
  background-size: contain;
  background-repeat: no-repeat;
  opacity: 0.2;
  width: 250px;
  height: 250px;
  z-index: -1;
}

.paw-watermark-first {
  top: 1%;
  left: 60%;
  transform: rotate(-30deg);
}

.paw-watermark-second {
  top: 54%;
  left: 35%;
  transform: translate(-50%, -50%) rotate(30deg);
}
</style>

