function myConcat(separator) {
  var s = "";
  for (var i = 1; i < arguments.length; ++i) {
    s += arguments[i];
    if (i < arguments.length - 1) {
      s += separator;
    }
  }
  return s;
}

console.log(myConcat("/", "pen", "pineapple", "apple", "pen"));

function makeCounter() {
  var count = 0;
  ++count;
  return function() {
    return ++count;
  };
}

var counter = makeCounter();
console.log(counter());
console.log(counter());
console.log(counter());