// 부분 적용된 함수를 반환하는 함수
function partial(f) {
  var args = arguments;
  return function () {
    // 외부 함수의 두번째 인수부터 변수 a에 복사
    var a = Array.prototype.slice.call(args, 1);
    for (i = 0, j = 0; i < a.length; ++i) {
      // 외부 함수의 두번째 인수가 정의되지 않았으면 이 함수의 arguments를 사용함
      if (!a[i]) {
        a[i] = arguments[j++];
      }
    }
    return f.apply(this, a);
  }
}

var square = partial(Math.pow, undefined, 2);
var sqrt = partial(Math.pow, undefined, 0.5);
var cubicroot = partial(Math.pow, undefined, 1 / 3);
var exp = partial(Math.pow, Math.E, undefined);
