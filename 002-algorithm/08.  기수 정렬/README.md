# 기수 정렬(Radix Sort)
- 계수 정렬`Counting Sort`와 마찬가지로 **비교를 통해 정렬하지 않음**
- 데이터를 구성하는 요소의 기수Radix를 활용해 정렬 진행
- 정수를 정렬한다 가정하면 `첫째 자릿수를 기준으로 정렬 -> 둘째 자릿수를 기준으로 정렬 -> ...`을 반복해서 정렬

## 특징
- 시간 복잡도는 `O(d * (n + b))`, `d`는 `정렬한 숫자의 자릿수`, `b`는 `10`(각 자릿수의 범위는 `0~9`)
- 기수 정렬은 문자열이나 정수는 정렬이 가능하나, **자릿수가 없는 것은 정렬 불가** => 실수`Floating Number`
- 계수 정렬처럼 중간 결과를 저장할 배열이 따로 필요해, `In-place`하지 않음

## 소스 코드
- 큐`Queue` 또는 계수 정렬`Counting Sort`을 활용할 수 있음
- 여기선 계수 정렬을 활용해 구현

```java
public void radixSort(int[] arr) {
    int maxVal = Arrays.stream(arr).max().getAsInt();
    for(int exp = 1; maxVal / exp > 0; exp *= 10) {
        countingSort(arr, exp);    
    }
}

private void countingSort(int[] arr, int exp) {
    int[] buffer = new int[arr.length];
    int[] counting = new int[10];
    
    for(int i = 0; i < arr.length; ++i) {
        ++counting[(arr[i] / exp) % 10];     
    }
    
    for(int i = 1; i < arr.length; ++i) {
        counting[i] += counting[i - 1];    
    }
    
    for(int i = arr.length - 1; i >= 0; --i) {
        buffer[--counting[(arr[i] / exp) % 10]] = arr[i];    
    }
    arr = buffer;
}
```

## LSD vs. MSD
- `Least Significant Digit(LSD)`
  - 예시에 적용
  - 가장 낮은 자리 수부터 전개
- `Most Significant Digit(MSD)`
  - 정렬을 수행하는 과정에서 이미 정렬이 완료되어, 끝까지 수행해야 하는 `LSD`보단 효율적일 수 있음
    - ex. `10004`와 `70002` 비교
  - 그러나, 그러기 위해선 한번 기수 정렬을 수행하고 체크 처리를 해야 하는 데다, 구현은 `LSD`가 용이함
  - 이러한 이유로 주로 `LSD`가 언급됨
