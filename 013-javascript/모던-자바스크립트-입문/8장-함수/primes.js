// 클로저를 활용한 임의의 소수 곱을 출력하는 함수
function primes(n) {
  var p = [];
  for (var i = 2; i <= n; ++i) {
    p[i] = true;
  }

  var max = Math.floor(Math.sqrt(n));
  var x = 2;
  while (x <= max) {
    for (var i = 2 * x; i <= n; i += x) {
      p[i] = false;
    }
    while (!p[++x]);
  }
  // 소수만 꺼내서 배열 primes에 저장
  var primes = [], nprimes = 0;
  for (var i = 2; i <= n; ++i) {
    if(p[i]) {
      primes[nprimes++] = i;
    }
  }
  p = null;       // 메모리 해제
  return function (m) {
    for (var i = 0, product = 1; i < m; ++i) {
      var value = primes[Math.floor(Math.random() * nprimes)];
      //5console.log(value);
      product *= value;
    }
    return product;
  }
};

var primeProduct = primes(10000);
console.log(primeProduct(2));
console.log(primeProduct(2));