package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
       untilOne();
    }

    /**
     * 03. 그리디 - 거스름돈
     */
    private static void remnantMoney(int n) {
        int count = 0;

        // 큰 단위의 화폐부터 차례대로 확인
        int[] coinTypes = new int[]{500, 100, 50, 10};

        for (int coin : coinTypes) {
            count += n / coin;
            n %= coin;
        }

        System.out.println(count);
    }

    /**
     * 03. 그리디 - 큰 수의 법칙
     */
    private static void lawOfBigNumber() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] nmk = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int n = nmk[0];
        int m = nmk[1];
        int k = nmk[2];

        final int[] sortedArr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).sorted().toArray();
        final int first = sortedArr[sortedArr.length - 1];
        final int second = sortedArr[sortedArr.length - 2];

        int result = 0;

        while (true) {
            for(int i = 0; i < k; ++i) {
                if (m == 0) {
                    break;
                }
                result += first;
                --m;
            }
            if (m == 0) {
                break;
            }
            result += second;
            --m;
        }

        System.out.println(result);
    }

    /**
     * 03. 그리디 - 큰 수의 법칙2
     */
    private static void lawOfBigNumber2() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] nmk = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int n = nmk[0];
        int m = nmk[1];
        int k = nmk[2];

        final int[] sortedArr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).sorted().toArray();
        final int first = sortedArr[sortedArr.length - 1];
        final int second = sortedArr[sortedArr.length - 2];

        // 가장 큰 수가 더해지는 횟수 계산
        int count = (m / (k + 1)) * k + (m % (k + 1));

        int result = 0;
        result += count * first;        // 가장 큰수 더하기
        result += (m - count) * second; // 두번째 큰수 더하기

        System.out.println(result);
    }

    /**
     * 03. 그리디 - 숫자 카드 게임
     * @throws IOException
     */
    private static void numberCardGame() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] nm = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        int result = Integer.MIN_VALUE;

        for(int i = 0 ; i < nm[0]; ++i) {
            int minValue = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).min().getAsInt();
            result = Integer.max(result, minValue);
        }

        System.out.println(result);
    }

    /**
     * 03. 그리디 - 1이 될때까지
     * @throws IOException
     */
    private static void untilOne() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] nk = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int n = nk[0];
        int k = nk[1];

        int number = 1;
        int result = 0;
        while (number != n) {
            if (number * k > n) {
                ++number;
            } else {
                number *= k;
            }
            ++result;
        }
        System.out.println(result);
    }

}
