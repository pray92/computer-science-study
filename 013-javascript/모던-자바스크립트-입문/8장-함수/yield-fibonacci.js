function* fibonacci() {
  var fn1 = 0, fn2 = 1;
  while (true) {
    var fnew = fn1 + fn2;
    fn1 = fn2;
    fn2 = fnew;
    reset = yield fn1;
    if (reset) {
      fn1 = 0; fn2 = 1;
    }
  }
}

var iter = fibonacci();
for(var i = 0; i < 10; ++i) {
  console.log(iter.next().value);
}
console.log(iter.next().value);
console.log(iter.next(true).value);
console.log(iter.next().value);
console.log(iter.next().value);