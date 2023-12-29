# 삽입 정렬(Insertion Sort)
- 2번째 원소부터 시작해, 이전 인덱스의 원소보다 작을 때까지 이전 인덱스로 이동해서 정렬
  1. `i`번째 원소를 임시 변수에 저장 -> `1 <= i < n`
  2. `i-1`번째 '이전' 원소부터 비교하여 임시 변수 값보다 작은 원소가 나올 때까지 진행
     - 이 과정에서 '이전' 원소를 뒤로 삽입을 진행
  3. 1번부터 반복

## 그림
![insertion-sort](https://github.com/pray92/computer-science-study/assets/11500877/758dec3d-5e52-4acd-8a9c-a9b3cd95933c)

## 특징
- 시간 복잡도는 최악의 경우 `O(n^2)`, 이미 정렬된 경우 교환 과정을 거치지 않아 최선의 경우 `O(n)`
- 또한 추가 배열을 필요하지 않아 `In-place`하며, `Stable`함
- 한번 진행하면 해당 원소가 정확한 위치에 지정된다는 점에서 선택 정렬`Selection Sort`과 유사
  - 그러나 정확한 위치를 찾기 위해 모든 원소를 찾아 비교하는 선택 정렬과 달리, 삽입 정렬`Insertion Sort`는 해당 위치를 찾는데 '필요한 만큼' 탐색
  - 따라서 선택 정렬보다 효율적으로 탐색해 정렬

## 소스 코드
```java
public void insertionSort(int[] arr) {
    for(int i = 1; i < arr.length; ++i) {
        int tmp = arr[i];
        int prev = i - 1;
        while((prev >= 0) && tmp < arr[prev]) {
            arr[prev + 1] = arr[prev];
            --prev;
        }
        arr[prev + 1] = tmp;
    }    
}
```