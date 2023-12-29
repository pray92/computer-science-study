# 거품 정렬(Bubble Sort)
- 서로 인접한 원소들끼리 비교해서 현재 인덱스 기준, 다음 인덱스의 원소가 크면 위치를 바꿔주는 방식으로 정렬
  1. `i`번째 인덱스부터 시작하여 다음 인덱스 `i+1`의 원소가 현재 `i`번째보다 크면 바꿔줌 -> `0 <= i < n-1`
  2. 이후 `i+1 <= i < n-i` 범위를 반복

## 그림
![bubble-sort](https://github.com/pray92/computer-science-study/assets/11500877/3b5bfe56-1c5e-4181-b795-7f3819543219)

## 시간 복잡도
- `(n-1) + (n-2) + … + 1 =  n(n+1)/2`가 되므로 `O(n^2)`
- 정렬 여부에 관계없이 동일한 흐름을 가지므로 최선, 평균, 최악 모두 `O(n^2)`

## 소스 코드
```java
public void bubbleSort(int[] arr) {
    for(int i = 0; i < arr.length; ++i) {
        for(int j = 1; j < arr.length - i; ++j) {
            if(arr[j] < arr[j - 1]) {
                swap(arr[j], arr[j - 1]);    
            }    
        }
    }    
}
```
