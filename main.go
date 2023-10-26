package main

import (
	"math/rand"
	"sync"
	"time"
)

type ArraySum struct {
	sum int
	mu  sync.Mutex
}

func main() {
	array1 := []int{1, 2, 3, 4, 5}
	arraySum1 := ArraySum{}

	array2 := []int{6, 7, 8, 9, 10}
	arraySum2 := ArraySum{}

	array3 := []int{11, 12, 13, 14, 15}
	arraySum3 := ArraySum{}

	var wg sync.WaitGroup
	wg.Add(3)

	go func() {
		defer wg.Done()

		for {
			arraySum1.mu.Lock()
			sum1 := arraySum1.sum
			arraySum1.mu.Unlock()

			sum2 := arraySum2.sum
			sum3 := arraySum3.sum

			if sum1 == sum2 && sum2 == sum3 {
				break
			}

			array1[rand.Intn(len(array1))] += rand.Intn(2) - 1

			arraySum1.mu.Lock()
			arraySum1.sum = 0
			for _, v := range array1 {
				arraySum1.sum += v
			}
			arraySum1.mu.Unlock()

			time.Sleep(10 * time.Millisecond)
		}
	}()

	go func() {
		defer wg.Done()

		for {
			arraySum2.mu.Lock()
			sum2 := arraySum2.sum
			arraySum2.mu.Unlock()

			sum1 := arraySum1.sum
			sum3 := arraySum3.sum

			if sum1 == sum2 && sum2 == sum3 {
				break
			}

			array2[rand.Intn(len(array2))] += rand.Intn(2) - 1

			arraySum2.mu.Lock()
			arraySum2.sum = 0
			for _, v := range array2 {
				arraySum2.sum += v
			}
			arraySum2.mu.Unlock()

			time.Sleep(10 * time.Millisecond)
		}
	}()

	go func() {
		defer wg.Done()

		for {
			arraySum3.mu.Lock()
			sum3 := arraySum3.sum
			arraySum3.mu.Unlock()

			sum1 := arraySum1.sum
			sum2 := arraySum2.sum

			if sum1 == sum2 && sum2 == sum3 {
				break
			}

			array3[rand.Intn(len(array3))] += rand.Intn(2) - 1

			arraySum3.mu.Lock()
			arraySum3.sum = 0
			for _, v := range array3 {
				arraySum3.sum += v
			}
			arraySum3.mu.Unlock()

			time.Sleep(10 * time.Millisecond)
		}
	}()

	wg.Wait()
}
