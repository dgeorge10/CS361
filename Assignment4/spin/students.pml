#define NUM_STUDENTS 4

bool eating[NUM_STUDENTS] = false;
bool waiting[NUM_STUDENTS] = false;
bool coding[NUM_STUDENTS] = false;
bool ordering[NUM_STUDENTS] = false;
int forks[NUM_STUDENTS] = -1;
int cards[NUM_STUDENTS/2] = -1;
int phones[NUM_STUDENTS/2] = -1;

ltl sem { [](sem==1 || sem==0) } 
ltl fair0 { []<> (eating[0]) }
ltl fair1 { []<> (eating[1]) }
ltl fair2 { []<> (eating[2]) }
ltl fair3 { []<> (eating[3]) }

int BOWL_AMOUNT=10;
byte sem=1;
inline wait(sem) {
	atomic {
		sem>0;
		sem--;
	}
}

inline signal(sem) {
	sem++;
}

proctype student(int i) {
	int right=i; int left=(i+1)%NUM_STUDENTS;
Code: atomic { eating[i]=false; coding[i]=true;};
Wait: atomic { coding[i]=false; waiting[i]=true;};
	  if :: left < right;
	  /* try to get the left fork */
	  atomic { forks[left]==-1 -> forks[left]=i};
	  atomic { forks[right]==-1 -> forks[right]=i};
	  :: right < left;
	  /* try to get the right fork*/
	  atomic { forks[right]==-1 -> forks[right]=i;};
	  atomic { forks[left]==-1 -> forks[left]=i;};
	  fi;
Eat: atomic { waiting[i]=false; eating[i]=true; };
	 wait(sem);
	 BOWL_AMOUNT--;
	 printf("bowl amount: %d\n", BOWL_AMOUNT);
	 if :: BOWL_AMOUNT < 1 -> goto Order;
	    :: else -> atomic{ eating[i]=false; goto Done; };
	 fi;

Order: atomic { ordering[i]=true; eating[i]=false;};
	 printf("inside order");
	 if :: i%2==0;
	 /* try to get the left card / phone*/
	 atomic { cards[(i/2)-1 % NUM_STUDENTS/2]==-1 -> cards[(i/2)-1 % NUM_STUDENTS/2]=i;};
	 atomic { phones[i/2]==-1 -> phones[i/2]=i;};
	 :: i%2!=0;
	 /* try to get the right card / phone */
	 atomic { phones[(i/2)+1 % NUM_STUDENTS/2]==-1 -> phones[(i/2)+1 % NUM_STUDENTS/2]=i;};
	 atomic { cards[i/2]==-1 -> cards[i/2]=i;};
	 fi;
	 
	 printf("increasing bowl amount");
	 BOWL_AMOUNT=10;

	 atomic { ordering[i]=false; };
	 if :: i%2==0;
	 /* try to get the left card/phone*/
	 atomic { cards[(i/2)-1 % NUM_STUDENTS/2]==i -> cards[(i/2)-1 % NUM_STUDENTS/2]=-1;};
	 atomic { phones[i/2]==i -> phones[i/2]=-1;};
	 :: i%2!=0;
	 /* try to get the right card/phone */
	 atomic { phones[(i/2)+1 % NUM_STUDENTS/2]==i -> phones[(i/2)+1 % NUM_STUDENTS/2]=-1;};
	 atomic { cards[i/2]==i -> cards[i/2]=-1;};
     fi;


Done: forks[right]=-1; forks[left]=-1;
	 signal(sem);
	 goto Code;
}


init {
	int i = 0;
	do
		::	i >= NUM_STUDENTS -> break;
		:: else -> run student(i);
	i++;
	od
}
