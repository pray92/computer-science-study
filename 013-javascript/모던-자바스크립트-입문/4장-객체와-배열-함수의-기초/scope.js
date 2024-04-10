var a = "global";
function f() {
  var a = "local";
  console.log(a);
  return a;
}
console.log("f() : " + f());
console.log("a : " + a);
