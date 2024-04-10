var now = new Date();
console.log(now);

var then = new Date(2008, 5, 10);
console.log(then);

var elapsed = now - then;
console.log(elapsed);

// 실행 시간을 측정하는 예제
var start = new Date();
for(var i = 0; i < 1000000000; ++i);
var end = new Date();
console.log(end - start);