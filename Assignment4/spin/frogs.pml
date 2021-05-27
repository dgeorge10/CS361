#define STONES 7

/* adapted from https://www.weizmann.ac.il/sci-tea/benari/sites/sci-tea.benari/files/uploads/keynotesAndEssays/invisible-slides.pdf */ 
/* verify acceptance of []!success */
#define success (\
	(stones[0]==female) && \
	(stones[1]==female) && \
	(stones[2]==female) && \
	(stones[4]==male)   && \
	(stones[5]==male)   && \
	(stones[6]==male)      \
	)

mtype = { none, male, female }
mtype stones[STONES];

ltl { []!success }

/* define maleFrog proctype */
proctype maleFrog(byte index) {
end:do
	/* male frog moves one space forward */
	:: 	atomic {
			(index < STONES-1) && 
			(stones[index+1] == none) -> 
			stones[index] = none; 
			stones[index+1] = male;
			index = index + 1;
		}
	/* male frog moves two spaces forward */
	:: atomic {
			(index < STONES-2) && 
	   		(stones[index+1] != none) && 
			(stones[index+2] == none) -> 
			stones[index] = none; 
			stones[index+2] = male;
			index = index + 2;
		}
	od
}

proctype femaleFrog(byte index) {
end:do
	/* female frog moves one space forward */
	:: atomic {
			(index > 0) && 
			(stones[index-1] == none) -> 
			stones[index] = none; 
			stones[index-1] = female;
			index = index - 1;
		}
	/* female frog moves two spaces forward */
	:: atomic {
			(index > 1) && 
	   		(stones[index-1] != none) && 
			(stones[index-2] == none) -> 
			stones[index] = none; 
			stones[index-2] = female;
			index = index - 2;
		}
	od
}

/* simulate */
init {
	atomic {
		stones[STONES/2] = none;
		byte i = 0;
        do
        :: i == STONES/2 -> break;
   		:: else -> 
             stones[i] = male;
             run maleFrog(i);
    		 stones[STONES-i-1] = female;
			 run femaleFrog(STONES-i-1);
             i++
        od
	}
}
