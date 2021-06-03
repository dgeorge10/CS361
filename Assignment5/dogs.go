package main

import (
   "fmt"
   "math/rand"
   "time"
   "os"
   "strconv"
)

type DogType struct {
	name string
	dog_type string
	busy chan bool
}

type KennelQueue struct {
	queue chan DogType
	inside []DogType
}

func Kennel(kq KennelQueue, finished chan string) {
	for {
		current_type := ""
		for doggo := range kq.queue {
			fmt.Println("pulled " + doggo.name + " off channel")
			if (current_type == "" || (current_type != "Doodle" && doggo.dog_type == current_type)) {
				fmt.Println("currently serving " + doggo.dog_type + " dogs")
				kq.inside = append(kq.inside, doggo)
				current_type = doggo.dog_type
			} else {
				kq.queue<-doggo
				fmt.Println("put dog back on queue")
				break
			}
		}

		if(len(kq.inside) > 0) {
			fmt.Println("currently inside")
			for _,doggo := range kq.inside {
				fmt.Print(doggo.name)
				fmt.Print(",")
			}
			fmt.Print("\n")

			fmt.Println("ready to serve dogs")
			for _, doggo := range kq.inside {
				fmt.Println(doggo.name + " is eating")
			}

			// simulate the amount of time it takes for dogs to eat
			sec := rand.Intn(10) * 1000
			time.Sleep(time.Duration(sec) * time.Millisecond)

			for _, doggo := range kq.inside {
				doggo.busy<-false
			}

			fmt.Println("all dogs are done eating")
			kq.inside = nil

			fmt.Println("done servicing " + current_type)
		 } else {
			fmt.Println("nobody is in line yet")
		 }
	}
	finished<-"done"
}


func Dog(dog DogType, queue chan DogType) {
	for {
		sec := rand.Intn(5) * 1000
		fmt.Println(dog.name + " is sleeping")
		time.Sleep(time.Duration(sec) * time.Millisecond)
		fmt.Println(dog.name + " is getting in line")
		queue<-dog
		<-dog.busy
	}
}

func main() {
	finished := make(chan string, 1)

	c_amount, _:= strconv.Atoi(os.Args[1])
	d_amount, _:= strconv.Atoi(os.Args[2])
	r_amount, _:= strconv.Atoi(os.Args[3])

	kennel_queue := KennelQueue {
		queue: make(chan DogType, 1000),
	}

	go Kennel(kennel_queue, finished)

	for i:=0; i<c_amount; i++ {
	   dog := DogType{fmt.Sprintf("%s%d", "coonhound",i), "Coonhound", make(chan bool, 1)}
	   go Dog(dog, kennel_queue.queue)
	}
	time.Sleep(1000 * time.Millisecond)
	for i:=0; i<r_amount; i++ {
	   dog := DogType{fmt.Sprintf("%s%d", "ridgeback",i), "Ridgeback", make(chan bool, 1)}
	   go Dog(dog, kennel_queue.queue)
	}
	time.Sleep(1000 * time.Millisecond)
	for i:=0; i<d_amount; i++ {
	   dog := DogType{fmt.Sprintf("%s%d", "doodle",i), "Doodle", make(chan bool, 1)}
	   go Dog(dog, kennel_queue.queue)
	}
	<-finished
}
