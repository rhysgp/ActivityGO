<script setup lang="ts">

import { onMounted, ref } from 'vue'

interface BoardProps {
  size: number;
}

interface Sizes {
  readonly width: Number;
  readonly height: Number;
  readonly hGap: Number;
  readonly vGap: Number;
  readonly hBorder: Number;
  readonly vBorder: Number;
}

const props = defineProps<BoardProps>()

const canvasRef = ref<HTMLCanvasElement | null>(null);

const margin = 3;

const calculatedSizes = (): Sizes | undefined => {
  const canvas = canvasRef.value;
  if (!canvas) return undefined;

  const width = canvas.width - margin * 2;
  const height = canvas.height - margin * 2;
  const hGap = width / props.size;
  const vGap = height / props.size;
  const hBorder = hGap / 2;
  const vBorder = vGap / 2;

  return {
    width,
    height,
    hGap,
    vGap,
    hBorder,
    vBorder,
  } as Sizes;
};

const drawBoard = () => {

  const canvas = canvasRef.value;
  if (!canvas) return;

  const { width, height, hGap, vGap, hBorder, vBorder } = calculatedSizes()

  const ctx = canvas.getContext('2d');

  console.log(`Scaling factor: ${ctx.scalingFactor}`)

  ctx.fillStyle = '#dec8aa';
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  ctx.strokeStyle = 'darkslategray'; // black
  ctx.lineWidth = 1;

  for (let i = 0; i < props.size; ++i) {
    // draw vertical line:
    const x = Math.round(hBorder + i * hGap) + 0.5 + margin;
    ctx.beginPath();
    ctx.moveTo(x, Math.round(vBorder) + 0.5 + margin);
    ctx.lineTo(x, Math.round(height - vBorder) + 0.5 + margin);
    ctx.stroke();

    // draw horizontal line:
    const y = Math.round(vBorder + i * vGap) + 0.5 + margin;
    ctx.beginPath();
    ctx.moveTo(Math.round(hBorder) + 0.5 + margin, y);
    ctx.lineTo(Math.round(width - hBorder) + 0.5 + margin, y);
    ctx.stroke();
  }
};

const drawStone = (gridX: Number, gridY: Number) => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const ctx = canvas.getContext('2d');

  const { hGap, vGap } = calculatedSizes();

  const x = Math.round(margin + gridX * hGap + hGap / 2);
  const y = Math.round(margin + gridY * vGap + vGap / 2);

  ctx.beginPath();
  ctx.arc(x, y, Math.round(hGap / 2 - 1), 0, 2 * Math.PI);
  // ctx.fillStyle = '#fffff0';
  ctx.fillStyle = '#222222';
  ctx.fill();

};

onMounted(() => {
  console.log("HERE");
  drawBoard();

  drawStone(0, 0);
  drawStone(0, 1);
  drawStone(1, 1);
  drawStone(2, 2);
  drawStone(3, 3);
  drawStone(4, 4);
  drawStone(5, 5);
  drawStone(6, 6);
  drawStone(7, 7);
  drawStone(8, 8);
  drawStone(9, 9);
  drawStone(10, 10);
  drawStone(11, 11);
  drawStone(12, 12);
  drawStone(13, 13);
  drawStone(14, 14);
  drawStone(15, 15);
  drawStone(16, 16);
  drawStone(17, 17);
  drawStone(18, 18);
});

</script>
<template>

  <!--
   - Need to show the board at the specified size...
   -->
  <canvas class="board" ref="canvasRef" width="1024" height="1024">
  </canvas>

</template>
<style scoped>
</style>
