# 선택 정렬(Selection Sort)
- 단순하게 생각하면, **`0 ~ length-1`까지 시작해 해당 인덱스에 적절한 원소를 넣어 정렬**한다로 생각하면 됨
  1. `i`번째 인덱스부터 시작하여 `i+1`부터 쭉 확인해서 가장 작은 원소의 인덱스를 찾음
  2. `i`번째 원소와 `가장 작은 원소의 인덱스`번째 원소와 교환
     - i번째 원소는 위치가 정해짐`Fixed`
     - 이를 `length-1`까지 반복하면 정렬 완료

## 그림
![selection-sort](https://github.com/pray92/computer-science-study/assets/11500877/d2083c74-b344-4b40-8e93-fe58e1dc475a)

## 시간 복잡도(feat. 특징)
- 거품 정렬`Bubble Sort`과 마찬가지로 `O(n^2)`
- 차이가 있다면 교환 횟수가 비교적 적은 편 -> 거품 정렬보단 효율적
- 따로 배열이 추가로 필요하지 않아 `In-place`한 특징이 있음
- 그러나 정확한 위치에 있는 원소도 교환이 일어나는 경우가 있어 `Unstable`함

## 소스 코드
```java
public void selectionSort(int[] arr) {
    for(int i = 0; i < arr.length - 1; ++i) {
        int minIndex = i;
        for(int j = i + 1; j < arr.length; ++j) {
            if(arr[minIndex] > arr[j]) {
                minIndex = j;
            }   
        }    
        
        if(minIndex != i) {
            swap(arr[i], arr[minIndex]);
        }
    }
}
```