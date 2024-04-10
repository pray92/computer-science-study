

setBallProperties(1, 1, 2, 2, 5);                       
setBallProperties({ x: 1, y : 3, vx: 10, vy: 15, radius: 5});   // 동작 X

function setBallProperties(x, y, vx, vy, radius) {
  console.log("x : "  + x  + ", y : " + y + ", vx : " + vx + ", vy : " + vy + ", radius : " + radius);
}
